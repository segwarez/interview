package com.segwarez.vertxrx.web.validation;

import com.segwarez.vertxrx.service.Genre;
import io.vertx.json.schema.Draft;
import io.vertx.json.schema.JsonSchemaOptions;
import io.vertx.json.schema.OutputFormat;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import io.vertx.rxjava3.ext.web.validation.RequestPredicate;
import io.vertx.rxjava3.ext.web.validation.ValidationHandler;
import io.vertx.rxjava3.ext.web.validation.builder.Bodies;
import io.vertx.rxjava3.ext.web.validation.builder.ParameterProcessorFactory;
import io.vertx.rxjava3.ext.web.validation.builder.Parameters;
import io.vertx.rxjava3.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.rxjava3.json.schema.SchemaRepository;

import java.util.Arrays;

import static io.vertx.json.schema.common.dsl.Keywords.maxLength;
import static io.vertx.json.schema.common.dsl.Keywords.minLength;
import static io.vertx.json.schema.common.dsl.Schemas.*;

public class BookValidationHandler {
    private final SchemaRepository schemaRepository = SchemaRepository.create(
        new JsonSchemaOptions()
            .setBaseUri("app://schemas/")
            .setDraft(Draft.DRAFT7)
            .setOutputFormat(OutputFormat.Basic)
    );

    public ValidationHandler getAllBooks() {
        return ValidationHandlerBuilder.create(schemaRepository)
            .queryParameter(buildTitleQueryParameter())
            .build();
    }

    public ValidationHandler getBookById() {
        return ValidationHandlerBuilder.create(schemaRepository)
            .pathParameter(buildIdPathParameter())
            .build();
    }

    public ValidationHandler createBook() {
        return ValidationHandlerBuilder.create(schemaRepository)
            .predicate(RequestPredicate.BODY_REQUIRED)
            .body(Bodies.json(buildCreateBodySchemaBuilder()))
            .build();
    }

    public ValidationHandler updateBook() {
        return ValidationHandlerBuilder.create(schemaRepository)
            .predicate(RequestPredicate.BODY_REQUIRED)
            .body(Bodies.json(buildUpdateBodySchemaBuilder()))
            .pathParameter(buildIdPathParameter())
            .build();
    }

    public ValidationHandler deleteBook() {
        return ValidationHandlerBuilder.create(schemaRepository)
            .pathParameter(buildIdPathParameter())
            .build();
    }

    private ObjectSchemaBuilder buildCreateBodySchemaBuilder() {
        return objectSchema()
            .requiredProperty("title", stringSchema().with(minLength(1)).with(maxLength(255)))
            .requiredProperty("author", stringSchema().with(minLength(1)).with(maxLength(255)))
            .requiredProperty("genre", enumSchema(Arrays.stream(Genre.values()).map(Enum::toString).toArray()))
            .requiredProperty("description", stringSchema().with(maxLength(1000)).nullable());
    }

    private ObjectSchemaBuilder buildUpdateBodySchemaBuilder() {
        return objectSchema()
            .requiredProperty("title", stringSchema().with(minLength(1)).with(maxLength(255)))
            .requiredProperty("author", stringSchema().with(minLength(1)).with(maxLength(255)))
            .requiredProperty("genre", enumSchema(Arrays.stream(Genre.values()).map(Enum::toString).toArray()))
            .requiredProperty("description", stringSchema().with(minLength(1)).with(maxLength(1000)).nullable())
            .requiredProperty("published", booleanSchema());
    }

    private ParameterProcessorFactory buildIdPathParameter() {
        return Parameters.param("id", stringSchema());
    }

    private ParameterProcessorFactory buildTitleQueryParameter() {
        return Parameters.optionalParam("title", stringSchema());
    }
}
