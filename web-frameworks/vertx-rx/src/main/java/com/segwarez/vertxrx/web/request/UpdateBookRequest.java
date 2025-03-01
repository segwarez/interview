package com.segwarez.vertxrx.web.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateBookRequest {
    private String title;
    private String author;
    private String genre;
    private String description;
    private boolean isPublished;
}
