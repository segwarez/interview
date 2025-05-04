package com.segwarez.springvideostreaming.controller;

import com.segwarez.springvideostreaming.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoRepository videoRepository;

    @GetMapping("/{videoId}/{segment:.+\\.ts}")
    public Mono<ResponseEntity<DataBuffer>> streamVideo(
            @PathVariable String videoId,
            @PathVariable String segment,
            @RequestHeader(value = HttpHeaders.RANGE, required = false) Optional<String> range) {
        return videoRepository.streamVideo(videoId, segment, range);
    }

    @GetMapping("/{videoId}/playlist")
    public Mono<ResponseEntity<String>> getHlsPlaylist(@PathVariable String videoId) {
        return videoRepository.getHlsPlaylist(videoId);
    }
}
