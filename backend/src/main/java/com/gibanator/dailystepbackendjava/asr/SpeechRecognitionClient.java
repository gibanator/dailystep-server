package com.gibanator.dailystepbackendjava.asr;

import com.gibanator.dailystepbackendjava.asr.dto.AsrResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class SpeechRecognitionClient {

    private final RestClient restClient;

    public SpeechRecognitionClient(@Value("${asr.url}") String asrUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(asrUrl)
                .build();
    }

    public AsrResponse transcribe(MultipartFile audio) throws IOException {
        Resource resource = audio.getResource();
        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(resolveContentType(audio));
        fileHeaders.setContentDispositionFormData("file", audio.getOriginalFilename());

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new HttpEntity<>(resource, fileHeaders));

        return restClient.post()
                .uri("/transcribe")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(AsrResponse.class);

    }

    private MediaType resolveContentType(MultipartFile audio) {
        String contentType = audio.getContentType();
        if (contentType == null || contentType.isBlank()) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        return MediaType.parseMediaType(contentType);
    }
}
