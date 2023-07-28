package ro.msg.learning.shop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("integrationtest")
public abstract class IntegrationTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @SneakyThrows
    protected String asJsonString(final Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

}
