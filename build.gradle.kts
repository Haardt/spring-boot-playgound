plugins {
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_20
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:3.1.2"))

        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-validation")

        implementation("org.springframework.data:spring-data-jpa")
        // Sql-Lite deps
        implementation("org.xerial:sqlite-jdbc:3.24.0.0")
        implementation("org.hibernate.orm:hibernate-community-dialects:6.2.7.Final")

        compileOnly("org.projectlombok:lombok:1.18.28")
        annotationProcessor("org.projectlombok:lombok:1.18.28")

        testCompileOnly("org.projectlombok:lombok:1.18.28")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.28")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")

        testImplementation("com.epages:restdocs-api-spec:0.18.2") {
            exclude("org.springframework.boot")
        }
        testImplementation("com.epages:restdocs-api-spec-webtestclient:0.18.2")  {
            exclude("org.springframework.boot")
        }

        testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
        testImplementation("io.kotest:kotest-assertions-core:5.5.5")

    }
}

tasks.withType<JavaCompile> {
    val compilerArgs = options.compilerArgs
    compilerArgs.add("--enable-preview")
    compilerArgs.add("-Xlint:preview")
}


//tasks.withType<JavaExec> {
//    jvmArgs?.add("--enable-preview")
//}

application {
    mainClass.set("com.sft.Impossible")
}
