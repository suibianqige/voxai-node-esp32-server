package com.voxai.dialogue.adapter;

import com.voxai.ai.tool.session.ToolSession;
import com.voxai.ai.tool.session.ToolSessionProvider;
import com.voxai.communication.common.ChatSession;
import com.voxai.communication.common.SessionManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * dialogue 层的 ToolSessionProvider 实现。
 * 将 ai 层的会话查找请求委托给 dialogue 的 SessionManager，
 * 通过 ChatSessionToolAdapter 包装，隔离通信层细节。
 */
@Component
public class DialogueToolSessionProvider implements ToolSessionProvider {

    @Resource
    private SessionManager sessionManager;

    @Override
    public ToolSession getSession(String sessionId) {
        ChatSession session = sessionManager.getSession(sessionId);
        return session != null ? new ChatSessionToolAdapter(session) : null;
    }

    @Override
    public ToolSession getSessionByDeviceId(String deviceId) {
        ChatSession session = sessionManager.getSessionByDeviceId(deviceId);
        return session != null ? new ChatSessionToolAdapter(session) : null;
    }
}
