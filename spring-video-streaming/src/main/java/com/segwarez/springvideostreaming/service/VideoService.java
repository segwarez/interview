package com.segwarez.springvideostreaming.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VideoService {
    private static final String PLAYLIST_PATH_FORMAT = "video/%s.m3u8";
    private static final String VIDEO_PATH_FORMAT = "video/%s.ts";

    public Mono<ResponseEntity<String>> getHlsPlaylist(String videoId) {
        var playlistPath = Paths.get(String.format(PLAYLIST_PATH_FORMAT, videoId));

        return Mono.fromCallable(() -> {
            if (Files.exists(playlistPath)) {
                var playlistContent = Files.readString(playlistPath);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "application/vnd.apple.mpegurl")
                        .body(playlistContent);
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    private byte[] readVideoFile(String videoId) throws IOException {
        var videoPath = Paths.get(String.format(VIDEO_PATH_FORMAT, videoId));
        return Files.readAllBytes(videoPath);
    }

    public Mono<ResponseEntity<DataBuffer>> streamVideo(String videoId, Optional<String> range) {
        var videoPath = Paths.get(String.format(VIDEO_PATH_FORMAT, videoId));
        if (!Files.exists(videoPath)) return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        return Mono.fromCallable(() -> {
            try {
                var videoBytes = readVideoFile(videoId);
                if (range.isPresent()) {
                    return streamRange(videoBytes, range.get());
                }
                var dataBuffer = new DefaultDataBufferFactory().wrap(videoBytes);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "video/mp2t")
                        .body(dataBuffer);
            } catch (IOException e) {
                throw new RuntimeException("Error reading video file", e);
            }
        });
    }

    private ResponseEntity<DataBuffer> streamRange(byte[] videoBytes, String range) {
        var rangeParts = range.replace("bytes=", "").split("-");
        var startByte = Long.parseLong(rangeParts[0]);
        var endByte = rangeParts.length > 1 ? Long.parseLong(rangeParts[1]) : -1;

        var fileLength = videoBytes.length;
        if (endByte == -1 || endByte >= fileLength) {
            endByte = fileLength - 1;
        }

        var videoPart = new byte[(int) (endByte - startByte + 1)];
        System.arraycopy(videoBytes, (int) startByte, videoPart, 0, videoPart.length);

        var dataBuffer = new DefaultDataBufferFactory().wrap(videoPart);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, "video/mp2t")
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + startByte + "-" + endByte + "/" + videoBytes.length)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(videoPart.length))
                .body(dataBuffer);
    }
}
