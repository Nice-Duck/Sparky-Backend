package com.cmc.sparky.scrap.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagUpdateRequest {
  private Long tagId;
  private String name;
}
