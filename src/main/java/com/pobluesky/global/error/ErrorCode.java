package com.pobluesky.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Global
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "G0001", "알 수 없는 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "G0002", "잘못된 요청입니다."),
    EXTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G0003", "외부 서버 오류입니다."),
    INVALID_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "G0004","권한 정보가 없는 토큰입니다."),
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G0005","JSON 처리 중 오류가 발생했습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "U0001", "존재하지 않는 사용자입니다."),
    ALREADY_EXISTS_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "U0004","이미 존재하는 이메일입니다."),
    REQ_MANAGER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "U0002", "존재하지 않는 요청 담당자입니다."),
    RES_MANAGER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "U0003", "존재하지 않는 응답 담당자입니다."),
    INVALID_PASSWORD(HttpStatus.INTERNAL_SERVER_ERROR, "U0005", "비밀번호가 틀립니다."),
    USER_NOT_MATCHED(HttpStatus.INTERNAL_SERVER_ERROR, "U0007","적합한 사용자가 아닙니다."),
    UNAUTHORIZED_USER_SALES(HttpStatus.INTERNAL_SERVER_ERROR, "U0008", "판매 담당자가 아닙니다."),
    UNAUTHORIZED_USER_QUALITY(HttpStatus.INTERNAL_SERVER_ERROR, "U0009", "품질 담당자가 아닙니다."),
    UNAUTHORIZED_USER_CUSTOMER(HttpStatus.INTERNAL_SERVER_ERROR, "U0010", "고객사가 아닙니다."),
    UNAUTHORIZED_USER_MANAGER(HttpStatus.INTERNAL_SERVER_ERROR, "U0011", "담당자가 아닙니다."),
    SALES_MANAGER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "U0012", "존재하지 않는 판매 담당자입니다."),
    QUALITY_MANAGER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "U0013", "존재하지 않는 품질 담당자입니다."),

    // Inquiry
    INQUIRY_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "I0001", "존재하지 않는 문의입니다."),
    INVALID_ORDER_CONDITION(HttpStatus.INTERNAL_SERVER_ERROR, "I0002", "올바르지 않은 정렬 조건입니다."),
    PROGRESS_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "I0003", "존재하지 않는 진행단계입니다."),
    INVALID_PROGRESS_UPDATE(HttpStatus.INTERNAL_SERVER_ERROR, "I0004", "올바르지 않은 Progress 업데이트 요청입니다."),
    INQUIRY_UNABLE_TO_MODIFY(HttpStatus.INTERNAL_SERVER_ERROR, "I0005", "이미 접수되어 수정 불가능한 문의입니다."),
    INQUIRY_UNABLE_ALLOCATE(HttpStatus.INTERNAL_SERVER_ERROR, "I0006", "해당 문의에는 담당자를 배정할 수 없습니다."),
    INQUIRY_LIST_EMPTY(HttpStatus.NO_CONTENT, "I0007", "해당 제품 유형에 대한 문의가 없습니다."),
    INQUIRY_INVALID_PRODUCTTYPE(HttpStatus.INTERNAL_SERVER_ERROR, "I0008", "올바르지 않은 Product Type 요청입니다."),
    INQUIRY_NOT_MATCHED(HttpStatus.INTERNAL_SERVER_ERROR, "I0009", "해당 사용자가 작성한 Inquiry가 아닙니다."),
    DEPARTMENT_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "I0010", "존재하지 않는 부서입니다."),

    // Review
    REVIEW_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "R0001", "존재하지 않는 검토입니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, "R0002", "해당 Inquiry에는 이미 제출된 검토가 존재합니다."),

    // LineItem
    LINE_ITEM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "L0001", "존재하지 않는 라인아이템입니다."),

    // Quality
    QUALITY_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Q0001", "존재하지 않는 품질입니다."),
    QUALITY_REVIEW_INFO_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Q0002", "존재하지 않는 품질검토정보입니다."),
    QUALITY_ALREADY_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, "Q0003", "해당 Inquiry에는 이미 제출된 품질이 존재합니다."),

    // OfferSheet
    OFFERSHEET_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "O0001", "존재하지 않는 오퍼시트입니다."),

    // Notification
    NOTIFICATION_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "N0001", "존재하지 않는 알림입니다."),

    // Question
    QUESTION_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Q0001", "존재하지 않는 질문입니다."),
    QUESTION_STATUS_COMPLETED(HttpStatus.INTERNAL_SERVER_ERROR, "Q0002", "이미 답변이 완료된 질문입니다."),
    QUESTION_NOT_MATCHED(HttpStatus.INTERNAL_SERVER_ERROR, "Q0003", "해당 사용자가 작성한 질문이 아닙니다."),
    QUESTION_ALREADY_DELETED(HttpStatus.INTERNAL_SERVER_ERROR, "Q0004", "이미 삭제된 질문입니다."),

    // Answer
    ANSWER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "A0001", "존재하지 않는 답변입니다."),
    ANSWER_NOT_MATCHED(HttpStatus.INTERNAL_SERVER_ERROR, "A0002", "해당 담당자가 작성한 답변이 아닙니다."),
    ANSWER_ALREADY_DELETED(HttpStatus.INTERNAL_SERVER_ERROR, "A0003", "이미 삭제된 답변입니다."),

    // Collaboration
    COLLABORATION_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "C0001", "존재하지 않는 협업입니다."),
    COLLABORATION_STATUS_READY(HttpStatus.INTERNAL_SERVER_ERROR, "C0002", "협업이 진행 중인 답변으로 수정 및 삭제할 수 없습니다."),
    COLLABORATION_STATUS_INPROGRESS(HttpStatus.INTERNAL_SERVER_ERROR, "C0003", "이미 진행중인 협업입니다."),
    COLLABORATION_STATUS_COMPLETED(HttpStatus.INTERNAL_SERVER_ERROR, "C0004", "이미 완료된 협업입니다."),
    COLLABORATION_STATUS_REFUSED(HttpStatus.INTERNAL_SERVER_ERROR, "C0005", "이미 거절된 협업입니다."),
    COLLABORATION_INFO_MISMATCH(HttpStatus.INTERNAL_SERVER_ERROR, "C0006", "일치하지 않은 협업 정보입니다."),
    RESMANAGER_NOT_MACHED(HttpStatus.INTERNAL_SERVER_ERROR, "C0007", "해당 협업의 응답 담당자가 아닙니다."),
    REQMANAGER_NOT_MACHED(HttpStatus.INTERNAL_SERVER_ERROR, "C0008", "해당 협업의 요청 담당자가 아닙니다."),

    // AI
    OCR_PROCESS_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AI001", "텍스트 추출에 실패했습니다."),
    UPLOAD_FAIL_TO_GOOGLE(HttpStatus.INTERNAL_SERVER_ERROR, "AI002", "구글 스토리지에 파일 업로드를 실패했습니다."),
    PDF_CONVERSION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI003", "PDF를 통한 이미지 변환에 실패했습니다."),
    PDF_CONVERSION_NO_IMAGES(HttpStatus.INTERNAL_SERVER_ERROR, "AI004", "변환된 이미지가 존재하지 않습니다."),
    UNEXPECTED_GPT_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "AI005", "GPT응답이 올바르지 않습니다."),
    SYSTEM_PROMPT_FILE_READ_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI006", "챗봇용 프롬프트 파일을 불러오지 못했습니다."),
    UNSUPPORTED_FILE_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "AI007", "지원하지 않는 파일 형식입니다."),
    FILE_CONVERSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI008", "파일을 통한 이미지 변환에 실패했습니다."),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "AI009", "유효하지 않은 파일 이름입니다.");

    private HttpStatus status;
    private String code;
    private String message;
}