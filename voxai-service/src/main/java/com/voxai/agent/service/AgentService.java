package com.voxai.agent.service;

import com.voxai.common.model.bo.AgentBO;
import com.voxai.common.model.PageResult;

public interface AgentService {

    PageResult<AgentBO> page(int pageNo, int pageSize, String provider, String agentName, Integer userId);
}
