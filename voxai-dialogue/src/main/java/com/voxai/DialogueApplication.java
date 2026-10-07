package com.voxai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Dialogue 独立启动入口
 * <p>
 * 包含：WebSocket/MQTT。
 * 可横向扩展，通过 Redis Pub/Sub 与其他实例协作。
 * <p>
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
@ComponentScan(
    basePackages = {
        // voxai-common
        "com.voxai.common",
        "com.voxai.communication",
        "com.voxai.utils",
        // voxai-service (dialogue 需要的部分)
        "com.voxai.config",
        "com.voxai.storage",
        "com.voxai.device",
        "com.voxai.mcptoolexclude",
        "com.voxai.message",
        "com.voxai.monitoring",
        "com.voxai.role",
        "com.voxai.summary",
        "com.voxai.task",
        "com.voxai.verifycode",
        // voxai-ai
        "com.voxai.ai",
        // voxai-dialogue
        "com.voxai.dialogue",
    },
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)
    }
)
@MapperScan({
    "com.voxai.config.dal.mysql.mapper",
    "com.voxai.device.dal.mysql.mapper",
    "com.voxai.mcptoolexclude.dal.mysql.mapper",
    "com.voxai.message.dal.mysql.mapper",
    "com.voxai.role.dal.mysql.mapper",
    "com.voxai.summary.dal.mysql.mapper",
    "com.voxai.verifycode.dal.mysql.mapper",
})
public class DialogueApplication {

    public static void main(String[] args) {
        SpringApplication.run(DialogueApplication.class, args);
    }
}
