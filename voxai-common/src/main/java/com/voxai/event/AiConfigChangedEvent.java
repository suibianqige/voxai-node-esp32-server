package com.voxai.event;

import com.voxai.common.domain.AbstractDomainEvent;
import lombok.Getter;

/**
 * AI 模型配置变更事件（更新或删除）。
 * 由 ConfigRepositoryImpl.save() / delete() 发布，触发 STT/TTS/Token 缓存失效广播。
 */
@Getter
public class AiConfigChangedEvent extends AbstractDomainEvent {

    private final String configType;
    private final Integer configId;

    public AiConfigChangedEvent(Object source, String configType, Integer configId) {
        super(source);
        this.configType = configType;
        this.configId = configId;
    }
}
