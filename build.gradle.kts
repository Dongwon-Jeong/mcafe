import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    id("jacoco")

    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
    kotlin("kapt") version "1.8.22"
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

group = "com.midasit"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
repositories {
    mavenCentral()
    maven { setUrl("https://nexus.devops.midasit.com/repository/maven-public/") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("javax.xml.bind:jaxb-api:2.3.1")

    implementation(group = "com.querydsl", name = "querydsl-jpa", classifier = "jakarta")
    kapt(group = "com.querydsl", name = "querydsl-apt", classifier = "jakarta")


    testImplementation("io.mockk:mockk:1.12.7")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.4.2")
    testImplementation("io.kotest:kotest-assertions-core:5.4.2")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("com.h2database:h2:2.1.214")
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    finalizedBy(tasks.jacocoTestCoverageVerification)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = "CLASS"
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
            excludes = listOf(
                "**.Q*",
                "com.midasit.mcafe.McafeV2Application*",
                "com.midasit.mcafe.domain.test.TestController",
                "com.midasit.mcafe.domain.*.dto.*",
                "com.midasit.mcafe.infra.config.*",
                "com.midasit.mcafe.infra.converter.*",
                "com.midasit.mcafe.infra.exception.*",
                "com.midasit.mcafe.model.*",
                "com.midasit.mcafe.infra.component.*",
                // need to remove

                "com.midasit.mcafe.domain.member.Member",
                "com.midasit.mcafe.domain.payment.Payment",
                "com.midasit.mcafe.domain.recentorder.RecentOrder",
                "com.midasit.mcafe.domain.looseHistory.*",
                "com.midasit.mcafe.domain.gamedata.*"
            )
        }
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}