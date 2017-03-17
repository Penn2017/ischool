package com.imis.jxufe.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Web操作工具类.
 *
 * @author penn.
 * @version 1.0,
 */
public final class SimpleWebUtil {


    public SimpleWebUtil() {
        /**
         * 空参构造器
         */
    }


    /**
     * 从RequestContextHolder中获取request
     * @return
     */
    public HttpServletRequest getRequest(){
        return   ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取response
     * @return
     */

    public HttpServletResponse getResponse(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取客户端的真实IP地址.<br/>
     * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，那么取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * @return IP.
     */
    public String getIpAddr() {

        HttpServletRequest request=getRequest();

        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip) && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")); // 截取第一个
        }
        return ip;
    }

    /**
     * 根据request和sessionKey获取session（如果调用处能提供request时则可调用此方法，性能高）.
     *
     * @return sessionValue .
     */
    public Object getSession(String sessionKey) {
        HttpServletRequest request=getRequest();
        return request.getSession().getAttribute(sessionKey);
    }

    /**
     * 保存Session值（如果调用处能提供request时则可调用此方法，性能高）.
     *
     * @param sessionKey   .
     * @param sessionValue .
     */
    public void putSession(String sessionKey, Object sessionValue) {
        HttpServletRequest request=getRequest();
        request.getSession().setAttribute(sessionKey, sessionValue);
    }


    /**
     * 根据session名称删除session值.
     *
     * @param sessionKey .
     */
    public void removeSession(String sessionKey) {
        HttpServletRequest request=getRequest();
        request.getSession().removeAttribute(sessionKey);
    }

    /**
     * 添加Cookie值（切记，为防止XSS劫持Cookie攻击，在向客户端返回Cookie值时记得设置HttpOnly）.
     *
     * @param name     cookie的名称 .
     * @param value    cookie的值 .
     * @param maxAge   cookie存放的时间(以秒为单位,假如存放三天,即3*24*60*60; 如果值为0, cookie将随浏览器关闭而清除).
     */
    public void addCookie( String name, String value, int maxAge) {
        HttpServletResponse response = getResponse();
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 根据某一Cookie名获取Cookie的值.
     *
     * @param name Cookie的名称 .
     * @return Cookie值.
     */
    public String getCookieByName(String name) {
        Map<String, Cookie> cookieMap = readCookieMap();
        if (cookieMap.containsKey(name)) {
            Cookie cookie = cookieMap.get(name);
            return cookie.getValue();
        } else {
            return null;
        }
    }

    /**
     * 从request中读取所有Cookie值,放入Map中.
     *
     * @return cookieMap.
     */
    private Map<String, Cookie> readCookieMap() {
        HttpServletRequest request=getRequest();
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int num = 0; num < cookies.length; num++) {
                cookieMap.put(cookies[num].getName(), cookies[num]);
            }
        }
        return cookieMap;
    }
}
