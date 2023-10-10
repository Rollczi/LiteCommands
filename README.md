
<div align="center"><img src="https://github.com/Rollczi/LiteCommands/assets/49173834/c3f218a0-268a-419d-899d-703ab0501ff0" alt="hacktoberfest" width="60%"/></div>
<div align="center"><h3>LiteCommands & Hacktoberfest 2023</h3></div>
<div align="center">Register on <a href="https://hacktoberfest.com/">hacktoberfest.com</a> and start supporting open source!</div>
<div align="center">Add/Improve/Fix features and open <a href="https://github.com/Rollczi/LiteCommands/pulls">Pull request</a>! If you don't have an idea, see  <a href="https://github.com/Rollczi/LiteCommands/issues">issues</a>!</div>
<br>
<div align="center"><img src="https://savemc.pl/files/litecommandsbanner.png" alt="banner" width="50%"/></div>

# ☄️ LiteCommands [![dependency](https://repo.panda-lang.org/api/badge/latest/releases/dev/rollczi/litecommands/core?color=53a2f9&name=LiteCommands)](https://repo.panda-lang.org/#/releases/dev/rollczi/litecommands) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/paypalme/NDejlich) [![Discord](https://img.shields.io/discord/896933084983877662?color=8f79f4&label=Lite%20Discord)](https://img.shields.io/discord/896933084983877662?color=8f79f4&label=Lite%20Discord)

#### Command framework for Velocity, Bukkit, Paper, BungeeCord, Minestom, JDA and your other implementations.

Helpful links:

- [Support Discord](https://discord.gg/6cUhkj6uZJ)
- [GitHub issues](https://github.com/Rollczi/LiteCommands/issues)
- [Documentation](https://docs.rollczi.dev/)

#### Examples:

- [Bukkit Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit)
- [Velocity Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/velocity)
- [ChatGPT Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit-chatgpt)
- [Bukkit (with Adventure Platform)](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit-adventure-platform)

### Panda Repository (Maven or Gradle)  ❤️

```xml

<repository>
    <id>panda-repository</id>
    <url>https://repo.panda-lang.org/releases</url>
</repository>
```

```groovy
maven { url "https://repo.panda-lang.org/releases" }
```

### Dependencies (Maven or Gradle)

Framework Core

```xml

<dependency>
    <groupId>dev.rollczi</groupId>
    <artifactId>litecommands-core</artifactId>
    <version>3.0.0-BETA-pre22</version>
</dependency>
```

```groovy
implementation 'dev.rollczi:litecommands-core:3.0.0-BETA-pre22'
```

### First Simple Command

`/hello-world <name> <amount>`  
`/hello-world message <text...>`  

```java

@Command(name = "hello-world")
@Permission("dev.rollczi.helloworld")
public class HelloWorldCommand {

    @Execute
    public void command(@Context CommandSender sender, @Arg String name, @Arg int amount) {
        for (int i = 0; i < amount; i++) {
            sender.sendMessage("Hello " + name);
        }
    }
    
    @Execute(name = "message")
    public void subcommand(@Context CommandSender sender, @Join String text) {
        sender.sendMessage(text);
    }

}
```

Register your first command in plugin main class: (in this case for Bukkit)

```java
this.liteCommands = LiteBukkitFactory.builder("example-plugin")
    .commands(LiteCommandsAnnotations.of(
        new HelloWorldCommand()
    ))
    .build();
```

### Bukkit Platform Dependencies (Maven or Gradle)

Add this to your dependencies if you want to use ready-made implementation for bukkit.

```kts
implementation("dev.rollczi:litecommands-bukkit:3.0.0-BETA-pre22")
```

```xml
<dependency>
    <groupId>dev.rollczi</groupId>
    <artifactId>litecommands-bukkit</artifactId>
    <version>3.0.0-BETA-pre22</version>
</dependency>
```

#### Platforms
- Velocity `litecommands-velocity`
- Bukkit `litecommands-bukkit`
- BungeeCord `litecommands-bungee`
- Minestom `litecommands-minestom`
- JDA `litecommands-jda`

#### Extensions:
- Adventure `litecommands-adventure`
- Adventure Platform `litecommands-adventure-platform`
- ChatGPT `litecommands-chatgpt`

### Add -parameters to your compiler to use all features of LiteCommands
Gradle
```groovy
tasks.withType(JavaCompile) {
    options.compilerArgs << "-parameters"
}
```
Gradle KTS
```kotlin
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
```
Maven
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>3.8.1</version>
  <configuration>
    <compilerArgs>
      <arg>-parameters</arg>
    </compilerArgs>
  </configuration>
</plugin>
```

#### Dependencies used
- [panda-lang/expressible](https://github.com/panda-lang/expressible)
- [JetBrains/java-annotations](https://github.com/JetBrains/java-annotations)
#### Plugins that use LiteCommands:

- [EternalCore](https://github.com/EternalCodeTeam/EternalCore)
- [EternalCombat](https://github.com/EternalCodeTeam/EternalCombat)
- [ChatFormatter](https://github.com/EternalCodeTeam/ChatFormatter)
