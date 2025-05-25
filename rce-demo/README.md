# Remote Code Execution Demo

## How To: Exploit vulnerability to perform RCE 

1. Add JVM flag
```
--add-opens java.base/sun.reflect.annotation=ALL-UNNAMED
```
2. Run application to generate malicious payload
3. Send the payload to the vulnerable endpoint
```
curl -X POST http://localhost:8080/vulnerable --data-binary @payload.bin -H 'Content-Type: application/octet-stream'
```