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
public class Scrap {
    @Id
    @GeneratedValue
    @Column(name="scpId")
    private Long id;
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
    @Column(length = 1000)
    private String title;
    @Column(length = 1000)
    private String subTitle;
    @Column(length = 1000)
    private String memo;
    @Column(length = 1000)
    private String imgUrl;
    @Column(length = 1000)
    private String scpUrl;
    private LocalDateTime postDate;
    private Integer open=1;
    private Integer used=1;
    private Integer declaration=0;
}
