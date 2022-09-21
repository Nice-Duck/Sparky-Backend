package com.cmc.sparky.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.UsesSunMisc;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {
   private final JavaMailSender mailSender;
    public void sendMail(String toAddress){
        MimeMessage message=mailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");
            String htmlStr = "<h1 style='color:blue;'> Hello World </h1>";
            messageHelper.setText(htmlStr, true);
            messageHelper.setTo(toAddress);
            messageHelper.setSubject("Sparky 회원가입 인증 메일입니다.");
            messageHelper.setFrom("user email","sparky");
            mailSender.send(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
