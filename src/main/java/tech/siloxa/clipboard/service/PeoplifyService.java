package tech.siloxa.clipboard.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tech.siloxa.clipboard.config.ApplicationProperties;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;

@Service
public class PeoplifyService {

    private static final String ACCEPT = "Accept";
    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_DATA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36";

    @Resource
    private ApplicationProperties applicationProperties;

    @Resource(name = "customRestTemplate")
    private RestTemplate restTemplate;

    @Resource
    private DocumentStoreService documentStoreService;

    public String randomAvatar() {
        final MultiValueMap<String, String> headers = constructHeaders();
        final HttpEntity<?> request = new HttpEntity<>(headers);
        final ResponseEntity<byte[]> response = restTemplate.exchange(
            applicationProperties.getPeoplify(),
            HttpMethod.GET,
            request,
            byte[].class
        );
        final String fileName = UUID.randomUUID() + ".png";
        return documentStoreService.storeDocument(fileName, Objects.requireNonNull(response.getBody()));
    }

    private MultiValueMap<String, String> constructHeaders() {
        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(USER_AGENT, USER_AGENT_DATA);
        headers.add(ACCEPT, MediaType.IMAGE_PNG_VALUE);
        return headers;
    }
}
