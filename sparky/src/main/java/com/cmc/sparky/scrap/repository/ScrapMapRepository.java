package com.cmc.sparky.scrap.repository;

import com.cmc.sparky.scrap.domain.ScrapMap;
import com.cmc.sparky.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapMapRepository extends JpaRepository<ScrapMap, Long> {
    Page<ScrapMap> findAllByUser(User user, Pageable pageable);
}
