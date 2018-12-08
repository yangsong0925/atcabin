package com.atc.task;

import com.atc.common.enums.UserEnum;
import com.atc.common.utils.AliyunSmsUtils;
import com.atc.dao.UserRepository;
import com.atc.dao.entity.UserInfo;
import lombok.extern.log4j.Log4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
@Component
public class ScheduledTasks {

    @Resource
    private UserRepository userRepository;

    @Scheduled(fixedRate = 180000)
    public void smsTask() {
        log.info("ScheduledTasks smsTask start ....");
        Specification<UserInfo> specification = (Specification<UserInfo>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            final Join<Object, Object> operationLog = root.join("operationLogs", JoinType.INNER);
            Path<Object> closeTime = operationLog.get("closeTime");
            list.add(closeTime.isNull());
            list.add(criteriaBuilder.equal(root.get("status"), UserEnum.OFFLINE));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
        List<UserInfo> userInfos = userRepository.findAll(specification);
        if (userInfos == null || userInfos.size() <= 0) return;
        List<String> phones = new ArrayList<>();
        ExecutorService smsThreadPool = Executors.newFixedThreadPool(5);
        int queryNum = 15;

        int index = 1;
        for (UserInfo userInfo : userInfos) {
            if (index % queryNum == 0) {
                SmsThread smsThread = new SmsThread(phones);
                phones = new ArrayList<>();
                smsThreadPool.execute(smsThread);
            } else {
                phones.add(userInfo.getPhone());
            }
            index++;
        }
        if (phones.size() > 0) {
            SmsThread smsThread = new SmsThread(phones);
            smsThreadPool.execute(smsThread);
        }
        smsThreadPool.shutdown();
        log.info("ScheduledTasks smsTask end ....");
    }

    class SmsThread extends Thread {

        private List<String> phones;

        SmsThread(List<String> phones) {
            this.phones = phones;
        }

        public void run() {
            try {
                for (String phone : phones) {
                    AliyunSmsUtils.sendMsg(phone);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
