package com.github.stoynko.easydoc.report.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public record Icd10SearchResponse(
        int totalDiagnosis,
        List<String> codes,
        String extraFields,
        List<List<String>> rows
) { }
