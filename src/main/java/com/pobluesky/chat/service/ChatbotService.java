package com.pobluesky.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.pobluesky.chat.dto.request.ChatRequestMsgDto;
import com.pobluesky.chat.dto.response.ChatCompletionDto;
import com.pobluesky.chat.feign.Customer;
import com.pobluesky.chat.feign.UserClient;
import com.pobluesky.chat.entity.type.ChatInquirySubType;
import com.pobluesky.chat.entity.type.ChatInquiryType;
import com.pobluesky.global.error.CommonException;
import com.pobluesky.global.error.ErrorCode;
import com.pobluesky.global.util.ResponseFactory;
import com.pobluesky.global.util.model.JsonResult;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final GptPromptService gptPromptService;

    private final UserClient userClient;

    private final ObjectMapper objectMapper;

    private final ResourceLoader resourceLoader;

    @Value("${openai.model}")
    private String openAiModel;

    @Value("${system.prompt.file.path.prefix}")
    private String promptFilePathPrefix;

    @Value("${system.prompt.file.path.suffix}")
    private String promptFilePathSuffix;

    public JsonResult<?> processChatMessage(String token,  String userMessage) {
        validateCustomer(token);

        String content = handleUserInquiry(userMessage);
        return ResponseFactory.getSuccessJsonResult(content);
    }

    private String handleUserInquiry(String userMessage) {
        ChatInquiryType inquiryType = determineInquiryType(userMessage);
        ChatInquirySubType inquirySubType = determineInquirySubType(userMessage, inquiryType);

        String response = switch (inquiryType) {
            case ORDER -> handleOrderInquiry(inquirySubType);
            case PRODUCT -> handleProductInquiry(inquirySubType);
            case REGISTRATION -> handleRegistrationInquiry(inquirySubType);
            case SITE_USAGE -> handleSiteUsageInquiry(inquirySubType);
            case OTHER -> null;
        };

        if (response == null) {
            return getGptResponseForInquiry(userMessage, inquiryType, inquirySubType);
        }
        return response;
    }

    private String handleOrderInquiry(ChatInquirySubType subType) {
        return switch (subType) {
            case SUBMISSION -> "고객님의 주문건은 관련 담당자 확인 후 접수되며, inquiry 등록일 기준 최소 2주의 시간이 소요됩니다. 주문건의 진행 상황은 'inquiry 조회'를 통해 추적 가능합니다.";
            case MODIFICATION -> "주문 변경은 현재 문의의 상태에 따라 가능 여부가 달라집니다. 주문 프로세스는 문의 제출 ▸ 문의 접수 ▸ 1차검토 완료 ▸ 품질검토 요청 ▸ 품질검토 접수 ▸ 품질검토 완료 ▸ 최종 검토 순서로 진행되며, 주문 변경은 '문의 제출' 단계에서만 가능합니다. \n\n "
                            + " 내 문의 진행 상황은 'inquiry 조회'에서 언제든지 확인 가능합니다.";
            case SHIPPING -> "네, 주문 배송지 변경은 현재 문의의 상태에 따라 가능 여부가 달라집니다. 주문 프로세스는 문의 제출 ▸ 문의 접수 ▸ 1차검토 완료 ▸ 품질검토 요청 ▸ 품질검토 접수 ▸ 품질검토 완료 ▸ 최종 검토 순서로 되며, 주문 배송지 변경은 '문의 제출' 단계에서만 가능합니다. \n\n"
                            + " 내 문의 진행 상황은 'inquiry 조회'에서 언제든지 확인 가능합니다.";
            case PROCESS -> "주문 프로세스는 문의 제출 ▸ 문의 접수 ▸ 1차검토 완료 ▸ 품질검토 요청 ▸ 품질검토 접수 ▸ 품질검토 완료 ▸ 최종 검토 순서로 진행됩니다. \n\n "
                            + "내 문의 진행 상황은 'inquiry 조회'에서 언제든지 확인 가능합니다.";
            case CONTRACT -> "최종검토 완료 이후, 고객님께 결과가 회신되며 계약 협의가 진행됩니다. 이후 절차는 담당자가 추후 메일로 안내드립니다. 자세한 문의는 아래 VoC 문의하기 버튼을 통해 남겨주시면 빠르게 답변해드릴게요!";
            default -> null;
        };
    }

    private String handleProductInquiry(ChatInquirySubType subType) {
        return switch (subType) {
            case LINE_ITEM -> "제품 유형에 따라 등록되어야 할 라인아이템 내역이 달라집니다. 제품 유형별 라인아이템 내역은 아래 표를 참고해주세요. ";
            case FILE_FORMAT -> "현재 라인아이템 내역 파일 등록 기능은 PDF 파일 확장만 지원 가능합니다. 제품 유형별 라인아이템 내역은 아래 표를 참고해주세요. \n\n"
                                + "다른 파일 형식의 업로드를 원하시면 아래 '직접 VoC 문의하기'를 통해 문의를 남겨주세요! 등록 과정을 상세히 도와드리겠습니다.";
            case ESTIMATE -> "제품 유형은 견적 문의와 견적&품질 문의로 나뉩니다. \n\n 견적 문의는 품질 검토 단계가 필요없는 문의 유형을 의미하며, 견적&품질 문의는 품질 담당자에 의한 품질 검토 단계가 필요한 문의 유형입니다. "
                                + "\n\n 고객님이 작성하신 inquiry 문의 유형은 담당자의 검토하에 변경될 수 있습니다. 더 자세한 문의는 아래 '직접 VoC문의하기'를 통해 남겨주시면 자세한 도움을 드리겠습니다!";
            case OFFERSHEET -> "offersheet 내역에 대한 다운로드는 inquiry ▸ inquiry 조회 ▸ 다운로드할 inquiry 선택 ▸ offersheet 내역 ▸ 엑셀 추출로 가능합니다. \n\n 다른 문의가 있다면 언제든 말씀해주세요!";
            default -> null;
        };
    }

    private String handleRegistrationInquiry(ChatInquirySubType subType) {
        return switch (subType) {
            case METHOD -> "사이트 접속 후 > 로그인 > inquiry 등록 단계를 통해 제품 유형별 맞춤 주문이 가능합니다. 회원이 아닐 경우 아래 '직접 VoC 문의하기'를 통해 문의를 남겨주세요. \n\n 다른 도움이 필요하신가요?";
            case MODIFICATION -> "inquiry 수정/삭제는 문의 상태에 따라 가능 여부가 달라집니다. 주문 프로세스는 문의 제출 ▸ 문의 접수 ▸ 1차검토 완료 ▸ 품질검토 요청 ▸ 품질검토 접수 ▸ 품질검토 완료 ▸ 최종 검토 순서로 진행되며, inquiry 수정/삭제는 '문의 제출' 단계에서만 가능합니다. \n\n "
                                + "또한 내 문의 진행 상황은 'inquiry 조회'에서 확인 가능합니다.";
            default -> null;
        };
    }

    private String handleSiteUsageInquiry(ChatInquirySubType subType) {
        if (subType == ChatInquirySubType.ACCOUNT_INFO) {
            return "마이페이지 정보 수정 방법은 아래와 같습니다. 홈페이지 상단 우측 프로필 선택 ▸ 설정 ▸ 변경하고 싶은 내용 수정 ▸ 수정 완료 \n\n 다른 도움이 필요하실까요?";
        }
        return null;
    }

    private ChatInquiryType determineInquiryType(String userMessage) {
        if (userMessage.contains("주문")) return ChatInquiryType.ORDER;
        if (userMessage.contains("제품")) return ChatInquiryType.PRODUCT;
        if (userMessage.contains("inquiry")) return ChatInquiryType.REGISTRATION;
        if (userMessage.contains("사이트") || userMessage.contains("마이페이지")) return ChatInquiryType.SITE_USAGE;
        return ChatInquiryType.OTHER;
    }

    private ChatInquirySubType determineInquirySubType(String userMessage, ChatInquiryType type) {
        return switch (type) {
            case ORDER -> {
                if (userMessage.contains("접수")) yield ChatInquirySubType.SUBMISSION;
                if (userMessage.contains("주문")) {
                    if (userMessage.contains("배송지")) yield ChatInquirySubType.SHIPPING;
                    else if (userMessage.contains("변경") || userMessage.contains("수정")) yield ChatInquirySubType.MODIFICATION;
                }
                if (userMessage.contains("프로세스") || userMessage.contains("절차")) yield ChatInquirySubType.PROCESS;
                if (userMessage.contains("결과") || userMessage.contains("계약")) yield ChatInquirySubType.CONTRACT;
                yield ChatInquirySubType.OTHER;
            }
            case PRODUCT -> {
                if (userMessage.contains("파일 형식") || userMessage.contains("파일") || userMessage.contains("형식") || userMessage.contains("등록 파일")) {
                    yield ChatInquirySubType.FILE_FORMAT;
                }
                if (userMessage.contains("라인아이템")) {
                    if (userMessage.contains("파일")) yield ChatInquirySubType.FILE_FORMAT;

                    else if (userMessage.contains("내역")) yield ChatInquirySubType.LINE_ITEM;
                }
                if (userMessage.contains("유형") || userMessage.contains("견적") || userMessage.contains("품질"))
                    yield ChatInquirySubType.ESTIMATE;
                if (userMessage.contains("offersheet") || userMessage.contains("오퍼시트"))
                    yield ChatInquirySubType.OFFERSHEET;
                yield ChatInquirySubType.OTHER;
            }
            case REGISTRATION -> {
                if (userMessage.contains("등록")) {
                    if(userMessage.contains("방법")) yield ChatInquirySubType.METHOD;
                    else if(userMessage.contains("수정") || userMessage.contains("삭제")) yield ChatInquirySubType.MODIFICATION;
                }
                yield ChatInquirySubType.OTHER;
            }
            case SITE_USAGE -> {
                if (userMessage.contains("전화번호") || userMessage.contains("비밀번호") || userMessage.contains("이메일")) {
                    yield ChatInquirySubType.ACCOUNT_INFO;
                }
                yield ChatInquirySubType.OTHER;
            }
            default -> ChatInquirySubType.OTHER;
        };
    }
    private String getGptResponseForInquiry(String userMessage, ChatInquiryType inquiryType, ChatInquirySubType inquirySubType) {
        List<ChatRequestMsgDto> messages = new ArrayList<>();

        String systemPrompt = loadPromptFromFile();

        String inquiryContext = "Provide a helpful response based on the customer's " + inquiryType + " inquiry, specifically about " + inquirySubType;

        messages.add(new ChatRequestMsgDto("system", systemPrompt));
        messages.add(new ChatRequestMsgDto("system", "You are a helpful assistant. " + inquiryContext));
        messages.add(new ChatRequestMsgDto("user", userMessage));

        ChatCompletionDto chatCompletionDto = new ChatCompletionDto(openAiModel, messages);

        return extractContentFromGptResponse(gptPromptService.prompt(chatCompletionDto));
    }

    private String loadPromptFromFile() {
        String promptFileName = "chatbot" + promptFilePathSuffix;
        String fullPromptPath = promptFilePathPrefix + promptFileName;

        try {
            Resource resource = resourceLoader.getResource(fullPromptPath);
            return Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CommonException(ErrorCode.SYSTEM_PROMPT_FILE_READ_ERROR);
        }
    }

    private String extractContentFromGptResponse(String gptResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(gptResponse);

            JsonNode contentNode = rootNode
                .path("choices")
                .path(0)
                .path("message")
                .path("content");

            if (contentNode.isMissingNode()) {
                throw new CommonException(ErrorCode.UNEXPECTED_GPT_RESPONSE);
            }

            return contentNode.asText();
        } catch (JsonProcessingException e) {
            throw new CommonException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    private void validateCustomer(String token) {
        Long userId = userClient.parseToken(token);

        Customer customer = userClient.getCustomerByIdWithoutToken(userId).getData();
        if(customer == null){
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

    }
}
