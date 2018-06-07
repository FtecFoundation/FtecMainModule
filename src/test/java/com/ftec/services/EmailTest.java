package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.resources.Resources;
import com.ftec.resources.Stocks;
import com.ftec.services.MailService.Email_BotTradesUser;
import com.ftec.services.MailService.Emails;
import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.AddressException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class EmailTest {
	
    @Autowired
    public MailService emailService;


    @Test
    public void sendEmails() throws AddressException, EmailException {
        List<String> users = new ArrayList<String>(){{
            add(Resources.sendToStatic);
        }};
        
        emailService.sendToMany(users, "Super subject", "Hello! You received this letter since you have been used the services of COINBOT trading modules.<br>" +
                "<br>" +
                "If the fields of algorithmic trading and crypto trading in general are interesting for you, pay attention to the Token Sale of the First Trading Ecosystem.<br>" +
                "<br>" +
                "Now there is a 25% discount on the ecosystem tokens. Also, all participants will be provided with a large number of advantages while using all future new modules of COINBOT and the First Trading Ecosystem.<br>" +
                "<br>" +
                "TOKEN SALE: https://ftec.tech/<br>" +
                "REFERRAL PROGRAM: https://ftec.tech/referral_reports/<br>" +
                "<br>" +
                "If you have already invested in Token Sale projects, we recommend you to pay attention to the First Trading Ecosystem since the project got very high grades from auditors and ratings.<br>" +
                "<br>" +
                "Icobench: 4.9/5 | Icomarks: 9.4/10 | Trackico: 5/5 <br>" +
                "ICOrating: Hype score: Very High | Risk score: Low<br>" +
                "<br>" +
                "Also, pay attention to a large number of documents presented on the website:<br>" +
                "<br>" +
                "How To Participate?<br>" +
                "Ambisafe Audit Report<br>" +
                "OnePager<br>" +
                "WhitePaper<br>" +
                "Howey Test<br>" +
                "Registration Certificate<br>" +
                "Benefits for tokenholders<br>" +
                "Marketing Plan<br>" +
                "Financial plan<br>" +
                "KYC ALMP<br>" +
                "Privacy Policy<br>" +
                "Pre-order Agreement<br>" +
                "Tokens Quantity<br>" +
                "Legal Opinion<br>" +
                "CNR<br>" +
                "Anti-Spam Policy<br>" +
                "<br>" +
                "The link to the promo, a video about the team, and Q&A session are attached to this letter.<br>" +
                "https://www.youtube.com/watch?v=4Ekaw7hctkg<br>" +
                "https://www.youtube.com/watch?v=qG8o-RA7GDY<br>" +
                "https://www.youtube.com/watch?v=yfxO-FnmKN8<br>");
    }

    @Test
    public void sendUniqueEmails() throws AddressException, EmailException {
        List<Email_BotTradesUser> users = new ArrayList<Email_BotTradesUser>(){{

            add(new Email_BotTradesUser(Resources.sendToStatic, true, new Locale("en"), "pair", "logs",Stocks.Binance));

        }};
        emailService.sendEmail(users, Emails.AutomaticTradesStarted);
    }

    @Test
    public void sendSimpleEmail() {
        emailService.sendSimpleMessageWithText(Resources.sendToStatic, "Test subject", "Test text");
    }
}