package com.voxai;

import com.voxai.communication.ServerAddressProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {
    // voxai-common
    "com.voxai.common",
    "com.voxai.communication",
    "com.voxai.utils",
    // voxai-service (全量)
    "com.voxai.agent",
    "com.voxai.authrole",
    "com.voxai.config",
    "com.voxai.device",
    "com.voxai.mcptoolexclude",
    "com.voxai.message",
    "com.voxai.monitoring",
    "com.voxai.operationlog",
    "com.voxai.permission",
    "com.voxai.role",
    "com.voxai.security",
    "com.voxai.service",
    "com.voxai.storage",
    "com.voxai.summary",
    "com.voxai.template",
    "com.voxai.user",
    "com.voxai.userauth",
    "com.voxai.verifycode",
    "com.voxai.task",
    // voxai-ai
    "com.voxai.ai",
    // voxai-server
    "com.voxai.file",
    "com.voxai.mcpserver",
    "com.voxai.memory",
    "com.voxai.music",
    "com.voxai.server",
    },
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)
    }
)
@MapperScan({
    "com.voxai.authrole.dal.mysql.mapper",
    "com.voxai.config.dal.mysql.mapper",
    "com.voxai.device.dal.mysql.mapper",
    "com.voxai.mcptoolexclude.dal.mysql.mapper",
    "com.voxai.message.dal.mysql.mapper",
    "com.voxai.permission.dal.mysql.mapper",
    "com.voxai.role.dal.mysql.mapper",
    "com.voxai.authrolepermission.dal.mysql.mapper",
    "com.voxai.summary.dal.mysql.mapper",
    "com.voxai.template.dal.mysql.mapper",
    "com.voxai.userauth.dal.mysql.mapper",
    "com.voxai.user.dal.mysql.mapper",
    "com.voxai.verifycode.dal.mysql.mapper",
    "com.voxai.operationlog.dal.mysql.mapper",
})
@Slf4j
public class VoxAIApplication {

    @Autowired
    private ServerAddressProvider serverAddressProvider;

    public static void main(String[] args) {
        SpringApplication.run(VoxAIApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        log.info("==========================================================");
        log.info("OTA服务地址: {}", serverAddressProvider.getOtaAddress());
        log.info("==========================================================");
    }
}
