package ro.msg.learning.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.service.IntegrationTestDBService;

@RestController
@Profile("integrationtest")
@AllArgsConstructor
@RequestMapping("/db")
public class TestController {
    private final IntegrationTestDBService service;

    @PostMapping("/clearAndPopulate")
    public void clearAndPopulateDB() {
        service.clearAndPopulateDB();
    }
}
