plugins {
    id("java")
}

group = "com.github.N1ckBaran0v.library"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-beans:6.2.2")
    implementation("org.springframework:spring-core:6.2.2")
    implementation("org.springframework:spring-aspects:6.2.2")
    implementation("org.springframework:spring-webmvc:6.2.2")
    implementation("org.springframework.data:spring-data-jpa:3.4.2")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("org.eclipse.jetty:jetty-server:11.0.24")
    implementation("org.eclipse.jetty:jetty-webapp:11.0.24")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.hibernate:hibernate-core:6.6.5.Final")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.6.5.Final")
    implementation("org.xerial:sqlite-jdbc:3.48.0.0")
    implementation("org.jetbrains:annotations:26.0.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.15.2")
}

tasks.test {
    useJUnitPlatform()
}