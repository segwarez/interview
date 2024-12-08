package com.segwarez.vertxrs.web.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBookRequest {
    private String title;
    private String author;
    private String genre;
    private String description;
}
