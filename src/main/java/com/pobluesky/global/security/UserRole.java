package com.pobluesky.global.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    CUSTOMER("CUSTOMER", "고객사"),
    SALES("ROLE_SALES", "판매 담당자"),
    QUALITY("ROLE_QUALITY", "품질 담당자");


    private final String role;
    private final String name;
}
