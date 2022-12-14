package com.cmc.sparky.account.service;

import com.cmc.sparky.account.domain.Mail;
import com.cmc.sparky.account.dto.MailCheckRequest;
import com.cmc.sparky.account.dto.MailSendRequest;
import com.cmc.sparky.account.repository.MailRepository;
import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.exception.ConflictException;
import com.cmc.sparky.common.exception.ErrorCode;
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
    private ServerResponse serverResponse =new ServerResponse();
    @Value("${spring.mail.username}")
    private String email;
    public ServerResponse sendMail(MailSendRequest mailSendRequest){
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
            String htmlStr = "<img src='https://sparkyapp.s3.amazonaws.com/Group+7221.png'  height='48' width='48'><br><br>"
                    +"<span style='color:rgb(0,0,0);font-size:medium'>"
                    +"안녕하세요, "+id+"님!<br><br>"+
                    "Sparky에 가입하시려면 앱에 이 인증 번호를 입력해 주세요.<br>다른 사람과 번호를 공유하지 마세요.<br><br></span>"+
                    "<h1>"+number+"<h1>";
            messageHelper.setText(htmlStr, true);
            messageHelper.setTo(toAddress);
            messageHelper.setSubject("Sparky ID 인증 번호");
            messageHelper.setFrom(email,"Sparky");
            mailSender.send(message);
        }catch(Exception e){
            e.printStackTrace();
        }
        return serverResponse.success("인증 번호를 전송했습니다.");
    }
    public ServerResponse checkMail(MailCheckRequest mailCheckRequest){
        Mail mailNumber=mailRepository.findById(mailCheckRequest.getEmail()).orElse(null);
        if(mailNumber==null){
            throw new ConflictException(ErrorCode.EXPIRE_NUMBER);
        }
        if(!mailNumber.getNumber().equals(mailCheckRequest.getNumber())){
            throw new ConflictException(ErrorCode.INVALID_NUMBER);
        }
        return serverResponse.success("메일 인증에 성공했습니다.");
    }
}
