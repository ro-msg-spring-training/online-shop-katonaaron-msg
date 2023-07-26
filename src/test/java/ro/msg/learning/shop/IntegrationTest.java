package ro.msg.learning.shop;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)
@ActiveProfiles("integrationtest")
public abstract class IntegrationTest {

    @Autowired
    private Flyway flyway;

    @Before
    public void cleanDB() {
        flyway.clean();
        flyway.migrate();
    }
}
