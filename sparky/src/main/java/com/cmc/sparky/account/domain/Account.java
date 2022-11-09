package com.cmc.sparky.account.domain;

import com.cmc.sparky.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    @Column(name="acntId")
    private Long id;
    @OneToOne
    @JoinColumn(name="userId")
    private User user;
    @Column(unique = true)
    private String email;
    private String password;
    private Integer used=1;
}
