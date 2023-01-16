package pl.com.segware.gapa.service.domain.message;

import java.math.BigInteger;

public class Message {
    private BigInteger id;
    private BigInteger authorId;
    private String content;
    private MessageStatus status;
}
