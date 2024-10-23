package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.dto.request.AddArticleRequest;
import com.estsoft.springproject.blog.service.BlogService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExternalApiController {
    private final BlogService service;

    @GetMapping("/api/external")
    public String callApi() {
        // 외부 API호출
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://jsonplaceholder.typicode.com/posts";

//        ResponseEntity<String> json = restTemplate.getForEntity("https://jsonplaceholder.typicode.com/posts",
//                String.class);
//
//        log.info("StatusCode: {} ", json.getStatusCode());
//        log.info(json.getBody());

        // 역 직렬화 json -> List<Content>
        ResponseEntity<List<Content>> resultList = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        log.info("code: {}", resultList.getStatusCode());
        log.info("{}", resultList.getBody());

        //resultList -> AddArticleRequest
        List<AddArticleRequest> addArticleRequests = resultList.getBody().stream()
                .map(content -> new AddArticleRequest(content.getTitle(), content.getBody()))
                .collect(Collectors.toList());

        // 1. stream forEach 사용
        //addArticleRequests.stream().map(addArticleRequest -> service.save(addArticleRequest));
        // 2. service.saveAll 구현
        service.saveAll(addArticleRequests);

        return "ok";
    }
}
