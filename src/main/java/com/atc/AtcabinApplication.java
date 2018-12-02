package com.atc;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

//@EnableAutoConfiguration                        // ：启用自动配置，该注解会使SpringBoot根据项目中依赖的jar包自动配置项目的配置项：
//@ComponentScan                                  //：默认扫描@SpringBootApplication所在类的同级目录以及它的子目录。
@SpringBootConfiguration
@EnableTransactionManagement                    // 开启注解事务管理，等同于xml配置文件中的 <tx:annotation-driven />
@SpringBootApplication
public class AtcabinApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtcabinApplication.class, args);
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(){
        //1.需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //2:添加fastJson的配置信息;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        /**
         * TODO 第一个SerializerFeature.PrettyFormat可以省略，毕竟这会造成额外的内存消耗和流量，第二个是用来指定当属性值为null是是否输出：pro:null
         　　　　　 * SerializerFeature.SkipTransientField
         */
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;
        return new HttpMessageConverters(converter);
    }

}
