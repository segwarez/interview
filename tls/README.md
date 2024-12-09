# Transport Layer Security

```
curl -X GET https://api.segwarez.com:8443 -v
curl -X GET --cacert rootCA.crt https://api.segwarez.com:8443 -v
curl -X GET --cacert rootCA.crt --cert client.crt --key client.key --pass test123 https://api.segwarez.com:8443 -v
```