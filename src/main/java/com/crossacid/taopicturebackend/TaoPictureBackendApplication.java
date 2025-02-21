package com.crossacid.taopicturebackend;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(exclude = {ShardingSphereAutoConfiguration.class})
@MapperScan("com.crossacid.taopicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class TaoPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaoPictureBackendApplication.class, args);
    }

}
