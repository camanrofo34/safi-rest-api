package unv.upb.safi.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class MailUtil {

    private final Map<String, String> verificationCodes = new HashMap<>();
    private final JavaMailSender mailSender;

    @Autowired
    public MailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public boolean verifyCode(String email, String code) {
        return code.equals(verificationCodes.get(email));
    }

    public void removeCode(String email) {
        verificationCodes.remove(email);
    }


    public void sendVerificationCode(String email) {
        String code = generateRandomCode();
        verificationCodes.put(email, code);
        scheduleCodeExpiration(email);

        sendEmail(email, "Your verification code is: " + code);
    }

    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(999999)); // 6-digit random number
    }

    private void sendEmail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verification Code");
        message.setText(text);
        mailSender.send(message);
    }

    private void scheduleCodeExpiration(String email) {
        new Thread(() -> {
            try {
                TimeUnit.MINUTES.sleep(10); // Code expires in 10 minutes
                verificationCodes.remove(email);
            } catch (InterruptedException ignored) {}
        }).start();
    }
}
