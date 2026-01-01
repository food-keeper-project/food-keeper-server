package com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.custom;

public interface FcmTokenCustomRepository {

    void deleteFcmTokens(String memberKey);
}
