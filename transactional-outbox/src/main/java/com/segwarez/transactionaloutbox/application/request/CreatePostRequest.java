package com.segwarez.transactionaloutbox.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.AllArgsConstructor;

@Value
@AllArgsConstructor
public class CreatePostRequest {
    @NotBlank
    @Size(max = 150)
    String title;
    @Size(max = 255)
    String author;
    @NotBlank
    @Size(max = 1000)
    String content;
}