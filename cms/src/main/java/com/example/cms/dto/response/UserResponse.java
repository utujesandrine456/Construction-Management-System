package com.example.cms.dto.response;

import com.example.cms.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private String phoneNumber;
}
