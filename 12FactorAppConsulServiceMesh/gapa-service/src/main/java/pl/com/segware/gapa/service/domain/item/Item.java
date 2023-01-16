package pl.com.segware.gapa.service.domain.item;

import pl.com.segware.gapa.service.domain.user.User;

import java.math.BigInteger;
import java.util.List;

public class Item {
    private BigInteger id;
    private User author;
    private ItemStatus status;
    private String name;
    private String description;
    private Location location;
    private List<String> imageUrls;
}
