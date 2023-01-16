package pl.com.segware.gapa.service.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.segware.gapa.service.domain.item.Item;
import pl.com.segware.gapa.service.domain.item.ItemService;

import java.math.BigInteger;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    private ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addItem(@RequestBody Item item) {
        BigInteger itemId = itemService.addItem(item);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(itemId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = {"/", "/{id}"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getItems(@PathVariable Optional<BigInteger> id) {
        return ResponseEntity.ok(itemService.getItem(id));
    }

    @DeleteMapping(path = {"/", "/{id}"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteItem(@PathVariable BigInteger id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

}
