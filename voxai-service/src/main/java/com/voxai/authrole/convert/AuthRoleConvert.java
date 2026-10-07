package com.voxai.authrole.convert;

import com.voxai.authrole.dal.mysql.dataobject.AuthRoleDO;
import com.voxai.common.model.bo.AuthRoleBO;
import com.voxai.common.model.resp.AuthRolePermissionConfigResp;
import com.voxai.common.model.resp.AuthRoleResp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthRoleConvert {

    AuthRoleBO toBO(AuthRoleDO authRoleDO);

    AuthRoleResp toResp(AuthRoleBO authRole);

    @Mapping(target = "permissionTree", ignore = true)
    @Mapping(target = "checkedPermissionIds", ignore = true)
    AuthRolePermissionConfigResp toPermissionConfigResp(AuthRoleBO authRole);
}
