package com.imis.jxufe.mail.facade.impl;

import com.google.gson.Gson;
import com.imis.jxufe.facade.SenderMailServiceFacade;
import com.imis.jxufe.param.MailParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhongping
 * @date 2017/3/25
 */
@Component("SenderMailService")
public class SenderMailServiceImpl implements SenderMailServiceFacade {

    private Logger LOGGER = LoggerFactory.getLogger(SenderMailServiceImpl.class);

    @Resource(name ="activeMqJmsTemplate")
    private JmsTemplate activeMqJmsTemplate;

    private Gson gson = new Gson();

    @Override
    public void send(MailParam mail) {
        LOGGER.debug("===========begin send mail================:" + gson.toJson(mail));
        activeMqJmsTemplate.send(session -> session.createTextMessage(gson.toJson(mail)));
        LOGGER.debug("===========complete send mail================:" + gson.toJson(mail));

    }
}
