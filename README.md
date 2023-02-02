# Mojang API for Java 

### Getting Started
#### Maven
```xml
<repositories>
    <repository>
        <id>eldonexus-repo</id>
        <url>https://eldonexus.de/repository/maven-public/</url>
    </repository>
</repositories>
```
```xml
<dependency>
    <groupId>de.snowii</groupId>
    <artifactId>mojang-api</artifactId>
    <version>1.0.1</version>
</dependency>
```
#### Gradle
```groovy
repositories {
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public/")
}
```
```groovy
dependencies {
    implementation("de.snowii:mojang-api:1.0.0")
}
```

If your using a Proxy you Probably should set it at the Begging
```java
        MojangJSONParser.setProxy(<proxy>);
```

after this you can use the API in the MojangAPI class, for example
```java
        GameProfile profile = MojangAPI.getGameProfile("Notch", true);
```
the same with UUID's 
```java
        GameProfile profile = MojangAPI.getGameProfile(UUID.fromString("7bd9c2cb-079f-4f5b-925d-4bffdcf24aa8"), true);
```
the boolean tells if the Profile should be cached this is highly recommended if your want u use the Same Profile several times,
But if your using cache you should clear it at the End Also using:
```java
        MojangAPI.clearCache();
```
