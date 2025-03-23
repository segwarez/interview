package com.segwarez.quarkusweb.application.rest;

import com.segwarez.quarkusweb.application.request.CreateBookRequest;
import com.segwarez.quarkusweb.application.request.UpdateBookRequest;
import com.segwarez.quarkusweb.domain.service.BookService;
import com.segwarez.quarkusweb.infrastructure.configuration.Pagination;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/books")
public class BookResource {
    @Inject
    private BookService bookService;

    @Inject
    private Pagination pagination;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks(
            @QueryParam("title") Optional<String> title,
            @QueryParam("page") @DefaultValue("0") int pageNumber,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("sort") List<String> sortQuery) {
        pagination.of(pageNumber, pageSize, sortQuery);
        if (title.isEmpty()) return Response.ok(bookService.findAll()).build();
        return Response.ok(bookService.findByTitleContaining(title.get())).build();
    }

    @GET
    @Path("/published")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPublishedBooks(
            @QueryParam("page") @DefaultValue("0") int pageNumber,
            @QueryParam("size") @DefaultValue("20") int pageSize,
            @QueryParam("sort") List<String> sortQuery
    ) {
        pagination.of(pageNumber, pageSize, sortQuery);
        return Response.ok(bookService.findByPublished(true)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") UUID id) {
        var optionalBook = bookService.findById(id);
        if (optionalBook.isEmpty()) return Response.status(NOT_FOUND).build();
        return Response.ok(optionalBook.get()).build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBook(@Valid CreateBookRequest createBookRequest, @Context UriInfo uriInfo) {
        var id = bookService.create(
                createBookRequest.title(),
                createBookRequest.author(),
                createBookRequest.genre(),
                createBookRequest.description());
        return Response.created(URI.create(uriInfo.getBaseUri().toString() + "books/" + id)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") UUID id, @Valid UpdateBookRequest request) {
        var optionalBook = bookService.update(
                id,
                request.title(),
                request.author(),
                request.genre(),
                request.description(),
                request.published());
        if (optionalBook.isEmpty()) return Response.status(NOT_FOUND).build();
        return Response.ok(optionalBook.get()).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") UUID id) {
        bookService.deleteById(id);
        return Response.noContent().build();
    }

    @DELETE
    @Transactional
    public Response deleteAllBooks() {
        bookService.deleteAll();
        return Response.noContent().build();
    }
}
