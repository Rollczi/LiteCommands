<div align="center"><img src="https://github.com/Rollczi/LiteCommands/assets/49173834/a5a1de27-af53-4d19-86e7-3f3306695e9a" alt="banner" width="50%"/></div>

# ☄️ LiteCommands [![dependency](https://repo.panda-lang.org/api/badge/latest/releases/dev/rollczi/litecommands-core?color=53a2f9&name=LiteCommands)](https://repo.panda-lang.org/#/releases/dev/rollczi/litecommands) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/paypalme/NDejlich) [![Discord](https://img.shields.io/discord/896933084983877662?color=8f79f4&label=Lite%20Discord)](https://img.shields.io/discord/896933084983877662?color=8f79f4&label=Lite%20Discord)

#### Annotation-based Command framework for Velocity, Bukkit, Paper, BungeeCord, Minestom, JDA and future implementations :D.

#### Helpful links:
- [Support Discord](https://discord.gg/6cUhkj6uZJ)
- [GitHub issues](https://github.com/Rollczi/LiteCommands/issues)
- [Documentation 3.x](https://litedevelopers.github.io/LiteDevelopers-documentation/introdution.html)
- [Documentation 2.x](https://docs.rollczi.dev/)

#### Features
- Arguments [@Arg](https://litedevelopers.github.io/LiteDevelopers-documentation/arg.html), Flags [@Flag](https://litedevelopers.github.io/LiteDevelopers-documentation/flag.html) and Optional arguments [@OptionalArg](https://litedevelopers.github.io/LiteDevelopers-documentation/optionalarg-null-way.html)
- Support for argument parsers: int, double, Duration, LocalDateTime, Location, Player [and more](https://litedevelopers.github.io/LiteDevelopers-documentation/supported-types.html)
- Asynchronous commands, argument parsing and completion. @Async
- Support for collection types: List, Set, TreeSet, Stack, Queue, traditional java array T[] and more!
- Auto argument joiner [@Join](https://litedevelopers.github.io/LiteDevelopers-documentation/join-argument.html)
- Support for multilevel argument parsing and suggestions e.g. LocalDateTime `yyyy-MM-dd HH:mm`
- [IntelliJ Plugin](https://github.com/LiteDevelopers/LiteCommands-IntelliJPlugin) for LiteCommands provides annotation inspections, syntax highlighting and a command viewer.
- Custom annotation validators [see example](https://github.com/Rollczi/LiteCommands/blob/master/examples/bukkit/src/main/java/dev/rollczi/example/bukkit/ExamplePlugin.java#L58C21-L58C21)
- Programmatic API for creating more dynamic commands. [see example](https://github.com/Rollczi/LiteCommands/blob/master/examples/bukkit/src/main/java/dev/rollczi/example/bukkit/ExamplePlugin.java#L64C26-L64C26)
- Jakarta EE annotation validation. [see the extension](https://litedevelopers.github.io/LiteDevelopers-documentation/jakarta-extension.html)
- ChatGPT suggestions [see the extension](https://litedevelopers.github.io/LiteDevelopers-documentation/chatgpt-extension.html)
- Adventure Kyori support
- and many more! ✨

### Command Example
`/hello <name> <amount>`  
`/hello message <text...>`  

```java
@Command(name = "hello")
@Permission("dev.rollczi.helloworld")
public class HelloCommand {

    @Execute
    void command(@Context CommandSender sender, @Arg String name, @Arg int amount) {
        for (int i = 0; i < amount; i++) {
            sender.sendMessage("Hello " + name);
        }
    }
    
    @Execute(name = "message")
    void subcommand(@Context CommandSender sender, @Join String text) {
        sender.sendMessage(text);
    }

}
```

Register your first command in plugin main class: (in this case for Bukkit)
```java
this.liteCommands = LiteBukkitFactory.builder("example-plugin")
    .commands(
        new HelloWorldCommand()
    )
    .build();
```

### Add Panda Repository ❤️
```kts
maven("https://repo.panda-lang.org/releases")
```
```xml
<repository>
    <id>panda-repository</id>
    <url>https://repo.panda-lang.org/releases</url>
</repository>
```

### Add LiteCommands to dependencies
```kts
implementation("dev.rollczi:{artifact}:3.2.2")
```
```xml
<dependency>
    <groupId>dev.rollczi</groupId>
    <artifactId>{artifact}</artifactId>
    <version>3.2.2</version>
</dependency>
```
`{artifact}` replace with [platform artifact](https://github.com/Rollczi/LiteCommands#platform-artifacts)

### Platform artifacts

| Artifact                  | Platform                      | Adventure Support |
|---------------------------|-------------------------------|-------------------|
| `litecommands-velocity`   | Velocity                      | yes               |
| `litecommands-bukkit`     | Bukkit, Spigot, Paper, Purpur | no                |
| `litecommands-bungeecord` | BungeeCord, Waterfall         | no                |
| `litecommands-minestom`   | Minestom                      | yes               |
| `litecommands-jda`        | JDA (Java Discord API)        | -                 |

### Add `-parameters` to your compiler to use all features of LiteCommands (optional)
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

#### Examples:
- [Bukkit Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit)
- [Velocity Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/velocity)
- [ChatGPT Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit-chatgpt)
- [Bukkit (with Adventure Platform)](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit-adventure-platform)

#### Plugins that use LiteCommands:
- [EternalCore](https://github.com/EternalCodeTeam/EternalCore)
- [EternalCombat](https://github.com/EternalCodeTeam/EternalCombat)
- [ChatFormatter](https://github.com/EternalCodeTeam/ChatFormatter)

#### Dependencies used
- [panda-lang/expressible](https://github.com/panda-lang/expressible)
- [JetBrains/java-annotations](https://github.com/JetBrains/java-annotations)

<div align="center">
    <h3>Thanks to all sponsors!</h3>
    <p>Special thanks to all sponsors for supporting me and my projects! ❤️</p>
    <!-- sponsors --><a href="https://github.com/rchomczyk"><img src="https://github.com/rchomczyk.png" width="60px" alt="Rafał Chomczyk" /></a>&nbsp;&nbsp;&nbsp;<a href="https://github.com/SfenKer"><img src="https://github.com/SfenKer.png" width="60px" alt="Hubert Kuliniak" /></a>&nbsp;&nbsp;&nbsp;<a href="https://github.com/P1otrulla"><img src="https://github.com/P1otrulla.png" width="60px" alt="Piotr Zych" /></a>&nbsp;&nbsp;&nbsp;<!-- sponsors -->
</div>


<div align="center">
    <br><br><br>
    <h3>Also thanks to our partners!</h3>
    <a  href="https://www.jetbrains.com/idea/">
        <img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_square.svg" alt="JetBrains" width="20%"/>
    </a>
    <p>
        <b>JetBrains</b><br>
        Supports us with a free license for the most intelligent Java IDE - <b><a href="https://www.jetbrains.com/idea/">IntelliJ IDEA Ultimate</a></b>
    </p>
    <br>
    <br>
    <br>
    <a  href="https://reposilite.com/">
        <img src="https://avatars.githubusercontent.com/u/88636591" alt="Reposilite" width="15%"/>
    </a>
    <br>
    <p>
        <b>Reposilite</b><br>
        Provides us with a Maven repository for our artifacts - <b><a href="https://repo.panda-lang.org/">Reposilite Repository</a></b>
    </p>
    <br>
    <br>
    <br>
    <a href="https://www.yourkit.com/java/profiler/">
        <img src="https://www.yourkit.com/images/yklogo.png" alt="YourKit" width="25%"/>
    </a>
    <br>
    <p>
        <b>YourKit</b><br>
        Supports us with a innovative and intelligent tools for monitoring and profiling - <b><a href="https://www.yourkit.com/java/profiler/">YourKit Java Profiler</a></b>
    </p>
</div>
