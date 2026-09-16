plugins {
    `java-library`
}

group = "dev.igoyek"
version = "1.0.0-SNAPSHOT"

tasks.compileJava {
    options.compilerArgs = listOf("-Xlint:deprecation", "-parameters")
    options.encoding = "UTF-8"
    options.release = 25
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}