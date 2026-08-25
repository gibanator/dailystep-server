package com.gibanator.dailystepbackendjava.asr;

import com.gibanator.dailystepbackendjava.asr.dto.TranscriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/audio")
@RequiredArgsConstructor
public class AudioController {

    private final SpeechRecognitionService speechRecognitionService;
    
    @PostMapping("/transcribe")
    public ResponseEntity<TranscriptionResponse> transcribe(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(speechRecognitionService.transcribe(file));
    }
}
