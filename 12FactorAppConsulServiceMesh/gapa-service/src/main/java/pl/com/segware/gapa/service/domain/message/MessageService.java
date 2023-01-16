package pl.com.segware.gapa.service.domain.message;

import java.math.BigInteger;
import java.util.List;

public interface MessageService {
    void sendMessage(Message message, BigInteger receipentId);
    List<Message> getMessagesForUser(BigInteger userId);
    void messageRead(BigInteger messageId);
    void deleteMessage(BigInteger messageId);
}
