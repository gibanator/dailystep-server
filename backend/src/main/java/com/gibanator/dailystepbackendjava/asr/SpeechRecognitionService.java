package com.gibanator.dailystepbackendjava.asr;

import com.gibanator.dailystepbackendjava.asr.dto.AsrResponse;
import com.gibanator.dailystepbackendjava.asr.dto.TranscriptionResponse;
import com.gibanator.dailystepbackendjava.asr.exception.AsrException;
import com.gibanator.dailystepbackendjava.asr.exception.AsrUnavailableException;
import com.gibanator.dailystepbackendjava.asr.exception.InvalidAudioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j 
@Service
@RequiredArgsConstructor
public class SpeechRecognitionService {

    private final SpeechRecognitionClient client;

    public TranscriptionResponse transcribe(MultipartFile file) {
        try {
            AsrResponse response = client.transcribe(file);

            log.info("ASR response: {}", response);

            if (response == null || response.text() == null || response.text().isBlank()) {
                throw new AsrUnavailableException("Empty ASR response");
            }

            return new TranscriptionResponse(response.text());

        } catch (IOException e) {
            throw new InvalidAudioException("Failed to read uploaded file");

        } catch (ResourceAccessException e) {
            throw new AsrUnavailableException("Could not connect to ASR service");

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AsrUnavailableException("ASR authentication failed");
            }

            if (e.getStatusCode() == HttpStatus.PAYLOAD_TOO_LARGE) {
                throw new InvalidAudioException("Uploaded audio is too large");
            }

            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                throw new InvalidAudioException("ASR could not process uploaded audio");
            }

            throw new AsrException(
                    "ASR returned client error: " + e.getStatusCode()
            );

        } catch (HttpServerErrorException e) {
            throw new AsrUnavailableException("ASR service failed");

        } catch (RestClientResponseException e) {
            throw new AsrException(
                    "ASR service returned unexpected error: " + e.getStatusCode()
            );
        }
    }
}