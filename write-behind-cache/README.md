# Write behind cache with Redis and Postgres

```
curl -X POST http://localhost:8080/events \
     -H "Content-Type: application/json" \
     -d '{
        "type": "USER_LOGIN",
        "userId": "user-123",
        "timestamp": "2023-10-27T10:00:00Z",
        "payload": "{\"browser\": \"Chrome\", \"ip\": \"127.0.0.1\"}"
     }' -v
```