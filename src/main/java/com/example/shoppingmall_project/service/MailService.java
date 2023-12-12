package com.example.shoppingmall_project.service;

import com.example.shoppingmall_project.model.vo.MailVO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail="nyg159@gmail.com";
    private static int number;

    public static void createNumber(){
        number = (int)(Math.random() * (90000)) + 100000;
    }

    public MimeMessage CreateMail(String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");

            String body ="";
            body += "<h3>" + "요청하신 인증 번호입니다." +"</h3>";
            body += "<h1>" + number + "</h1>";

            message.setText(body, "UTF-8", "html");

            System.out.println(number);


        }catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    public int sendMail(String mail){
        MimeMessage message = CreateMail(mail);

        javaMailSender.send(message);

        return number;

    }






    public void CreateMail(MailVO mailVO) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailVO.getReceiver());
        message.setFrom(senderEmail);
        message.setSubject(mailVO.getTitle());
        message.setText(mailVO.getContent());

        javaMailSender.send(message);
    }

}
