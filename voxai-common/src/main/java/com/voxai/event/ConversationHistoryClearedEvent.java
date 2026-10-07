package com.voxai.event;

import com.voxai.common.domain.AbstractDomainEvent;
import lombok.Getter;

/**
 * 对话历史清除事件，通知会话层通过 Redis 广播清除跨实例对话历史
 */
@Getter
public class ConversationHistoryClearedEvent extends AbstractDomainEvent {

    private final String deviceId;

    public ConversationHistoryClearedEvent(Object source, String deviceId) {
        super(source);
        this.deviceId = deviceId;
    }
}
