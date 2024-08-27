package info.ritesh.scm.services;

public interface EmailService {

	// Method to send
	void sendEmail(String to, String subject, String body) throws Exception;

	// Method to send email with HTML content
	void sendEmailWithHtml();

	// Method to send email with attachment
	void sendEmailWithAttachment();

}