plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.github.N1ckBaran0v"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jetty")
	modules {
		module("org.springframework.boot:spring-boot-starter-tomcat") {
			replacedBy("org.springframework.boot:spring-boot-starter-jetty", "Use Jetty instead of Tomcat")
		}
	}
	implementation("org.hibernate.orm:hibernate-community-dialects")
	implementation("org.xerial:sqlite-jdbc:3.48.0.0")
	implementation("org.jetbrains:annotations:26.0.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
