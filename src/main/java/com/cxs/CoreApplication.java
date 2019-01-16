package com.cxs;

import com.cxs.core.utils.LogUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:config/dubbo-provider.xml"})
@MapperScan("com.cxs.*.*.dao")
public class CoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
        LogUtil.info("程序启动成功！ \\^0^/ ");
    }
}
