package com.atc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableAutoConfiguration                        // ：启用自动配置，该注解会使SpringBoot根据项目中依赖的jar包自动配置项目的配置项：
//@ComponentScan                                  // ：默认扫描@SpringBootApplication所在类的同级目录以及它的子目录。
@EnableScheduling
@SpringBootConfiguration
@EnableTransactionManagement                      // : 开启注解事务管理，等同于xml配置文件中的 <tx:annotation-driven />
@SpringBootApplication
@ServletComponentScan
public class AtcabinApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtcabinApplication.class, args);
    }

}
