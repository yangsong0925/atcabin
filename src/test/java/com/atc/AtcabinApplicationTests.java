package com.atc;

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
import javax.persistence.criteria.*;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
    public void contextLoads() {
        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setPhone("13980152803");
        registerInfo.setProjectName("测试项目");
        registerInfo.setUserName("test");
        registerInfo.setCompany("测试项目");
        registerInfo.setBeginTime("2017-01-01");
        registerInfo.setEndTime("2022-01-01");
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
    public void sessionListener(HttpSession session){
        session.invalidate();
    }

}
