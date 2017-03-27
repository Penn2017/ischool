package com.imis.jxufe.interceptors;

import com.google.gson.Gson;
import com.imis.jxufe.model.Constant;
import com.imis.jxufe.model.ResponseEntity;
import com.imis.jxufe.redis.facade.RedisServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证用户是否登录的全局拦截器
 * @author zhongping
 * @date 2017/3/27
 */

public class LoginValidateInterceptor  implements HandlerInterceptor{

    @Autowired
    private RedisServiceFacade redisService;

    private Gson gson=new Gson();

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object o) throws Exception {

        String token = httpServletRequest.getParameter(Constant.USER_TOKEN);
        String userToken = redisService.get(token);
        if (userToken!=null) {
            //重新计算超时时间
            redisService.expire(token,Constant.USER_LOGIN_VALIDTE_TIME);
            return true;
        }
        //写回信息
        ResponseEntity responseEntity = new ResponseEntity(400, "没有登录");
        httpServletResponse.getWriter().write(gson.toJson(responseEntity));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }
}
