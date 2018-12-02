package com.atc;

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
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
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
        registerInfo.setUserName("杨松");
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

}
