package com.sep490.bads.distributionsystem.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendMail(String to, String subject, String htmlContent);
}
