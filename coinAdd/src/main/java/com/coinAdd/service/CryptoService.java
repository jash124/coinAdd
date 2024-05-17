package com.coinAdd.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.coinAdd.entity.ApiRequestResponse;
import com.coinAdd.repository.ApiRequestResponseRepository;

@Service
public class CryptoService {

    @Autowired
    private ApiRequestResponseRepository repository;

    private static final String API_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?symbol=BTC,ETH,LTC";
    private static final String API_KEY = "27ab17d1-215f-49e5-9ca4-afd48810c149";

    public ApiRequestResponse getCryptoQuotes(Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

        ApiRequestResponse apiRequestResponse = new ApiRequestResponse();
        apiRequestResponse.setUserId(userId);
        apiRequestResponse.setRequestUrl(API_URL);
        apiRequestResponse.setResponse(response.getBody());
        apiRequestResponse.setTimestamp(LocalDateTime.now());

        repository.save(apiRequestResponse);

        return apiRequestResponse;
    }
}