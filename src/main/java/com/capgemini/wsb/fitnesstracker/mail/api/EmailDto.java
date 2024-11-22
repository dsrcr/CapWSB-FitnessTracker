package com.capgemini.wsb.fitnesstracker.mail.api;

/**
 * Represents an email message with recipient address, subject, and content.
 * This record is used to encapsulate the details of an email that needs to be sent.
 */
public record EmailDto(String toAddress, String subject, String content) {
}
