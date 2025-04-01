package com.example.HealthCare.service;

import com.example.HealthCare.dto.SendMail.DataMailDTO;

public interface MailService {
    void sendHTMLMail(DataMailDTO dataMailDTO, String template);
}


