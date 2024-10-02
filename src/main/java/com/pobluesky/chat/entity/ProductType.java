package com.pobluesky.chat.entity;

import lombok.Getter;

@Getter
public enum ProductType {

    COLD_ROLLED("Cold Rolled", "냉연"),
    HOT_ROLLED("Hot Rolled", "열연"),
    WIRE_ROD("Wire Rod", "선재"),
    THICK_PLATE("Thick Plate", "후판"),
    CAR("Car", "자동차");

    private final String englishName;
    private final String koreanName;

    ProductType(String englishName, String koreanName) {
        this.englishName = englishName;
        this.koreanName = koreanName;
    }
}
