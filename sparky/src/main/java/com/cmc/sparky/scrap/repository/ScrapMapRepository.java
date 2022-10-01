package com.cmc.sparky.scrap.repository;

import com.cmc.sparky.scrap.domain.ScrapMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapMapRepository extends JpaRepository<ScrapMap, Long> {
}
