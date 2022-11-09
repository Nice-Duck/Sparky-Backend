package com.cmc.sparky.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserRequest {
    private MultipartFile icon;
}
