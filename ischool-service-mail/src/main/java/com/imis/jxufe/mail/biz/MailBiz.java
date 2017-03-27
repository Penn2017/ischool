/**
 * 基于Dubbo的分布式系统架构视频教程，吴水成，wu-sc@foxmail.com，学习交流QQ群：367211134 .
 */
package com.imis.jxufe.mail.biz;

import com.imis.jxufe.param.MailParam;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;


/**
 * @描述: 邮件发送业务逻辑类 .
 * @作者: WuShuicheng .
 * @创建时间: 2015-3-18,上午1:08:22 .
 * @版本号: V1.0 .
 */
@Component("mailBiz")
public class MailBiz {

//    @Autowired
//    private JavaMailSender mailSender;// spring配置中定义
//    @Autowired
//    private SimpleMailMessage simpleMailMessage;// spring配置中定义

    private static Logger logger = LoggerFactory.getLogger(MailBiz.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPool;

    @Value(value = "${mail.host}")
    private String mailHost;

    @Value(value = "${mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value(value = "${mail.transport.protocol}")
    private String mailTransportProtocol;





    /**
     * 发送模板邮件
     *
     * @throws Exception
     */
    public void mailSend(final MailParam mailParam) {
        threadPool.execute(() -> {
            try {

//                simpleMailMessage.setFrom(simpleMailMessage.getFrom()); // 发送人,从配置文件中取得
//                simpleMailMessage.setTo(mailParam.getTo()); // 接收人
//                simpleMailMessage.setSubject(mailParam.getSubject());//邮件主题
//                simpleMailMessage.setText(mailParam.getContent());//邮件内容
//                mailSender.send(simpleMailMessage);


                //跟smtp服务器建立一个连接
                Properties p = new Properties();
                // 设置邮件服务器主机名
                logger.debug("-----------------mail.host:"+mailHost);
                p.setProperty("mail.host", mailHost);//指定邮件服务器，默认端口 25
                // 发送服务器需要身份验证
                logger.debug("-----------------mail.smtp.auth:"+mailSmtpAuth);
                p.setProperty("mail.smtp.auth", mailSmtpAuth);//要采用指定用户名密码的方式去认证
                // 发送邮件协议名称
                p.setProperty("mail.transport.protocol", mailTransportProtocol);

                // 开启SSL加密，否则会失败

                p.put("mail.smtp.ssl.enable", "true");

                MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
                mailSSLSocketFactory.setTrustAllHosts(true);
                p.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);

                // 开启debug调试，以便在控制台查看
                //session.setDebug(true);也可以这样设置
                //p.setProperty("mail.debug", "true");

                // 创建session
                Session session = Session.getDefaultInstance(p, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("penn2017@163.com","callme49tj");
                    }
                });
                logger.debug("-----------------session:"+(session==null));
                //发送邮件
                 doSendMail(session,mailParam);

            } catch (MessagingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        });
    }

    private void doSendMail(Session session, MailParam mailParam) throws MessagingException {
        logger.debug("------------------so far so good-------------------");
        logger.debug("------------------mail:-------------------"+mailParam);
        session.setDebug(true);//设置打开调试状态
        //声明一个Message对象(代表一封邮件),从session中创建
        MimeMessage msg = new MimeMessage(session);
        //邮件信息封装
        //1发件人
        logger.debug("--------mailParam.getFrom"+mailParam.getFrom());
        msg.setFrom(new InternetAddress(mailParam.getFrom()));

        //2收件人
        logger.debug("--------mailParam.getTo"+mailParam.getTo());
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(mailParam.getTo()));

        //3邮件内容:主题、内容
        logger.debug("--------mailParam.getSubject"+mailParam.getSubject());
        msg.setSubject(mailParam.getSubject());
        logger.debug("--------mailParam.getContent"+mailParam.getContent());
        msg.setContent(mailParam.getContent(),"text/html;charset=utf-8");//发html格式的文本
        logger.debug("------------------so far so good2-------------------");
        Transport.send(msg);
        logger.debug("------------------so far so good3-------------------");
    }


}
