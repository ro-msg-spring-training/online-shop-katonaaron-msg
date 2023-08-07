package ro.msg.learning.shop.test;

import org.springframework.transaction.annotation.Transactional;

public interface IntegrationTestDBService {
    @Transactional
    void clearAndPopulateDB();
}
