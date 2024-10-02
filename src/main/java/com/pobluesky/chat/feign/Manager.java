package com.pobluesky.chat.feign;

import com.pobluesky.global.entity.Department;
import com.pobluesky.global.security.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Manager {
    Long userId;
    String empNo;
    String name;
    Department department;
    UserRole role;

}
