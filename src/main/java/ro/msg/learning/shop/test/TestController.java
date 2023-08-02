package ro.msg.learning.shop.test;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("integrationtest")
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final IntegrationTestDBService service;

    @PostMapping("/clearAndPopulate")
    public void clearAndPopulateDB() {
        service.clearAndPopulateDB();
    }
}
