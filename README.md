# Mojang API for Java

The Mojang API for Java is a library that provides Java developers with an easy-to-use interface for accessing Mojang's APIs. This library makes it simple to fetch game profiles and UUIDs from Mojang's servers, and cache them locally for faster access.

### Getting Started

To get started with the Mojang API for Java, you can add it to your Maven or Gradle project as a dependency.

#### Maven
```xml
<repositories>
    <repository>
        <id>eldonexus-repo</id>
        <url>https://eldonexus.de/repository/maven-public/</url>
    </repository>
</repositories>
```
Then add the dependency:
```xml
<dependency>
    <groupId>de.snowii</groupId>
    <artifactId>mojang-api</artifactId>
    <version>1.1.0</version>
</dependency>
```
### Gradle
Add the following repository to your build.gradle file:
```groovy
repositories {
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public/")
}
```
Then add the dependency:
```groovy
dependencies {
    implementation("de.snowii:mojang-api:1.1.0")
}
```

### Using the API

Once you have added the library to your project, you can use the API in the MojangAPI class. For example, to get a GameProfile object for a given username:
```java
    GameProfile profile = MojangAPI.getGameProfile("Notch");
```

You can also get a GameProfile by UUID:
```java
     GameProfile profile = MojangAPI.getGameProfile(UUID.fromString("7bd9c2cb-079f-4f5b-925d-4bffdcf24aa8"));
```
### Using a Proxy

If you are using a proxy, you can set it at the beginning of your code with:
```java
      MojangJSONParser.setProxy(<proxy>);
```
