package com.voxai.role.service;

import com.voxai.common.model.bo.RoleBO;
import com.voxai.common.model.PageResult;
import com.voxai.role.model.RoleProjection;

import java.util.List;

public interface RoleService {

    // ===================== 查询操作 =====================

    PageResult<RoleProjection> page(int pageNo, int pageSize, Integer roleId, String roleName,
                                    String isDefault, String state, Integer userId);

    RoleBO getBO(Integer roleId);

    List<RoleBO> listBO(Integer userId, int limit);

    RoleBO getDefaultOrFirstBO(Integer userId);

    // ===================== 写操作（待迁移到 RoleAppService） =====================

    Integer copyDefaultRole(Integer sourceUserId, Integer targetUserId);
}
