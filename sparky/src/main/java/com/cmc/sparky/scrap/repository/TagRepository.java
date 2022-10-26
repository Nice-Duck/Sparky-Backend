package com.cmc.sparky.scrap.repository;

import com.cmc.sparky.scrap.domain.Tag;
import com.cmc.sparky.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByNameAndUser(String name, User user);
    List<Tag> findAllByUserOrderByIdDesc(User user);
}
