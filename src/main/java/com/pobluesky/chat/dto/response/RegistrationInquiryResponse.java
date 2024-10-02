package com.pobluesky.chat.dto.response;

import com.pobluesky.chat.entity.type.ChatInquirySubType;

public enum RegistrationInquiryResponse {
    METHOD("localshot:9090/inq-list/customer 접속 후 > 로그인 > inquiry 등록 단계를 통해 제품 유형별 맞춤 주문이 가능합니다."),
    MODIFICATION("inquiry 수정/삭제는 '문의 제출' 단계에서만 가능합니다. 내 문의 상태 확인은 'inquiry 조회'에서 확인 가능합니다."),
    CONTRACT("최종검토완료 이후, 고객님께 결과가 회신된 후 계약 협의가 진행됩니다. 이후 절차는 담당자가 추후 메일로 안내드립니다. 자세한 문의는 VoC inquiry 문의로 남겨주세요.");

    private final String response;

    RegistrationInquiryResponse(String response) {
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
