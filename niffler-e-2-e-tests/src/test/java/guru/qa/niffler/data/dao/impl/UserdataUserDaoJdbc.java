package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.user.UserdataUserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserDaoJdbc implements UserdataUserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserdataUserEntity create(UserdataUserEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO users (username, currency, firstname, surname, fullname, photo, photo_small) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getCurrency().name());
                ps.setString(3, user.getFirstname());
                ps.setString(4, user.getSurname());
                ps.setString(5, user.getFullname());
                ps.setBytes(6, user.getPhoto());
                ps.setBytes(7, user.getPhotoSmall());

                ps.executeUpdate();

                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserdataUserEntity> findById(UUID id) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM users WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserdataUserEntity user = new UserdataUserEntity();
                        user.setId(rs.getObject("id", UUID.class));
                        user.setUsername(rs.getString("username"));
                        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        user.setFirstname(rs.getString("firstname"));
                        user.setSurname(rs.getString("surname"));
                        user.setFullname(rs.getString("fullname"));
                        user.setPhoto(rs.getBytes("photo"));
                        user.setPhotoSmall(rs.getBytes("photo_small"));
                        return Optional.of(user);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserdataUserEntity> findByUsername(String username) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            )) {
                ps.setString(1, username);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserdataUserEntity user = new UserdataUserEntity();
                        user.setId(rs.getObject("id", UUID.class));
                        user.setUsername(rs.getString("username"));
                        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        user.setFirstname(rs.getString("firstname"));
                        user.setSurname(rs.getString("surname"));
                        user.setFullname(rs.getString("fullname"));
                        user.setPhoto(rs.getBytes("photo"));
                        user.setPhotoSmall(rs.getBytes("photo_small"));
                        return Optional.of(user);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserdataUserEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM users WHERE id = ?"
            )) {
                ps.setObject(1, user.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}