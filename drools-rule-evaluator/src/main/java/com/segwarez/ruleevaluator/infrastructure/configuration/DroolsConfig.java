package com.segwarez.ruleevaluator.infrastructure.configuration;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {
    private static final String RULES_PATH = "rules/order.drl";

    @Bean
    public KieSession getKieSession() {
        return getKieContainer().newKieSession();
    }

    public KieContainer getKieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem(kieServices));
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    private KieFileSystem getKieFileSystem(KieServices kieServices) {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH));
        return kieFileSystem;
    }
}
