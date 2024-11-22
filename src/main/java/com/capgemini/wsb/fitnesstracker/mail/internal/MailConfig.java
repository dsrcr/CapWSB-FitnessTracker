package com.capgemini.wsb.fitnesstracker.mail.internal;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the mail settings in the application.
 * The {@link MailProperties} class contains the settings related to the mail system,
 * such as SMTP server configurations, email addresses, and other mail-related settings.
 */
@Configuration
@EnableConfigurationProperties(MailProperties.class)
class MailConfig {
}