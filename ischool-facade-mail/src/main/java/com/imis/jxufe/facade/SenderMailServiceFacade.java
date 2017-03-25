package com.imis.jxufe.facade;

import com.imis.jxufe.param.MailParam;

/**
 * 发送短信的接口
 * @author zhongping
 * @date 2017/3/25
 */
public interface SenderMailServiceFacade {


    /**
     * 发送短信
     * @param mail
     */
    void send(MailParam mail);

}
