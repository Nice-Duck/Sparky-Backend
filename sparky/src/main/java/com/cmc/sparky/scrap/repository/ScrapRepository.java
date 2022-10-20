package com.cmc.sparky.scrap.repository;

import com.cmc.sparky.scrap.domain.Scrap;
import com.cmc.sparky.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Page<Scrap> findAllByUser(User user, Pageable pageable);

}
