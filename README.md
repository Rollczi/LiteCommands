<div align="center"><img src="https://github.com/Rollczi/LiteCommands/assets/49173834/a5a1de27-af53-4d19-86e7-3f3306695e9a" alt="banner" width="50%"/></div>

# ☄️ LiteCommands [![dependency](https://repo.panda-lang.org/api/badge/latest/releases/dev/rollczi/litecommands-core?color=53a2f9&name=LiteCommands)](https://repo.panda-lang.org/#/releases/dev/rollczi/litecommands) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/paypalme/NDejlich) [![Discord](https://img.shields.io/discord/896933084983877662?color=8f79f4&label=Lite%20Discord)](https://img.shields.io/discord/896933084983877662?color=8f79f4&label=Lite%20Discord)

#### Command framework for Velocity, Bukkit, Paper, BungeeCord, Minestom, JDA and your other implementations.

#### Helpful links:
- [Support Discord](https://discord.gg/6cUhkj6uZJ)
- [GitHub issues](https://github.com/Rollczi/LiteCommands/issues)
- [Documentation](https://docs.rollczi.dev/)

#### Examples:
- [Bukkit Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit)
- [Velocity Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/velocity)
- [ChatGPT Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit-chatgpt)
- [Bukkit (with Adventure Platform)](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit-adventure-platform)

### Panda Repository ❤️
```kts
maven("https://repo.panda-lang.org/releases")
```
```xml
<repository>
    <id>panda-repository</id>
    <url>https://repo.panda-lang.org/releases</url>
</repository>
```

### Dependency
```kts
 implementation("dev.rollczi:{artifact}:3.1.2")
```
```xml
<dependency>
    <groupId>dev.rollczi</groupId>
    <artifactId>{artifact}</artifactId>
    <version>3.1.2</version>
</dependency>
```
`{artifact}` replace with [platform artifact](https://github.com/Rollczi/LiteCommands#platform-artifacts)

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

### Platform artifacts

| Artifact                  | Platform                      | Adventure Support |
|---------------------------|-------------------------------|-------------------|
| `litecommands-velocity`   | Velocity                      | yes               |
| `litecommands-bukkit`     | Bukkit, Spigot, Paper, Purpur | no                |
| `litecommands-bungeecord` | BungeeCord, Waterfall         | no                |
| `litecommands-minestom`   | Minestom                      | yes               |
| `litecommands-jda`        | JDA (Java Discord API)        | -                 |

### Extension artifacts
| Artifact                          | Extension          | For platforms                                                                            |
|-----------------------------------|--------------------|------------------------------------------------------------------------------------------|
| `litecommands-adventure`          | Adventure          | with [Native Adventure support](https://docs.advntr.dev/platform/native.html) e.g. Paper |
| `litecommands-adventure-platform` | Adventure Platform | with [Adventure Platform](https://docs.advntr.dev/platform/index.html) e.g. Bukkit       |
| `litecommands-chatgpt`            | ChatGPT OpenAI API | all                                                                                      |

### Add `-parameters` to your compiler to use all features of LiteCommands
Gradle KTS
```kotlin
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
```
Gradle
```groovy
tasks.withType(JavaCompile) {
    options.compilerArgs << "-parameters"
}
```
Maven
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>3.11.0</version>
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

<div align="center">
    <h3>Thanks to all sponsors!</h3>
    <p>Special thanks to all sponsors for supporting me and my projects! ❤️</p>
    <!-- sponsors --><a href="https://github.com/rchomczyk"><img src="https://github.com/rchomczyk.png" width="60px" alt="Rafał Chomczyk" /></a>&nbsp;&nbsp;&nbsp;<a href="https://github.com/SfenKer"><img src="https://github.com/SfenKer.png" width="60px" alt="Hubert Kuliniak" /></a>&nbsp;&nbsp;&nbsp;<!-- sponsors -->
</div>


<div align="center">
    <h3>Also thanks to our partners!</h3>
    <p>
        <b>JetBrains - supports us with a free license for the most intelligent Java IDE - <a href="https://www.jetbrains.com/idea/">IntelliJ IDEA Ultimate</a></b>
    </p>
    <a  href="https://www.jetbrains.com/idea/">
        <img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_square.svg" alt="JetBrains" width="20%"/>
    </a>
    <p>
        <b>Reposilite - provides us with a Maven repository for our artifacts - <a href="https://repo.panda-lang.org/">repo.panda-lang.org</a></b>
    </p>
    <a  href="https://reposilite.com/">
        <img src="https://avatars.githubusercontent.com/u/88636591" alt="Reposilite" width="15%"/>
    </a>
</div>
