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
public class ScrapMap {
    @Id
    @GeneratedValue
    @Column(name="mapId")
    private Long id;
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
    @ManyToOne
    @JoinColumn(name="scpId")
    private Scrap scrap;
    @ManyToOne
    @JoinColumn(name="tagId")
    private Tag tag;
    private LocalDateTime postDate;

}
