package com.voxai.communication.domain.iot;

import com.voxai.utils.JsonUtil;
import lombok.Data;

import java.util.Map;

/**
 * Iot设备描述信息
 */
@Data
public class IotDescriptor {
    private String name;
    private String description;
    private Map<String, IotProperty> properties;
    private Map<String, IotMethod> methods;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
