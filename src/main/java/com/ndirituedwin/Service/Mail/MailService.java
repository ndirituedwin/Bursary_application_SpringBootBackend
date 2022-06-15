package com.ndirituedwin.Service.Mail;

import com.ndirituedwin.Config.Constants.ApiUtils;
import com.ndirituedwin.Entity.Auth.NotificationEmail;
import com.ndirituedwin.Exception.MailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void SendMail(NotificationEmail notificationEmail){

        MimeMessagePreparator messagePreparator=mimeMessage -> {
            MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(ApiUtils.FROM_MAIL);
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try {
        javaMailSender.send(messagePreparator);
        log.info("Activation email sent to {}",notificationEmail.getRecipient());
        }catch (Exception exception){
            throw new MailException("an exception has occurred while trying to send email to "+notificationEmail.getRecipient());

        }

    }


}
