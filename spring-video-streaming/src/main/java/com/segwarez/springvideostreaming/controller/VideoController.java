package com.segwarez.springvideostreaming.controller;

import com.segwarez.springvideostreaming.service.VideoService;
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
    private final VideoService videoService;

    @GetMapping("/{videoId}")
    public Mono<ResponseEntity<DataBuffer>> streamVideo(
            @PathVariable String videoId,
            @RequestHeader(value = HttpHeaders.RANGE, required = false) Optional<String> range) {
        return videoService.streamVideo(videoId, range);
    }

    @GetMapping("/{videoId}/playlist")
    public Mono<ResponseEntity<String>> getHlsPlaylist(String videoId) {
        return videoService.getHlsPlaylist(videoId);
    }
}
