package com.atc;

import com.atc.common.criteria.ExpandCriteria;
import com.atc.common.criteria.Restrictions;
import com.atc.common.enums.UserEnum;
import com.atc.dao.RegisterInfoRepository;
import com.atc.dao.UserRepository;
import com.atc.dao.entity.RegisterInfo;
import com.atc.dao.entity.UserInfo;
import com.atc.service.SysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AtcabinApplicationTests {

    @Resource
    private SysService             sysService;
    @Resource
    private RegisterInfoRepository registerInfoRepository;
    @Resource
    private UserRepository         userRepository;


    @Test
    public void contextLoads() throws ParseException {
        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setPhone("13980152803");
        registerInfo.setProjectName("测试项目1");
        registerInfo.setUserName("test1");
        registerInfo.setCompany("测试项目1");
        registerInfo.setBeginTime(org.apache.commons.lang3.time.DateUtils.parseDate("2017-04-01","yyyy-MM-dd"));
        registerInfo.setEndTime(org.apache.commons.lang3.time.DateUtils.parseDate("2022-04-01","yyyy-MM-dd"));
        registerInfoRepository.save(registerInfo);
        List<RegisterInfo> all = registerInfoRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void findRegister() {
        List<RegisterInfo> all = registerInfoRepository.findAll();
        System.out.println(JSONArray.fromObject(all));
    }

    @Test
    public void findUser() throws JsonProcessingException {
        List<RegisterInfo> all = registerInfoRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(all.get(0)));
    }

    @Test
    public void findReg() throws JsonProcessingException {
        List<UserInfo> all = userRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(all));
    }

    @Test
    public void  testSql() {
        Specification<UserInfo> specification = (Specification<UserInfo>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            final Join<Object, Object> operationLog = root.join("operationLogs", JoinType.INNER);
            Path<Object> closeTime = operationLog.get("closeTime");
            list.add(closeTime.isNull());
            list.add(criteriaBuilder.equal(root.get("status"), UserEnum.OFFLINE));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
        List<UserInfo> all = userRepository.findAll(specification);
        System.out.println(all);
    }

    @Test
    public void sessionListener(){
        Date date = new Date();
        ExpandCriteria<RegisterInfo> criteria = new ExpandCriteria<>();
        criteria.add(Restrictions.eq("userName", "test", false));
        criteria.add(Restrictions.gte("beginTime", date, false));
        criteria.add(Restrictions.lte("endTime", date, false));

//        int registerCount = registerRepository.countByUserNameAndPhoneAndProjectNameAndBeginTimeAndEndTime(userVo.getUserName(), userVo.getPhone(), project, startDate, endDate);
        List<RegisterInfo> registerInfos = registerInfoRepository.findAll(criteria);

    }

}
