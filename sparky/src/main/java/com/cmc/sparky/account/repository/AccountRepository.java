package com.cmc.sparky.account.repository;

import com.cmc.sparky.account.domain.Account;
import com.cmc.sparky.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
    Account findByUser(User user);
    boolean existsByEmail(String email);
}
