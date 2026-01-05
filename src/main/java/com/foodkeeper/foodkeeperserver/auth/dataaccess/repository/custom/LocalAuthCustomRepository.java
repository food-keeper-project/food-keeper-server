package com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.custom;

public interface LocalAuthCustomRepository {

    boolean existsByAccount(String account);
}
