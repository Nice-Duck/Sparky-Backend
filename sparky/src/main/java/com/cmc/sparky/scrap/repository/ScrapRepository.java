package com.cmc.sparky.scrap.repository;

import com.cmc.sparky.scrap.domain.Scrap;
import com.cmc.sparky.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Page<Scrap> findByUserAndUsed(User user, Integer used, Pageable pageable);
    List<Scrap> findAllByTitleLikeAndUserNotOrderByPostDateDesc(String title, User user);
    List<Scrap> findAllByTitleLikeAndUserOrderByPostDateDesc(String title, User user);
}
