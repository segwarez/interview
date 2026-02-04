package com.segwarez.modularmonolith.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class ModularArchitectureTest {
    private static final String BASE_PACKAGE = "com.segwarez.modularmonolith";
    private static final JavaClasses CLASSES = new ClassFileImporter().importPackages(BASE_PACKAGE);
    private static final String[] MODULES = {"order", "billing", "warehouse", "delivery"};

    @Test
    void modulesShouldBeCycleFree() {
        slices().matching(BASE_PACKAGE + ".(*)..")
                .should().beFreeOfCycles()
                .check(CLASSES);
    }

    @Test
    void modulesShouldOnlyExposeApiPackageToOtherModules() {
        for (String module : MODULES) {
            String moduleBase = BASE_PACKAGE + "." + module;

            noClasses()
                    .that().resideOutsideOfPackage(moduleBase + "..")
                    .should().accessClassesThat().resideInAnyPackage(
                            moduleBase + ".application..",
                            moduleBase + ".domain..",
                            moduleBase + ".infrastructure.."
                    )
                    .check(CLASSES);
        }
    }
}