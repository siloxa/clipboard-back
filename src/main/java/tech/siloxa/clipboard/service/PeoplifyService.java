package tech.siloxa.clipboard.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tech.siloxa.clipboard.config.ApplicationProperties;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class PeoplifyService {

    private static final String ACCEPT = "Accept";
    public static final String IMAGE_SVG_XML = "image/svg+xml";

    @Resource
    private ApplicationProperties applicationProperties;

    @Resource(name = "customRestTemplate")
    private RestTemplate restTemplate;

    @Resource
    private DocumentStoreService documentStoreService;

    public String randomAvatar() {
        final MultiValueMap<String, String> headers = constructHeaders();
        final HttpEntity<?> request = new HttpEntity<>(headers);
        final ResponseEntity<String> response = restTemplate.exchange(
            applicationProperties.getPeoplify(),
            HttpMethod.GET,
            request,
            String.class
        );
        final String fileName = UUID.randomUUID() + ".svg";
        return documentStoreService.storeDocument(fileName, Objects.requireNonNull(response.getBody()).getBytes());
    }

    private MultiValueMap<String, String> constructHeaders() {
        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(ACCEPT, IMAGE_SVG_XML);
        return headers;
    }
}
