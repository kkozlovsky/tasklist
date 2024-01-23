package ru.kerporation.tasklist.service;

import ru.kerporation.tasklist.domain.mail.MailType;
import ru.kerporation.tasklist.domain.user.User;

import java.util.Properties;

public interface MailService {

    void sendEmail(final User user,
                   final MailType type,
                   final Properties params);

}