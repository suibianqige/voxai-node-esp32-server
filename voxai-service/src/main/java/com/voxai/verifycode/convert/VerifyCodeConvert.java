package com.voxai.verifycode.convert;

import com.voxai.common.model.bo.VerifyCodeBO;
import com.voxai.verifycode.dal.mysql.dataobject.VerifyCodeDO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VerifyCodeConvert {

    VerifyCodeBO toBO(VerifyCodeDO verifyCodeDO);
}
