package com.segwarez.transactionaloutbox.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class CreatePostRequest {
    @NotBlank
    @Size(max = 150)
    private String title;
    @Size(max = 255)
    private String author;
    @NotBlank
    @Size(max = 1000)
    private String content;
}