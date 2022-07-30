package nykon.oleg;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;


@RestController
@RequestMapping("/subscription")
public class MailController {
	public int checkMail(String newmail ) throws IOException {
		int code = 200;
			File file = new File("emails.txt");
		    if (!file.exists()) {
		    	file.createNewFile();
		    }
		    Scanner myReader = new Scanner(file);
		    while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		       
		    
		        if(newmail.equals(data))
		        {
		        	System.out.println(data +" ");
		        	code = 400;
		        }
		      }
		    myReader.close();
		    if(code != 400) {
		            FileWriter fileWriter = new FileWriter("emails.txt", true);
					fileWriter.write(newmail + "\n");
				    fileWriter.close();
		    }
					return code ;	
				
			
			
	
		
		
		
	}

	@PostMapping(path = "/subscribe",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE ) 
	public String subscribe(String email)  {
		try {
			if(checkMail(email) == 200)
			return "Email додано";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return "SomeTrouble";
		}
		return "409";
		
	}
	@PostMapping("/sendEmails")
	public void sendEmail() throws IOException {
		final String username = "nykon03@gmail.com";
        final String password = "zssfyavasyjnjdrn";
        File file = new File("emails.txt");
	    if (!file.exists()) {
	    	file.createNewFile();
	    }
	    int btcToUAH = RateController.getRate();
	    ArrayList<String> emails = new ArrayList<String>();
	    Scanner myReader = new Scanner(file);
	    while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	       emails.add(data);
	    }
	    myReader.close();
	    Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            for(String mail: emails) {
            message.setFrom(new InternetAddress("nykon03@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mail)
            );
            message.setSubject("Testing Gmail TLS");
            message.setText("Rate,"
                    + btcToUAH);

            Transport.send(message);

            System.out.println("Done");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
}
}

