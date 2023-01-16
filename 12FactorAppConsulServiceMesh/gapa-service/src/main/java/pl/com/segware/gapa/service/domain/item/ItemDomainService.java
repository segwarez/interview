package pl.com.segware.gapa.service.domain.item;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public class ItemDomainService implements ItemService {
    private ItemRepository itemRepository;

    public ItemDomainService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    @Override
    public BigInteger addItem(Item item) {
        return null;
    }

    @Override
    public List<Item> getItem(Optional<BigInteger> id) {
        if (id.isPresent()) {
            return itemRepository.findById(id.get()).get());
        } else {
            return itemRepository.findAll();
        }
    }

    @Override
    public void updateItem(Item item) {

    }

    @Override
    public void deleteItem(BigInteger id) {
        itemRepository.delete(id);
    }
}
