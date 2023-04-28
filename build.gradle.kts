plugins {
	java
	id("org.springframework.boot") version "3.0.6"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.xm.crypto"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.opencsv:opencsv:${project.property("opencsv.version")}")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.property("springdoc.version")}")
	implementation("com.github.vladimir-bukhtoyarov:bucket4j-core:${project.property("bucket4j.version")}")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
