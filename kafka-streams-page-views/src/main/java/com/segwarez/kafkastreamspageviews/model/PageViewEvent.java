package com.segwarez.kafkastreamspageviews.model;

import lombok.Data;

@Data
public class PageViewEvent {
    private String pageId;
    private String userId;
}
