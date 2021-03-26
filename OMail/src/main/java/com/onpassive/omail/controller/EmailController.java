package com.onpassive.omail.controller;

import java.io.FileNotFoundException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onpassive.omail.apis.EmailApis;
import com.onpassive.omail.constants.Constants;
import com.onpassive.omail.model.ResponseMessage;

@RestController
public class EmailController {

	@Autowired
	private EmailApis emailApis;

	@Autowired
	public HttpServletRequest request;

	@PostMapping("/sendmail")
	public ResponseEntity<ResponseMessage> sendEmail(@RequestParam("data") String data,
			@RequestParam(value = "files", required = false) MultipartFile[] files)
			throws MessagingException, JsonProcessingException, FileNotFoundException {
		if (emailApis.sendMailMultipart(data, files))
			return ResponseEntity
					.ok(new ResponseMessage(new Date(), HttpStatus.OK, Constants.SUCCESS, request.getRequestURI()));
		else
			return ResponseEntity
					.ok(new ResponseMessage(new Date(), HttpStatus.OK, Constants.FAILLED, request.getRequestURI()));
	}

	
	@PostMapping("/replyMail")
	public ResponseEntity<ResponseMessage> replyMail(@RequestParam("data") String data,
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam("folder") String folder) throws JsonProcessingException, MessagingException {
		if (emailApis.replyMail(data, files, folder))
			return ResponseEntity
					.ok(new ResponseMessage(new Date(), HttpStatus.OK, "Success", request.getRequestURI()));
		else
			return ResponseEntity
					.ok(new ResponseMessage(new Date(), HttpStatus.OK, Constants.FAILLED, request.getRequestURI()));
	}

	@PostMapping("/forwardMail")
	public ResponseEntity<ResponseMessage> forwardMail(@RequestParam("data") String data,
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam("folder") String folder) throws JsonProcessingException, MessagingException {
		if (emailApis.forwardMail(data, files, folder))
			return ResponseEntity
					.ok(new ResponseMessage(new Date(), HttpStatus.OK, Constants.SUCCESS, request.getRequestURI()));
		else
			return ResponseEntity
					.ok(new ResponseMessage(new Date(), HttpStatus.OK, Constants.FAILLED, request.getRequestURI()));
	}

}
