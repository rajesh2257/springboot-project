package com.onpassive.omail.model;

import java.util.Arrays;
import java.util.List;

public class Email {

	private int sno;
	private String from;
	private String replyTo;
	private String[] to;
	private String[] cc;
	private String subject;
	private String date;
	private String body;
	private Boolean attachment;
	private List<Attachments> attachments;
	private String folderName;

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Boolean getAttachment() {
		return attachment;
	}

	public void setAttachment(Boolean attachment) {
		this.attachment = attachment;
	}

	public List<Attachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachments> attachments) {
		this.attachments = attachments;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	@Override
	public String toString() {
		return "Email [sno=" + sno + ", from=" + from + ", replyTo=" + replyTo + ", to=" + Arrays.toString(to) + ", cc="
				+ Arrays.toString(cc) + ", subject=" + subject + ", date=" + date + ", body=" + body + ", attachment="
				+ attachment + ", attachments=" + attachments + ", folderName=" + folderName + "]";
	}

}
