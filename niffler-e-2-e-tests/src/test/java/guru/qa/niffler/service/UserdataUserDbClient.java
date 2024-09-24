package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.user.UserdataUserEntity;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserDbClient {

    private final UserdataUserDao userDAO = new UserdataUserDaoJdbc();

    public UserdataUserEntity create(UserdataUserEntity user) {
        return userDAO.create(user);
    }

    public Optional<UserdataUserEntity> findById(UUID id) {
        return userDAO.findById(id);
    }

    public Optional<UserdataUserEntity> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public void delete(UserdataUserEntity user) {
        userDAO.delete(user);
    }
}