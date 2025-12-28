plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
  id("xyz.jpenilla.run-paper") version "2.3.1"
  id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "codes.Elix"
version = "1.0"
description = "Gamemode for Minecraft"


java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {

  paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
  compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.9.2")
  compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:2.9.2") { isTransitive = false }
  implementation("fr.mrmicky:fastboard:2.1.5") // Modern scoreboard library
  implementation("org.mongodb:mongodb-driver-reactivestreams:4.8.1") // Database
  implementation("org.springframework.data:spring-data-mongodb:4.0.1") // Database
  implementation("io.projectreactor:reactor-core:3.6.0") // Database
  // implementation("org.mongodb:mongodb-driver-async:3.12.11")
  // implementation("mysql:mysql-connector-java:8.0.31")
  // implementation("pro.husk:mysql:1.4.1")

  // For own API use
  // compileOnly(files("/Users/jackolix/IdeaProjects/Paper/Paper-API/build/libs/paper.jar")) // Custom paper-api
  // implementation("net.kyori:adventure-api:4.12.0")
  // compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
}

repositories {
  mavenCentral()
  maven("https://jitpack.io")
  maven("https://oss.sonatype.org/content/repositories/snapshots")
  maven("https://maven.enginehub.org/repo/")
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.lucko.me/") // Spark profiler
}

tasks {
  // Configure reobfJar to run when invoking the build task
  assemble {
    dependsOn(reobfJar)
  }
  compileJava {
    options.encoding = Charsets.UTF_8.name()
    options.release.set(21)
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
  processResources {
    filteringCharset = Charsets.UTF_8.name()
  }

  jar {
    // destinationDirectory.set(file("/Users/jackolix/Development/Server/Paper1.19/plugins"))
  }

  reobfJar {
    // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
    // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
    // outputJar.set(layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar"))
  }

}