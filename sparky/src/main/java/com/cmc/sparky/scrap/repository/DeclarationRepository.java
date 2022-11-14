package com.cmc.sparky.scrap.repository;

import com.cmc.sparky.scrap.domain.Declaration;
import com.cmc.sparky.scrap.domain.Scrap;
import com.cmc.sparky.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeclarationRepository extends JpaRepository<Declaration, Long> {
  Long countByScrap(Scrap scrap);
  Declaration findByScrapAndUser(Scrap scrap, User user);
}
