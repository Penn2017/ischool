package com.imis.jxufe.mail.listener;

import com.google.gson.Gson;
import com.imis.jxufe.mail.biz.MailBiz;
import com.imis.jxufe.param.MailParam;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.Session;

/**
 * @author zhongping
 * @date 2017/3/24
 */


@Component("mailSessionAwareMessageListener")
public class MailSessionAwareMessageListener implements SessionAwareMessageListener {
    private static final Log LOGGER = LogFactory.getLog(MailSessionAwareMessageListener.class);
    private Gson gson = new Gson();
    @Autowired
    private MailBiz bailBiz;

    public synchronized void onMessage(Message message, Session session) {
        LOGGER.debug("===================this is recevie a mail!===========");
        try {
            ActiveMQTextMessage msg = (ActiveMQTextMessage) message;
            final String ms = msg.getText();
            LOGGER.info("==>receive message:" + ms);
            MailParam mailParam = gson.fromJson(ms, MailParam.class);// 转换成相应的对象
            if (mailParam == null) {
                return;
            }

            try {
                bailBiz.mailSend(mailParam);
            } catch (Exception e) {
                // 发送异常，重新发送一次
                bailBiz.mailSend(mailParam);
            }
        } catch (Exception e) {
            LOGGER.error("==>", e);
        }
    }
}
