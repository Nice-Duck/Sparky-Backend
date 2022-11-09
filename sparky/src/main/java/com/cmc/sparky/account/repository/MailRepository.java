package com.cmc.sparky.account.repository;

import com.cmc.sparky.account.domain.Mail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends CrudRepository<Mail,String> {
}
