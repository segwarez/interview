package pl.com.segware.gapa.service.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.segware.gapa.service.domain.item.ItemDomainService;
import pl.com.segware.gapa.service.domain.item.ItemRepository;
import pl.com.segware.gapa.service.domain.item.ItemService;

@Configuration
public class BeanConfiguration {
    @Bean
    ItemService getItemService(ItemRepository itemRepository) {
        return new ItemDomainService(itemRepository);
    }
}
