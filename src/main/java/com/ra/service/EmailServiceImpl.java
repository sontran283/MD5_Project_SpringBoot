package com.ra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public String sendMail() {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("jav2306@gmail.com");
            simpleMailMessage.setTo("tranhongson283@gmail.com");
            simpleMailMessage.setText("Thank you for your purchase!!!");
            simpleMailMessage.setSubject("COGO fresh fruit store");
            javaMailSender.send(simpleMailMessage);
            return "OK sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
