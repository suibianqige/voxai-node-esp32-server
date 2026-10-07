package com.voxai.communication.domain.mcp.device.initialize;

import lombok.Data;

@Data
public class DeviceMcpClientInfo {
    private String name = "voxai-mqtt-client";
    private String version = "1.0.0";
}