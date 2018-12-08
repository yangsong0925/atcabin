package com.atc.filter;

import com.alibaba.fastjson.JSON;
import com.atc.common.enums.UserEnum;
import com.atc.common.utils.ConstantUtils;
import com.atc.common.utils.ResultUtil;
import com.atc.common.vo.UserSession;
import com.atc.dao.entity.UserInfo;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession(false);
        if (!ObjectUtils.allNotNull(session)){
            outMessage(httpServletResponse);
            return false;
        }
        Object user = session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
        if(!ObjectUtils.allNotNull(user)){
            outMessage(httpServletResponse);
            return false;
        }
        UserSession userSession = (UserSession)user;
        UserInfo userInfo = userSession.getUserInfo();
        if (!UserEnum.SUCCESS.equals(userInfo.getStatus())){
            outMessage(httpServletResponse);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {


    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private void outMessage(HttpServletResponse httpServletResponse){
        PrintWriter writer = null;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("text/html; charset=utf-8");
        try {
            writer = httpServletResponse.getWriter();
            String rsultJson= JSON.toJSONString(ResultUtil.error("请登录!"));
            writer.print(rsultJson);
        } catch (IOException e) {
            log.error("response error",e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
