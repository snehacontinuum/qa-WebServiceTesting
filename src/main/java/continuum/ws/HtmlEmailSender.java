package continuum.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;






/***************
 * Utility to send soapui reports
 * @author sneha.chemburkar
 *
 */
public class HtmlEmailSender {

	static String absolutePath=new File("").getAbsolutePath();

	public static void sendEmail(final String userName, final String password, String reciever,
			String subject, String message, File report)
	{
		try{
			// sets SMTP server properties
			Properties properties = new Properties();
			properties.setProperty("mail.smtp.host",Utility.getMavenProperties("emailHost"));
			properties.setProperty("mail.smtp.port", Utility.getMavenProperties("emailPort"));
			properties.setProperty("mail.smtp.auth", "true");

			properties.setProperty("mail.smtp.starttls.enable", "true");
			//   properties.setProperty("mail.smtp.EnableSSL.enable","true");
			//oo properties.setProperty("mail.smtp.ssl.trust",Utilities.getMavenProperties("emailHost"));

			Session session = Session.getInstance(properties,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			});

			

			// creates a new e-mail message
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress(userName));

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reciever));


			msg.setSubject(subject);
			msg.setSentDate(new Date());


			addReportToMailBody(msg, report);
			Transport.send(msg);
			System.out.println("********Sending report mail**********");

		} catch (MessagingException e) {
			System.out.println("****************Unable to Send Email : " + e.getMessage());
		}

	}
	public static void addReportToMailBody(Message msg,File report) throws MessagingException{

		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(new FileInputStream(report), writer);
			msg.setContent(writer.toString(), "text/html");
		} catch (IOException e) {
			System.out.println("Not able to retrive  report file");
			e.printStackTrace();
		}
	}


	public static void sendReport(){
		if(Utility.getMavenProperties("reportMail").equalsIgnoreCase("true"))
		{
			
			String sender=Utility.getMavenProperties("reportUser");
            String projectName=Utility.getMavenProperties("ProjectName");
			String subject= "SoapUI WebService Report for " + projectName;
			//System.out.println("Sending "+subject);				
			String message="SoapUI WebService Report for " + projectName;
			String password=Utility.getMavenProperties("reportPassword");
			File soapUIReport=null;
			if(Utility.getMavenProperties("jenkins").equalsIgnoreCase("true"))
				soapUIReport=new File(absolutePath+"//test-report//"+projectName+"//package-summary.html");
			else
			    soapUIReport=new File(absolutePath+"\\test-report\\"+projectName+"\\package-summary.html");
			String reciever=Utility.getMavenProperties("reportReciever");

			sendEmail(sender, password, reciever, subject, message, soapUIReport);

		}
	}
}





