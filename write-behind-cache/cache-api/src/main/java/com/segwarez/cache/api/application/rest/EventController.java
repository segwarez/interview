package com.segwarez.cache.api.application.rest;

import com.segwarez.cache.api.application.request.CreateEventRequest;
import com.segwarez.cache.api.domain.Event;
import com.segwarez.cache.api.infrastructure.repository.EventDao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventDao eventDao;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Valid @RequestBody CreateEventRequest request) {
        var event = new Event(request.getType(), request.getUserId(), request.getTimestamp(), request.getPayload());
        eventDao.save(event);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(event.getId()).toUri()).build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> getEventById(@PathVariable("id") UUID eventId) {
        var optionalEvent = eventDao.get(eventId);
        return optionalEvent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID eventId) {
        eventDao.delete(eventId);
    }
}