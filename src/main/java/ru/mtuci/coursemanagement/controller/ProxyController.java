package ru.mtuci.coursemanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.mtuci.coursemanagement.service.UrlValidationService;

@RestController
public class ProxyController {
    private final UrlValidationService urlValidation;

    public ProxyController(UrlValidationService urlValidation) {
        this.urlValidation = urlValidation;
    }

    @GetMapping("/api/proxy")
    public String proxy(@RequestParam("targetUrl") String targetUrl) {
        var safeUri = urlValidation.validateHttpUrl(targetUrl);
        RestTemplate rt = new RestTemplate();
        return rt.getForObject(safeUri, String.class);
    }
}
