plugins {
	java
	id("org.springframework.boot") version "4.1.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "no.sigurof"
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
	implementation("org.springframework.boot:spring-boot-starter-restclient")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.0")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

// Dependency locking ensures we don't rely on dynamic dependency versions. The following
// lines enable dependency locking for all the gradle configurations (compileClassPath, productionClassPath etc)
// and ensure that the build fails if the build resolves to anything else than the dependency versions declared
// in `gradle.lockfile`.
dependencyLocking {
	lockAllConfigurations()
}

// Run `./gradlew resolveAndLockAll --write-locks` to regenerate
tasks.register("resolveAndLockAll") {
	notCompatibleWithConfigurationCache("Filters configurations at execution time")
	doFirst {
		require(gradle.startParameter.isWriteDependencyLocks) { "$path must be run from the command line with the `--write-locks` flag" }
	}
	doLast {
		configurations.filter {
			// Add any custom filtering on the configurations to be resolved
			it.isCanBeResolved
		}.forEach { it.resolve() }
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
