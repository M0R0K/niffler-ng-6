package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.user.UserdataUserEntity;

import java.util.Optional;
import java.util.UUID;

import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;

import static guru.qa.niffler.data.Databases.transaction;

public class UserdataUserDbClient {

    private static final Config CFG = Config.getInstance();

    public UserdataUserEntity create(UserdataUserEntity user) {
        return transaction(connection -> {
            return new UserdataUserDaoJdbc(connection).create(user);
        }, CFG.userdataJdbcUrl());
    }

    public Optional<UserdataUserEntity> findById(UUID id) {
        return transaction(connection -> {
            return new UserdataUserDaoJdbc(connection).findById(id);
        }, CFG.userdataJdbcUrl());
    }

    public Optional<UserdataUserEntity> findByUsername(String username) {
        return transaction(connection -> {
            return new UserdataUserDaoJdbc(connection).findByUsername(username);
        }, CFG.userdataJdbcUrl());
    }

    public void delete(UserdataUserEntity user) {
        transaction(connection -> {
            new UserdataUserDaoJdbc(connection).delete(user);
        }, CFG.userdataJdbcUrl());
    }
}