package com.cmc.sparky.user.dto;

import javax.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryRequest {
  private String email;
  private String title;
  private String contents;
}
