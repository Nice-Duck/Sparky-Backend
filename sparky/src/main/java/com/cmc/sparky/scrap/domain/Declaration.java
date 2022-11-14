package com.cmc.sparky.scrap.domain;

import com.cmc.sparky.user.domain.User;
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
public class Declaration {
  @Id
  @GeneratedValue
  @Column(name="declareId")
  private Long id;

  @ManyToOne
  @JoinColumn(name="userId")
  private User user;

  @ManyToOne
  @JoinColumn(name="scpId")
  private Scrap scrap;

}
