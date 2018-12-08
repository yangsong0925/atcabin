package com.atc.listener;

import com.atc.common.enums.UserEnum;
import com.atc.common.utils.ConstantUtils;
import com.atc.common.vo.UserSession;
import com.atc.dao.OperationLogRepository;
import com.atc.dao.UserRepository;
import com.atc.dao.entity.UserInfo;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.annotation.Resource;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

@Log4j
@WebListener
public class SessionListener implements HttpSessionListener {

    @Resource
    private UserRepository userRepository;
    @Resource
    private OperationLogRepository operationLogRepository;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        Object user = session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
        if(!ObjectUtils.allNotNull(user)){
            return;
        }
        UserSession userSession = (UserSession)user;
        UserInfo userInfo = userSession.getUserInfo();
        userRepository.updateStatusByUserId(userInfo.getUserId(),UserEnum.OFFLINE);
        String operationId = ObjectUtils.allNotNull(session.getAttribute(ConstantUtils.USER_OPERATION_LOG)) ? String.valueOf(session.getAttribute(ConstantUtils.USER_OPERATION_LOG)) : "" ;
        int count = operationLogRepository.countByOperationIdAndCloseTimeIsNull(operationId);
        if(count < 0 )
            return;
        String remark = DateFormatUtils.format(new Date(),"yyyy-MM-dd hh:mm:ss") + "未关闭舱门离开";
        operationLogRepository.updateRemarkById(operationId,remark);
    }


}
