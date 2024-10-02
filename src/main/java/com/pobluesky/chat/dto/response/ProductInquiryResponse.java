package com.pobluesky.chat.dto.response;

import com.pobluesky.chat.entity.type.ChatInquirySubType;

public enum ProductInquiryResponse {
    LINE_ITEM("제품 유형에 따라 등록되어야 할 라인아이템 내역이 달라집니다. 자세한 내용은 inquiry 등록 화면에서 확인 가능합니다."),
    FILE_FORMAT("현재 라인아이템 등록 간소화 과정은 PDF 파일 형식만 지원 가능합니다. 다른 파일 형식의 업로드를 원하시면 VoC 문의로 남겨주세요."),
    ESTIMATE("견적 문의는 품질 검토 단계가 필요없는 경우이며, 견적&품질 문의는 품질 담당자에 의한 품질 검토 단계가 필요한 문의 유형입니다. 고객님이 작성하신 inquiry 문의 유형은 담당자의 검토하에 변경될 수 있습니다."),
    OFFERSHEET("offersheet 내역에 대한 다운로드는 inquiry > inquiry 조회 > 다운로드할 inquiry 선택 > offersheet 내역 > 엑셀로 추출 방법으로 가능합니다.");

    private final String response;

    ProductInquiryResponse(String response) {
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
