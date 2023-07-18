import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Also update pom.xml

plugins {
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.1"
	kotlin("jvm") version "1.9.0"
	kotlin("plugin.spring") version "1.9.0"
}

group = "com.panopset"
version = "1.2.4"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
	implementation("org.springframework.session:spring-session-core")
	implementation("redis.clients:jedis:4.4.3")
	implementation("org.springframework.boot:spring-boot-configuration-processor")
	implementation("com.panopset:flywheel:1.2.4")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks {
	bootJar {
		archiveFileName.set("site.jar")
	}
}

