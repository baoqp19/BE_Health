package com.example.HealthCare.config;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();

        // // Định dạng cụ thể thay vì ISO
        // registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd
        // HH:mm:ss")
        // .withZone(ZoneId.of("Asia/Bangkok")));
        // registrar.registerFormatters(registry);
    }

}