package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();


    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findSpendById(id);
                },
                CFG.spendJdbcUrl()
        );
    }

    public List<SpendEntity> findAllSpendsByUsername(String username) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findAllByUsername(username);
                },
                CFG.spendJdbcUrl()
        );
    }

    public void deleteSpend(SpendEntity spend) {
        transaction(connection -> {
                    new SpendDaoJdbc(connection).deleteSpend(spend);
                },
                CFG.spendJdbcUrl()
        );
    }

    public void deleteSpendJson(SpendJson spendJson) {
        transaction(connection -> {
                    // Проверяем, существует ли SpendEntity с данным ID
                    Optional<SpendEntity> spendEntity = new SpendDaoJdbc(connection).findSpendById(spendJson.id());

                    // Если запись найдена, удаляем её
                    spendEntity.ifPresent(spend -> new SpendDaoJdbc(connection).deleteSpend(spend));

                },
                CFG.spendJdbcUrl()
        );
    }
}
