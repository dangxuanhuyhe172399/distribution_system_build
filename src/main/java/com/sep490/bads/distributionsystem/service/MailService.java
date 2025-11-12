package com.sep490.bads.distributionsystem.service;


public interface MailService {
    void sendMail(String to, String subject, String htmlContent);
}
