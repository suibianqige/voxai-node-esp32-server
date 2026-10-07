package com.voxai.userauth.service;

import com.voxai.common.model.bo.UserAuthBO;

public interface UserAuthService {

    UserAuthBO getByOpenIdAndPlatform(String openId, String platform);

    UserAuthBO getByUserIdAndPlatform(Integer userId, String platform);

    UserAuthBO create(UserAuthBO userAuth);

    void update(UserAuthBO userAuth);

    void deleteById(Long id);
}
