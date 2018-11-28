package com.atc.listener;

import com.atc.common.utils.ConstantUtils;
import com.atc.dao.OperationLogRepository;
import com.atc.dao.ProjectInfoRepository;
import com.atc.dao.entity.OperationLog;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Resource
    private OperationLogRepository operationLogRepository;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        String operationId = ObjectUtils.allNotNull(session.getAttribute(ConstantUtils.USER_OPERATION_LOG)) ? "" : String.valueOf(session.getAttribute(ConstantUtils.USER_OPERATION_LOG));
        int count = operationLogRepository.countByOperationIdAndCloseTimeIsNull(operationId);
        if(count < 0 )
            return;

    }


}
