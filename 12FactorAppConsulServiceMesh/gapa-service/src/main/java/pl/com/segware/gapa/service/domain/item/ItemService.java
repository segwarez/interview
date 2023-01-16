package pl.com.segware.gapa.service.domain.item;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    BigInteger addItem(Item item);
    List<Item> getItem(Optional<BigInteger> id);
    void updateItem(Item item);
    void deleteItem(BigInteger id);

}
