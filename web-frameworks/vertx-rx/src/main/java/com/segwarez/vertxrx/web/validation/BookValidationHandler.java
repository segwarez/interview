package com.segwarez.vertxrx.web.validation;

import com.segwarez.vertxrx.service.Genre;
import io.vertx.json.schema.SchemaRouterOptions;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import io.vertx.rxjava3.ext.web.validation.ValidationHandler;
import io.vertx.rxjava3.ext.web.validation.RequestPredicate;
import io.vertx.rxjava3.ext.web.validation.builder.Bodies;
import io.vertx.rxjava3.ext.web.validation.builder.ParameterProcessorFactory;
import io.vertx.rxjava3.ext.web.validation.builder.Parameters;
import io.vertx.rxjava3.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.rxjava3.json.schema.SchemaParser;
import io.vertx.rxjava3.json.schema.SchemaRouter;
import io.vertx.rxjava3.core.Vertx;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static io.vertx.json.schema.common.dsl.Keywords.maxLength;
import static io.vertx.json.schema.common.dsl.Keywords.minLength;
import static io.vertx.json.schema.common.dsl.Schemas.*;

@RequiredArgsConstructor
public class BookValidationHandler {
    private final Vertx vertx;

    public ValidationHandler getAllBooks() {
        final SchemaParser schemaParser = buildSchemaParser();
        return ValidationHandlerBuilder.create(schemaParser).queryParameter(buildTitlePathParameter()).build();
    }

    public ValidationHandler getBookById() {
        final SchemaParser schemaParser = buildSchemaParser();
        return ValidationHandlerBuilder.create(schemaParser).pathParameter(buildIdPathParameter()).build();
    }

    public ValidationHandler createBook() {
        final SchemaParser schemaParser = buildSchemaParser();
        final ObjectSchemaBuilder schemaBuilder = buildCreateBodySchemaBuilder();
        return ValidationHandlerBuilder.create(schemaParser).predicate(RequestPredicate.BODY_REQUIRED).body(Bodies.json(schemaBuilder)).build();
    }

    public ValidationHandler updateBook() {
        final SchemaParser schemaParser = buildSchemaParser();
        final ObjectSchemaBuilder schemaBuilder = buildUpdateBodySchemaBuilder();

        return ValidationHandlerBuilder.create(schemaParser).predicate(RequestPredicate.BODY_REQUIRED).body(Bodies.json(schemaBuilder)).pathParameter(buildIdPathParameter()).build();
    }

    public ValidationHandler deleteBook() {
        final SchemaParser schemaParser = buildSchemaParser();
        return ValidationHandlerBuilder.create(schemaParser).pathParameter(buildIdPathParameter()).build();
    }

    private SchemaParser buildSchemaParser() {
        return SchemaParser.createDraft7SchemaParser(SchemaRouter.create(vertx, new SchemaRouterOptions()));
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

    private ParameterProcessorFactory buildTitlePathParameter() {
        return Parameters.optionalParam("title", stringSchema());
    }
}
