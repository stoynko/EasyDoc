package com.github.stoynko.easydoc.report.client;

import com.github.stoynko.easydoc.report.web.dto.response.Icd10SearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "diagnosis-client", url = "https://clinicaltables.nlm.nih.gov")
public interface DiagnosisClient {

    @GetMapping("/api/icd10cm/v3/search")
    Icd10SearchResponse searchIcd10(@RequestParam("sf") String searchFields,
                                    @RequestParam("terms") String terms,
                                    @RequestParam("maxList") int maxCount

    );
}