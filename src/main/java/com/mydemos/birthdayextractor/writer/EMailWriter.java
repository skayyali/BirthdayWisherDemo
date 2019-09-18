package com.mydemos.birthdayextractor.writer;

import com.mydemos.birthdayextractor.model.EMail;
import com.mydemos.birthdayextractor.service.EMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EMailWriter {
    @Autowired
    private EMailService eMailService;

    @Autowired
    private EMail mail;

    public void sendBirthdayEmail(String name, String toEMail, String message)  {
        mail.setFrom("Said");
        mail.setTo(toEMail);
        mail.setSubject("Happy Birthday! :)2");
        mail.setContent(message);

        eMailService.sendSimpleMessage(mail);
    }
}
