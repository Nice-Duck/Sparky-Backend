package com.cmc.sparky.scrap.domain;

import com.cmc.sparky.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue
    @Column(name="tagId")
    private Long id;
    @Column(nullable = false)
    private String name;
    private String color="#ffffff";
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
    private Boolean isDeleted=false;
}
