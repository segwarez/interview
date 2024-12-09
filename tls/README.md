# Transport Layer Security

## Steps:

1. Generate root certificate
```
openssl req -x509 -sha256 -days 3650 -newkey rsa:4096 -keyout rootCA.key -out rootCA.crt -passout pass:test123
```
2. Generate server private key and certificate signing request
```
openssl req -new -newkey rsa:4096 -keyout server.key -out server.csr -passout pass:test123
```
3. Prepare server.ext file
```
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
subjectAltName = @alt_names
[alt_names]
DNS.1 = api.segwarez.com
```
4. Sign CSR with root ca private key
```
openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in server.csr -out server.crt -days 365 -CAcreateserial -extfile server.ext -passin pass:test123
```
5. Export key and cert to P12 file
```
openssl pkcs12 -export -out server.p12 -name "api.segwarez.com" -inkey server.key -in server.crt -passin pass:test123 -password pass:test123
```
6. Convert P12 to JKS Keystore
```
keytool -importkeystore -srckeystore server.p12 -srcstoretype PKCS12 -destkeystore server_keystore.jks -deststoretype JKS
```
7. Create JKS Truststore
```
keytool -import -trustcacerts -noprompt -alias ca -ext san=dns:${SERVER_CN},ip:127.0.0.1 -file rootCA.crt -keystore server_truststore.jks
```
8. Generate client private key and certificate signing request
```
openssl req -new -newkey rsa:4096 -keyout client.key -out client.csr -passout pass:test123
```
9. Sign CSR with root ca private key
```
openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in client.csr -out client.crt -days 365 -CAcreateserial -passin pass:test123
```
10. Export key and cert to P12 file
```
openssl pkcs12 -export -out client.p12 -name "client" -inkey client.key -in client.crt -passin pass:test123 -password pass:test123
```

## Testing
```
curl -X GET https://api.segwarez.com:8443/payments/6238434a-4774-4e35-9a57-a928db3a3b1d -v
curl -X GET --cacert rootCA.crt https://api.segwarez.com:8443/payments/6238434a-4774-4e35-9a57-a928db3a3b1d -v
curl -X GET --cacert rootCA.crt --cert client.crt --key client.key --pass test123 https://api.segwarez.com:8443/payments/6238434a-4774-4e35-9a57-a928db3a3b1d -v
```