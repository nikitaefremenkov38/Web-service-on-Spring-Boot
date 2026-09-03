package ru.mtuci.coursemanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class UrlValidationService {
    private final Set<String> allowedHosts;

    public UrlValidationService(@Value("${app.allowedImportHosts:localhost}") String hosts) {
        this.allowedHosts = Arrays.stream(hosts.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    public URI validateHttpUrl(String raw) {
        try {
            URI uri = new URI(raw);
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
                throw new ResponseStatusException(BAD_REQUEST, "Недопустимый протокол URL");
            }
            String host = uri.getHost();
            if (host == null || !allowedHosts.contains(host)) {
                throw new ResponseStatusException(BAD_REQUEST, "Недопустимый хост URL");
            }
            return uri;
        } catch (URISyntaxException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Некорректный URL");
        }
    }
}
