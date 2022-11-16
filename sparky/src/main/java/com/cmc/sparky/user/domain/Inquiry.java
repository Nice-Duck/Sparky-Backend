package com.cmc.sparky.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Inquiry {
  @Id @GeneratedValue
  @Column(name="inquiryId")
  private Long id;
  @ManyToOne
  @JoinColumn(name="userId")
  private User user;
  @Column(nullable = false)
  private String email;
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private String contents;

}
