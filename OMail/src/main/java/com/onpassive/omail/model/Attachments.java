package com.onpassive.omail.model;

public class Attachments {
	private String fileName;
	private String fileUrl;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	@Override
	public String toString() {
		return "Attachments [fileName=" + fileName + ", fileUrl=" + fileUrl + "]";
	}

	
}
