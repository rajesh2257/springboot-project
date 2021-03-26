package com.onpassive.omail.model;

import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

public class ComposeEmail {

	private String toEmail;
	private String ccEmail;
	private String bccEmail;
	private String subject;
	private String email;
	private MultipartFile filename;
	private String body;
	private String mailId;
	private String[] inlines;

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getCcEmail() {
		return ccEmail;
	}

	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}

	public String getBccEmail() {
		return bccEmail;
	}

	public void setBccEmail(String bccEmail) {
		this.bccEmail = bccEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public MultipartFile getFilename() {
		return filename;
	}

	public void setFilename(MultipartFile filename) {
		this.filename = filename;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String[] getInlines() {
		return inlines;
	}

	public void setInlines(String[] inlines) {
		this.inlines = inlines;
	}

	@Override
	public String toString() {
		return "ComposeEmail [toEmail=" + toEmail + ", ccEmail=" + ccEmail + ", bccEmail=" + bccEmail + ", subject="
				+ subject + ", email=" + email + ", filename=" + filename + ", body=" + body + ", mailId=" + mailId
				+ ", inlines=" + Arrays.toString(inlines) + "]";
	}

}
