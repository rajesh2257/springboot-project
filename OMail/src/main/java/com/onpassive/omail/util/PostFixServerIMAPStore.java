package com.onpassive.omail.util;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.mail.util.MailSSLSocketFactory;

@Service
public class PostFixServerIMAPStore {
	
	private static final Logger logger = LoggerFactory.getLogger(PostFixServerIMAPStore.class);

	private Session session;

	private Store store;
	
	
	public PostFixServerIMAPStore() throws GeneralSecurityException {
		MailSSLSocketFactory mailSocketFactory = new MailSSLSocketFactory();
		mailSocketFactory.setTrustAllHosts(true);

		Properties properties = new Properties();
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.host", "omailuat.onpassive.com");
		properties.put("mail.smtp.socketFactory.port", "465"); // SSL Port
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.ssl.trust", "omailuat.onpassive.com");
		properties.put("email.sender.username", "jram");
		properties.put("email.sender.password", "user@123");
		properties.put("email.sender.imap.host", "omailuat.onpassive.com");
		properties.put("email.sender.imap.port", 993);
		properties.put("mail.store.protocol", "imap");
		properties.put("email.imap.ssl.enable", true);
		properties.put("mail.imaps.ssl.trust", "omailuat.onpassive.com");
		properties.put("mail.imap.ssl.socketFactory", mailSocketFactory);
		properties.put("mail.smtp.ssl.socketFactory", mailSocketFactory);

		session = Session.getInstance(properties, new Authenticator() {
			private PasswordAuthentication authentication;
			{
				authentication = new PasswordAuthentication(properties.getProperty("email.sender.username"),
						properties.getProperty("email.sender.password"));
			}

			protected PasswordAuthentication getPasswordAuthentication() {
				return authentication;
			}
		});
	}

	public Session getSmtpStore() {
		return session;
	}

	public Store getImapStore() throws MessagingException {
		//Store emailStore = null;
		store = session.getStore("imaps");
		store.connect(session.getProperty("email.sender.imap.host"), session.getProperty("email.sender.username"),
				session.getProperty("email.sender.password"));
		return store;
	}

	public void closeStore() {
		if (store != null) {
			try {
				store.close();
			} catch (MessagingException e) {
				logger.error("unable to close the Store {}", e.getMessage());
			}
		}
	}

}
