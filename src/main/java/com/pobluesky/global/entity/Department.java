package com.pobluesky.global.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Department {

    CRM("CRM", "냉연마케팅실"),
    HWM("HWM", "열연선재마케팅실"),
    EM("EM", "에너지조선마케팅실"),
    CMM("CMM", "자동차소재마케팅실"),
    SFM("SFM", "강건재가전마케팅실"),
    SM("SM", "스테인리스마케팅실");

    private final String code;

    private final String name;
}
