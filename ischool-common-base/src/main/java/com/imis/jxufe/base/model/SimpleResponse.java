package com.imis.jxufe.base.model;

import java.io.Serializable;

/**
 * 服务于各个service层的简单的响应载体
 * @author zhongping
 * @date 2017/3/29
 */
public class SimpleResponse  implements Serializable {
    private int status;
    private String Msg;

    public SimpleResponse() {
        /**
         * empty
         */
    }

    public SimpleResponse(int status, String msg) {
        this.status = status;
        Msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }
}
