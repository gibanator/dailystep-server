package com.gibanator.dailystepbackendjava.asr;

import com.gibanator.dailystepbackendjava.asr.dto.AsrResponse;
import com.gibanator.dailystepbackendjava.asr.dto.TranscriptionResponse;
import com.gibanator.dailystepbackendjava.asr.exception.AsrException;
import com.gibanator.dailystepbackendjava.asr.exception.AsrUnavailableException;
import com.gibanator.dailystepbackendjava.asr.exception.InvalidAudioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SpeechRecognitionService {

    private final SpeechRecognitionClient client;

    public TranscriptionResponse transcribe(MultipartFile file) {
        try {
            AsrResponse response = client.transcribe(file);

            if (response == null || response.text() == null || response.text().isBlank()) {
                throw new AsrUnavailableException("Empty ASR response");
            }

            return new TranscriptionResponse(response.text());

        } catch (IOException e) {
            throw new InvalidAudioException("Failed to read uploaded file");

        } catch (ResourceAccessException e) {
            throw new AsrUnavailableException("Could not connect to ASR service");

        } catch (HttpClientErrorException e) {
            throw new InvalidAudioException("ASR rejected uploaded audio");

        } catch (HttpServerErrorException e) {
            throw new AsrUnavailableException("ASR service failed");

        } catch (RestClientResponseException e) {
            throw new AsrException("ASR service returned unexpected error");
        }
    }
}
