package com.onpassive.omail.apis;

import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onpassive.omail.constants.Constants;
import com.onpassive.omail.model.ComposeEmail;
import com.onpassive.omail.util.PostFixServerIMAPStore;
import com.sun.mail.imap.IMAPFolder;

@Service
public class EmailApis {

	@Autowired
	private JavaMailSender javaMailSender;

	/*
	 * @Value("${mail.username}") private String username;
	 * 
	 * @Value("${mail.password}") private String password;
	 */

	@Autowired
	private PostFixServerIMAPStore storeTemplate;

	@SuppressWarnings("deprecation")
	public boolean sendMailMultipart(String data, MultipartFile[] files)
			throws MessagingException, JsonMappingException, JsonProcessingException {

		MimeMessage msg = javaMailSender.createMimeMessage();
		// MimeMessage msg = new MimeMessage(storeTemplate.getSmtpStore());
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(msg, true, "utf-8");
		// String str = "@omailuat.onpassive.com";
		mimeMessageHelper.setFrom("jram");
		ComposeEmail emailsend = new ObjectMapper().readValue(data, ComposeEmail.class);
		mimeMessageHelper.setTo(emailsend.getToEmail());
		mimeMessageHelper.setCc(emailsend.getCcEmail());
		mimeMessageHelper.setBcc(emailsend.getBccEmail());
		mimeMessageHelper.setSubject(emailsend.getSubject());
		mimeMessageHelper.setText(emailsend.getBody());
		if (!StringUtils.isEmpty(files)) {
			for (int i = 0; i < files.length; i++) {
				String attachName = files[i].getOriginalFilename();
				if (attachName != null)
					mimeMessageHelper.addAttachment(attachName, files[i]);
			}
		}
		if (!StringUtils.isEmpty(emailsend.getInlines())) {
			String[] name = emailsend.getInlines();
			for (int i = 0; i < emailsend.getInlines().length; i++) {
				((Part) mimeMessageHelper).setContent(addInlines(name[i]));
			}
		}
		/*
		 * mimeMessageHelper.setText("<html><body> <img src='cid:tree'/> </body></html>"
		 * , true); FileSystemResource res = new FileSystemResource(new
		 * File("C:/Users/lenovo/Downloads/tree.jpg"));
		 * mimeMessageHelper.addInline("tree", res);
		 * 
		 */
		// Transport.send(msg);
		javaMailSender.send(msg);
		return true;

	}

	public MimeMultipart addInlines(String fileName) throws MessagingException {
		String addedCid = com.onpassive.omail.util.ApplicationUtil.generateRandomString(new Random(), 20);
		MimeMultipart multipart = new MimeMultipart("related");
		// first part (the html)
		MimeBodyPart textPart = new MimeBodyPart();
		String htmlText = "<img src=\"cid:" + addedCid + "\">";
		textPart.setContent(htmlText, "text/html");
		multipart.addBodyPart(textPart);

		// second part (the image)
		MimeBodyPart imgBodyPart = new MimeBodyPart();
		DataSource fds = new FileDataSource(fileName);
		imgBodyPart.setHeader(Constants.CONTENT_ID, addedCid);
		imgBodyPart.setDataHandler(new DataHandler(fds));
		multipart.addBodyPart(imgBodyPart);
		return multipart;
	}

	
	public boolean replyMail(String data, MultipartFile[] files, String folder)
			throws NumberFormatException, MessagingException, JsonMappingException, JsonProcessingException {

		Store mailStore = storeTemplate.getImapStore();
		Folder mailfolder = mailStore.getFolder(folder);
		mailfolder.open(Folder.READ_ONLY);
		ComposeEmail compose = new ObjectMapper().readValue(data, ComposeEmail.class);
	//	Message emailMessage = mailfolder.getMessage(Integer.valueOf(compose.getMailId()));
			Message emailMessage = mailfolder.getMessage(1);

		Message mimeMessage = new MimeMessage(storeTemplate.getSmtpStore());
		mimeMessage = (MimeMessage) emailMessage.reply(false);

		mimeMessage.setFrom(new InternetAddress("jram"));
		if (compose.getEmail() != null)
			mimeMessage.setText(compose.getEmail());
		else
			mimeMessage.setText("");
		if (mimeMessage.getSubject() != null)
			mimeMessage.setSubject(mimeMessage.getSubject());
		if (!compose.getToEmail().equals(""))
			mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(compose.getToEmail()));
		if (!compose.getCcEmail().equals("") && compose.getCcEmail() != null)
			mimeMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(compose.getCcEmail()));
		if (!compose.getBccEmail().equals("") && compose.getBccEmail() != null)
			mimeMessage.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(compose.getBccEmail()));
		// Create your new message part
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText("");

		// Create a multi-part to combine the parts
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// Create and fill part for the forwarded content
		messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(emailMessage.getDataHandler());

		// Add part to multipart
		multipart.addBodyPart(messageBodyPart);

		// Associate multipart with message
		mimeMessage.setContent(multipart);

		if (compose.getFilename() != null) {

			DataSource source = new FileDataSource(compose.getFilename().getOriginalFilename());
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(compose.getFilename().getOriginalFilename());
			multipart.addBodyPart(messageBodyPart);
			mimeMessage.setContent(multipart);
		}
		Transport.send(mimeMessage);
		mailfolder.close(false);
		mailStore.close();
		return true;
	}

	public boolean forwardMail(String data, MultipartFile[] files, String folder)
			throws AddressException, MessagingException, JsonMappingException, JsonProcessingException {

		Session session = storeTemplate.getSmtpStore();
		Store mailStore = storeTemplate.getImapStore();
		Folder folderName = mailStore.getFolder(folder);
		folderName.open(Folder.READ_ONLY);
		ComposeEmail compose = new ObjectMapper().readValue(data, ComposeEmail.class);
		Message emailMessage = folderName.getMessage(1);

		// Create the message to forward
		MimeMessage forward = new MimeMessage(session);

		// Fill in header
		forward.setSubject(emailMessage.getSubject());
		String str = "@omailuat.onpassive.com";
		forward.setFrom(new InternetAddress("jram"));
		if (compose.getToEmail() != null)
			forward.addRecipients(Message.RecipientType.TO, InternetAddress.parse(compose.getToEmail()));
		if (compose.getCcEmail() != null)
			forward.addRecipients(Message.RecipientType.CC, InternetAddress.parse(compose.getCcEmail()));
		if (compose.getBccEmail() != null)
			forward.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(compose.getBccEmail()));

		// Create your new message part
		BodyPart messageBodyPart = new MimeBodyPart();

		messageBodyPart.setText("");

		// Create a multi-part to combine the parts
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// Create and fill part for the forwarded content
		messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(emailMessage.getDataHandler());

		// Add part to multi part
		multipart.addBodyPart(messageBodyPart);

		// Associate multi-part with message
		forward.setContent(multipart);

		// Send message
		Transport.send(forward);
		moveToSent(forward);
		folderName.close();
		mailStore.close();
		return true;
	}

	public void moveToSent(MimeMessage message) throws MessagingException {
		Store store = storeTemplate.getImapStore();
		if (store != null) {
			IMAPFolder sentFolder = (IMAPFolder) store.getFolder(Constants.SENT_ITEMS);
			if (!sentFolder.exists()) {
				sentFolder.create(Folder.HOLDS_MESSAGES);
			}
			sentFolder.open(Folder.READ_WRITE);
			message.setFlag(Flag.SEEN, true);
			sentFolder.appendMessages(new Message[] { message });
			message.setFlag(Flag.RECENT, true);
		}

	}

}
