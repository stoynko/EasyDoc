package com.github.stoynko.easydoc.client;

import com.github.stoynko.easydoc.web.dto.response.Icd10SearchResult;
import io.netty.handler.codec.http.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Icd10Client {

   private final RestTemplate restTemplate;

   public static final String CLINIC_TABLES_BASE_URL = "https://clinicaltables.nlm.nih.gov/api/icd10cm/v3/search";

   public Icd10SearchResult search(String term, int max) {
       String uriString = UriComponentsBuilder.fromUriString(CLINIC_TABLES_BASE_URL)
               .queryParam("sf", "code,name")
               .queryParam("terms", term)
               .toUriString();

       return null;
   }
}
