package com.pobluesky.chat.entity.type;

public enum ChatInquiryType {
    ORDER("Order", "주문 문의"),
    PRODUCT("Product", "제품 종합 문의"),
    REGISTRATION("Registration", "등록 문의"),
    SITE_USAGE("Site Usage", "사이트 이용 문의"),
    OTHER("Other", "기타 문의");

    private final String type;
    private final String name;

    ChatInquiryType(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
