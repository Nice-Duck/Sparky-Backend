package com.cmc.sparky.account.service;

import com.cmc.sparky.account.domain.Mail;
import com.cmc.sparky.account.dto.MailCheckRequest;
import com.cmc.sparky.account.dto.MailSendRequest;
import com.cmc.sparky.account.repository.MailRepository;
import com.cmc.sparky.common.dto.ServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final MailRepository mailRepository;
    @Value("${spring.mail.username}")
    private String email;
    public ServerResponse sendMail(MailSendRequest mailSendRequest){
        ServerResponse serverResponse =new ServerResponse();
        MimeMessage message=mailSender.createMimeMessage();
        String toAddress=mailSendRequest.getEmail();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");
            String id=toAddress.split("@")[0];
            String number="";
            Random rd=new Random();
            for(int i=0; i<6; i++){
                number+=String.valueOf(rd.nextInt(10));
            }
            mailRepository.save(new Mail(toAddress,number));
            String htmlStr = "<h3>안녕하세요! "+id+"님<br></h3>"+
                    "Sparky 서비스에 가입해주셔서 감사합니다.<br>인증번호는 다음과 같습니다.<br></h3>"+
                    "<h1>"+number+"<h1>";
            messageHelper.setText(htmlStr, true);
            messageHelper.setTo(toAddress);
            messageHelper.setSubject("[Sparky] 회원가입 인증 메일입니다.");
            messageHelper.setFrom(email,"Sparky");
            mailSender.send(message);
        }catch(Exception e){
            e.printStackTrace();
        }
        serverResponse.setCode("0000");
        serverResponse.setMessage("인증 번호를 전송했습니다.");
        serverResponse.setResult(null);
        return serverResponse;
    }
    public ServerResponse checkMail(MailCheckRequest mailCheckRequest){
        ServerResponse serverResponse =new ServerResponse();
        Mail mailNumber=mailRepository.findById(mailCheckRequest.getEmail()).orElse(null);
        if(mailNumber==null){
            serverResponse.setCode("0003");
            serverResponse.setMessage("인증 시간이 초과되었습니다.");
            serverResponse.setResult(null);
        }
        else if(!mailNumber.getNumber().equals(mailCheckRequest.getNumber())){
            serverResponse.setCode("0004");
            serverResponse.setMessage("메일 인증 번호가 일치하지 않습니다.");
            serverResponse.setResult(null);
        }
        else {
            serverResponse.setCode("0000");
            serverResponse.setMessage("메일 인증에 성공했습니다.");
            serverResponse.setResult(null);
        }
        return serverResponse;
    }
}
