package com.voxai.userauth.convert;

import com.voxai.common.model.bo.UserAuthBO;
import com.voxai.userauth.dal.mysql.dataobject.UserAuthDO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAuthConvert {

    UserAuthBO toBO(UserAuthDO d);

    UserAuthDO toDO(UserAuthBO bo);
}
