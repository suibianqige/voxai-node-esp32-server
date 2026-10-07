package com.voxai.permission.convert;

import com.voxai.common.model.bo.PermissionBO;
import com.voxai.common.model.resp.PermissionTreeResp;
import com.voxai.permission.dal.mysql.dataobject.PermissionDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionConvert {

    @Mapping(target = "children", ignore = true)
    PermissionBO toBO(PermissionDO permissionDO);

    PermissionTreeResp toTreeResp(PermissionBO permission);
}
