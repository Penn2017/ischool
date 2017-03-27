package com.imis.jxufe.base.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongping
 * @date 2017/3/16
 */
public class ResponseEntity implements Serializable {

    private static final long serialVersionUID = -1483013122646260435L;

    /**
     * 响应状态
     */
    private int resStatus;

    /**
     * 响应信息
     */
    private String resMsg;

    /**
     * 响应参数
     */
    private transient Map<String, Object> params = new HashMap<>();

    public ResponseEntity() {
        /**
         * empty construct
         */
    }

    public ResponseEntity(int resStatus, String resMsg) {
        super();
        this.resStatus = resStatus;
        this.resMsg = resMsg;
    }

    public int getResStatus() {
        return resStatus;
    }

    public void setResStatus(int resStatus) {
        this.resStatus = resStatus;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
