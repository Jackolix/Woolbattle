plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "1.3.8"
  id("xyz.jpenilla.run-paper") version "1.0.6"
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

group = "codes.Elix"
version = "1.0"
description = "Gamemode for Minecraft"


java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {

  paperDevBundle("1.20-R0.1-SNAPSHOT")
  implementation(platform("com.intellectualsites.bom:bom-1.18.x:1.20")) //FAWE
  compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.5.0") //FAWE
  compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:2.5.0") //FAWE

  // implementation("com.github.ShieldCommunity:SternalBoard:2.1.0") // ScoreBoard
  implementation("org.mongodb:mongodb-driver-reactivestreams:4.8.1") // Database
  implementation("org.springframework.data:spring-data-mongodb:4.0.1") // Database
  implementation("io.projectreactor:reactor-core") // Database
  // implementation("org.mongodb:mongodb-driver-async:3.12.11")
  // implementation("mysql:mysql-connector-java:8.0.31")
  // implementation("pro.husk:mysql:1.4.1")

  // For own API use
  // compileOnly(files("/Users/jackolix/IdeaProjects/Paper/Paper-API/build/libs/paper.jar")) // Custom paper-api
  // implementation("net.kyori:adventure-api:4.12.0")
  // compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
}

dependencyManagement {
  imports {
    mavenBom("io.projectreactor:reactor-bom:2022.0.2")
  }
}
repositories {
  mavenCentral()
  maven("https://jitpack.io")
  maven("https://oss.sonatype.org/content/repositories/snapshots")
}

tasks {
  // Configure reobfJar to run when invoking the build task
  assemble {
    dependsOn(reobfJar)
  }
  compileJava {
    options.encoding = Charsets.UTF_8.name()
    options.release.set(17)
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
  processResources {
    filteringCharset = Charsets.UTF_8.name()
  }

  jar {
    // destinationDirectory.set(file("/Users/jackolix/Development/Server/Paper1.19/plugins"))
    // finalizedBy("push")
  }

  register<Exec>("push") {
    commandLine("sh", "/Users/jackolix/IdeaProjects/WoolbattleOLD/push.sh")
  }


  reobfJar {
    // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
    // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
    // outputJar.set(layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar"))
    finalizedBy("push")
  }

}