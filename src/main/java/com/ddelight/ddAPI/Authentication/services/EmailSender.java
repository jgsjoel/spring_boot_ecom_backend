package com.ddelight.ddAPI.Authentication.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;
    private ThreadPoolExecutor executor;


    private class EmailThreadFactory implements ThreadFactory{
        private AtomicInteger threadNumber = new AtomicInteger(1);
        private String threadNamePrefix = "EmailThread-";

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable,threadNamePrefix+threadNumber.getAndIncrement());
        }
    }

    @PostConstruct
    public void createThreadPool(){
        executor = new ThreadPoolExecutor(
                5,
                10,
                5000,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );
        executor.setThreadFactory(new EmailThreadFactory());
    }

    public void message(String template,String email){
        executor.execute(()->{
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    mimeMessage.setRecipient(Message.RecipientType.TO,
                            new InternetAddress(email));
                    mimeMessage.setFrom(new InternetAddress("1to8testing@gmail.com"));
                    mimeMessage.setContent(template,"text/html; charset=utf-8");
                }
            };

            try {
                this.mailSender.send(preparator);
            }
            catch (MailException ex) {
                System.err.println(ex.getMessage());
            }
        });
    }

}
