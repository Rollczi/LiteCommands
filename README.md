
<div align="right">
    
[![dependency](https://repo.panda-lang.org/api/badge/latest/releases/dev/rollczi/litecommands-core?color=9372f9&name=Version)](https://repo.panda-lang.org/#/releases/dev/rollczi/litecommands)  
</div>

<div align="center">

<img src="https://github.com/Rollczi/LiteCommands/assets/49173834/a5a1de27-af53-4d19-86e7-3f3306695e9a" alt="banner" width="50%"/>
<br><br>

[![Discord](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/social/discord-plural_vector.svg)](https://discord.gg/6cUhkj6uZJ)
[![Sponsor](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/donate/ghsponsors-singular_vector.svg)](https://github.com/sponsors/Rollczi)
[![Gradle](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/built-with/gradle_vector.svg)](https://gradle.org/)
[![Java](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/built-with/java8_vector.svg)](https://www.java.com/)

### LiteCommands
Annotation based command framework for Velocity, Bukkit, Paper, BungeeCord, Minestom JDA and future implementations.

</div>

### 📌 Helpful links
- [Support Discord](https://discord.gg/6cUhkj6uZJ)
- [Bugs & issues report](https://github.com/Rollczi/LiteCommands/issues)
- [Documentation 3.x](https://litedevelopers.github.io/LiteDevelopers-documentation/introdution.html)
- [Documentation 2.x](https://docs.rollczi.dev/)

### ✨ Features
- Simple Arguments [@Arg](https://litedevelopers.github.io/LiteDevelopers-documentation/arg.html) & Optional arguments [@OptionalArg](https://litedevelopers.github.io/LiteDevelopers-documentation/optionalarg-null-way.html)
- Flags - [@Flag](https://litedevelopers.github.io/LiteDevelopers-documentation/flag.html)
- Joiner - _[@Join](https://litedevelopers.github.io/LiteDevelopers-documentation/join-argument.html) remaining arguments into a string_
- Asynchronous commands, argument parsing and completion. [@Async](https://github.com/Rollczi/minecraft-lista-plugin/blob/master/src/main/java/dev/rollczi/minecraftlista/MinecraftListaCommand.java#L19C10-L19C10)
- Command shortcuts [@Shortcut](https://github.com/Rollczi/LiteCommands/blob/master/litecommands-annotations/test/dev/rollczi/litecommands/annotations/shortcut/ShortcutMultilevelTest.java#L21C13-L21C13)
- Support for basic types: `int`, `double`, `Duration`, `LocalDateTime`, `Location` and [more](https://litedevelopers.github.io/LiteDevelopers-documentation/supported-types.html)!
- Support for collection types: `List`, `Set`, `TreeSet`, `Queue`, java array `T[]` and many others!
- [IntelliJ Plugin](https://github.com/LiteDevelopers/LiteCommands-IntelliJPlugin) that provides annotation inspections, syntax highlighting and [more](https://github.com/LiteDevelopers/LiteCommands-IntelliJPlugin)!
- Custom annotation validators. [(example)](https://github.com/Rollczi/LiteCommands/blob/master/examples/bukkit/src/main/java/dev/rollczi/example/bukkit/ExamplePlugin.java#L58C21-L58C21)
- Programmatic API for creating more dynamic commands. [(example)](https://github.com/Rollczi/LiteCommands/blob/master/examples/bukkit/src/main/java/dev/rollczi/example/bukkit/ExamplePlugin.java#L64C26-L64C26)
- Jakarta EE annotation validation. [(extension)](https://litedevelopers.github.io/LiteDevelopers-documentation/jakarta-extension.html)
- ChatGPT suggestions. [(extension)](https://litedevelopers.github.io/LiteDevelopers-documentation/chatgpt-extension.html)
- Adventure Kyori support. [(extension)](https://litedevelopers.github.io/LiteDevelopers-documentation/adventure-kyori.html) 
- and [more](https://litedevelopers.github.io/LiteDevelopers-documentation/introdution.html)! ✨

## 💡 Command Example
This is an example of `/hello <name> <amount>` command:

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
}
```
Then we need to register the command in plugin main class: (in this case for Bukkit)
```java
this.liteCommands = LiteBukkitFactory.builder("example-plugin")
    .commands(new HelloCommand())
    .build();
```

## 🚀 Get Started 

#### ➕ Add Panda Repository ❤️
```kts
maven("https://repo.panda-lang.org/releases")
```
```xml
<repository>
    <id>panda-repository</id>
    <url>https://repo.panda-lang.org/releases</url>
</repository>
```

#### ➕ Add LiteCommands to dependencies
```kts
implementation("dev.rollczi:{artifact}:3.3.1")
```
```xml
<dependency>
    <groupId>dev.rollczi</groupId>
    <artifactId>{artifact}</artifactId>
    <version>3.3.1</version>
</dependency>
```
> ⚠️ Replace `{artifact}` with [platform artifact](https://litedevelopers.github.io/LiteDevelopers-documentation/platforms.html)  
> Add `-parameters` flag to your compiler to use all features [(read more)](https://litedevelopers.github.io/LiteDevelopers-documentation/parameters-compile-flag.html)

<h2> 💖 GitHub Sponsors</h2>
    
```diff
@@ Special thanks to all sponsors for supporting me and my projects! ❤️ @@
```

<!-- sponsors --><a href="https://github.com/rchomczyk"><img src="https://images.weserv.nl/?url=https://github.com/rchomczyk.png?v=4&mask=circle" width="60px" alt="Rafał Chomczyk" /></a>&nbsp;&nbsp;&nbsp;<a href="https://github.com/SfenKer"><img src="https://images.weserv.nl/?url=https://github.com/SfenKer.png?v=4&mask=circle" width="60px" alt="Hubert Kuliniak" /></a>&nbsp;&nbsp;&nbsp;<a href="https://github.com/P1otrulla"><img src="https://images.weserv.nl/?url=https://github.com/P1otrulla.png?v=4&mask=circle" width="60px" alt="Piotr Zych" /></a>&nbsp;&nbsp;&nbsp;<!-- sponsors -->

## 🔮 Official IntelliJ Plugin

With LiteCommands plugin, you can easily develop your commands within the **IntelliJ IDEA** environment. 
The plugin provides **annotation inspections**, **tools** for creating new command classes, **syntax highlighting**, **code completion**, 
and other useful features to make your development experience smoother and more efficient. How to [install](https://github.com/LiteDevelopers/LiteCommands-IntelliJPlugin?tab=readme-ov-file#installation)? 
![litecommandsbanner-intellij](https://github.com/Rollczi/LiteCommands/assets/49173834/dafd034e-4dab-4ab0-9984-489ae6808d8d)

## 🌍 More Examples
#### ➕ Official examples
- [Bukkit Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit)
- [Velocity Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/velocity)
- [ChatGPT Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit-chatgpt)
- [Bukkit (with Adventure Platform)](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit-adventure-platform)

#### ➕ Plugins that use LiteCommands!
- [EternalCore](https://github.com/EternalCodeTeam/EternalCore)
- [EternalCombat](https://github.com/EternalCodeTeam/EternalCombat)
- [ChatFormatter](https://github.com/EternalCodeTeam/ChatFormatter)


<div>
    <h2>💖 Thanks for our partners!</h2>
    <a  href="https://www.jetbrains.com/idea/">
        <img src="https://github.com/Rollczi/LiteCommands/assets/49173834/44190624-9da5-4f2c-ac43-dcb5caab1c62" alt="JetBrains" width="7%"/>
    </a>
    <p>
        <b>JetBrains</b><br>
        Supports us with a free license for the most intelligent Java IDE - <b><a href="https://www.jetbrains.com/idea/">IntelliJ IDEA Ultimate</a></b>
    </p>
    <a  href="https://reposilite.com/">
        <img src="https://avatars.githubusercontent.com/u/88636591" alt="Reposilite" width="7%"/>
    </a>
    <p>
        <b>Reposilite</b><br>
        Provides us with a Maven repository for our artifacts - <b><a href="https://repo.panda-lang.org/">Reposilite Repository</a></b>
    </p>
    <a href="https://www.yourkit.com/java/profiler/">
        <img src="https://www.yourkit.com/images/yklogo.png" alt="YourKit" width="15%"/>
    </a>
    <p>
        <b>YourKit</b><br>
        Supports us with a innovative and intelligent tools for monitoring and profiling - <b><a href="https://www.yourkit.com/java/profiler/">YourKit Java Profiler</a></b>
    </p>
</div>


## 🔐 Dependencies used
- [panda-lang/expressible](https://github.com/panda-lang/expressible)
- [JetBrains/java-annotations](https://github.com/JetBrains/java-annotations)
