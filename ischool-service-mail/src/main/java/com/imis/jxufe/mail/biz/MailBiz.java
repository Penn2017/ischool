/**
 * 基于Dubbo的分布式系统架构视频教程，吴水成，wu-sc@foxmail.com，学习交流QQ群：367211134 .
 */
package com.imis.jxufe.mail.biz;

import com.imis.jxufe.param.MailParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;


/**
 * @描述: 邮件发送业务逻辑类 .
 * @作者: WuShuicheng .
 * @创建时间: 2015-3-18,上午1:08:22 .
 * @版本号: V1.0 .
 */
@Component("mailBiz")
public class MailBiz {

    @Autowired
    private JavaMailSender mailSender;// spring配置中定义
    @Autowired
    private SimpleMailMessage simpleMailMessage;// spring配置中定义
    @Autowired
    private ThreadPoolTaskExecutor threadPool;

    /**
     * 发送模板邮件
     *
     * @throws Exception
     */
    public void mailSend(final MailParam mailParam) {
        threadPool.execute(() -> {
            try {
                simpleMailMessage.setFrom(simpleMailMessage.getFrom()); // 发送人,从配置文件中取得
                simpleMailMessage.setTo(mailParam.getTo()); // 接收人
                simpleMailMessage.setSubject(mailParam.getSubject());//邮件主题
                simpleMailMessage.setText(mailParam.getContent());//邮件内容
                mailSender.send(simpleMailMessage);
            } catch (MailException e) {
                throw e;
            }
        });
    }
}
