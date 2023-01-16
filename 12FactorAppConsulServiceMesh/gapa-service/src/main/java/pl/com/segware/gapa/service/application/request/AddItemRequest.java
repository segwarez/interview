package pl.com.segware.gapa.service.application.request;

public class AddItemRequest {
    @NotNull
    private Item item;

    @JsonCreator
    public AddItemRequest(@JsonProperty("item") @NotNull final Item product) {
        this.product = product;
    }

    public Item getProduct() {
        return product;
    }
}
