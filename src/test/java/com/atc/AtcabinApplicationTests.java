package com.atc;

import com.atc.entity.UserInfo;
import com.atc.service.SysService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AtcabinApplicationTests {

    @Resource
    private SysService sysService;


    @Test
    public void contextLoads() {
        UserInfo userinfo = new UserInfo();
        userinfo.setUserName("zhangsan");
        userinfo.setPassword("lisi");
        System.out.println(sysService.register(userinfo));
    }

}
