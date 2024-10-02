package com.pobluesky.chat.dto.response;

import com.pobluesky.chat.entity.type.ChatInquirySubType;

public enum OrderInquiryResponse {
    SUBMISSION("inquiry 등록일 기준 최소 2주 예상됩니다."),
    MODIFICATION("주문 변경은 '문의 제출' 단계에서만 가능합니다. 주문 프로세스는 문의 제출 -> 문의 접수 -> 1차검토 완료 -> 품질검토 요청 -> 품질검토 접수 -> 품질검토 완료 -> 최종 검토 순서로 진행됩니다. 내 문의 상태 확인은 'inquiry 조회'에서 확인 가능합니다."),
    SHIPPING("주문 배송지 변경은 '문의 제출' 단계에서만 가능합니다. 주문 프로세스는 문의 제출 -> 문의 접수 -> 1차검토 완료 -> 품질검토 요청 -> 품질검토 접수 -> 품질검토 완료 -> 최종 검토 순서로 진행됩니다. 내 문의 상태 확인은 'inquiry 조회'에서 확인 가능합니다."),
    PROCESS("주문 프로세스는 문의 제출 -> 문의 접수 -> 1차검토 완료 -> 품질검토 요청 -> 품질검토 접수 -> 품질검토 완료 -> 최종 검토 순서로 진행됩니다. 내 문의 상태 확인은 'inquiry 조회'에서 확인 가능합니다.");
    private final String response;

    OrderInquiryResponse(String response) {
        this.response = response;
    }

    public static String getResponse(ChatInquirySubType subType) {
        try {
            return valueOf(subType.name()).response;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
