<div align="right">

[![dependency](https://repo.panda-lang.org/api/badge/latest/releases/dev/rollczi/litecommands-core?color=9372f9&name=Version)](https://repo.panda-lang.org/#/releases/dev/rollczi/litecommands)
</div>

<div align="center">

<img src="https://github.com/Rollczi/LiteCommands/assets/49173834/4c297b16-eacd-4c89-ba34-2767b114e391" alt="banner" width="65%"/>
<br><br>

[![Discord](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/social/discord-plural_vector.svg)](https://discord.gg/6cUhkj6uZJ)
[![Sponsor](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/donate/ghsponsors-singular_vector.svg)](https://github.com/sponsors/Rollczi)
[![Gradle](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/built-with/gradle_vector.svg)](https://gradle.org/)
[![Java](https://raw.githubusercontent.com/intergrav/devins-badges/v3/assets/cozy/built-with/java8_vector.svg)](https://www.java.com/)

### LiteCommands
Annotation based command framework for Velocity, Bukkit, Paper, BungeeCord, Minestom, Sponge, Fabric, JDA, TelegramBots and future implementations.

</div>

### 📌 Helpful links
- [Support Discord](https://discord.gg/6cUhkj6uZJ)
- [Bugs & issues report](https://github.com/Rollczi/LiteCommands/issues)
- [Documentation](https://docs.rollczi.dev/)

### ✨ Features
- Simple Arguments [@Arg](https://docs.rollczi.dev/documentation/litecommands/arguments/arg.html#arg) & Optional arguments [@OptionalArg](https://docs.rollczi.dev/documentation/litecommands/arguments/arg-optional.html#arg-optional)
- Flags - [@Flag](https://docs.rollczi.dev/documentation/litecommands/arguments/flag.html#flag)
- Joiner - _[@Join](https://docs.rollczi.dev/documentation/litecommands/arguments/join.html#join) remaining arguments into a string_
- Literal arguments - @Literal
- [@Quoted](https://docs.rollczi.dev/documentation/litecommands/arguments/quoted.html) arguments for `"quoted input!"`
- Asynchronous commands, argument parsing and completion. [@Async](https://docs.rollczi.dev/documentation/litecommands/features/asynchronous/async-annotation.html#async)
- Context providers - e.g. `@Sender Player`, `@Context World` and [more](https://docs.rollczi.dev/documentation/litecommands/getting-started/context.html)
- Command shortcuts [@Shortcut](https://docs.rollczi.dev/documentation/litecommands/features/shortcut.html#shortcut)
- Support for basic types: `int`, `double`, `Duration`, `LocalDateTime`, `Location` and [more](https://docs.rollczi.dev/documentation/litecommands/arguments/arg/java-types.html#java-types)!
- Support for collection types: `List`, `Set`, `TreeSet`, `Queue`, java array `T[]` and many others!
- [IntelliJ Plugin](https://github.com/LiteDevelopers/LiteCommands-IntelliJPlugin) that provides annotation inspections, syntax highlighting and [more](https://docs.rollczi.dev/documentation/litecommands/intellij-idea-plugin.html)!
- Custom annotation validators. [(example)](https://github.com/Rollczi/LiteCommands/blob/master/examples/bukkit/src/main/java/dev/rollczi/example/bukkit/ExamplePlugin.java#L58C21-L58C21)
- Programmatic API for creating more dynamic commands. [(example)](https://github.com/Rollczi/LiteCommands/blob/master/examples/bukkit/src/main/java/dev/rollczi/example/bukkit/ExamplePlugin.java#L64C26-L64C26)
- Jakarta EE annotation validation. [(extension)](https://docs.rollczi.dev/documentation/litecommands/extensions/jakarata.html#jakarta)
- ChatGPT suggestions. [(extension)](https://docs.rollczi.dev/documentation/litecommands/extensions/chatgpt-extension.html#chatgpt)
- Adventure Kyori support. [(extension)](https://docs.rollczi.dev/documentation/litecommands/extensions/kyori-adventure/adventure-kyori.html#adventure-native) 
- and [more](https://docs.rollczi.dev/documentation/litecommands/what-is-litecommands.html)! ✨

## 📦 Platforms
- Velocity
- Bukkit, Spigot, Paper
- Folia (extension) _(by [Kropsk](https://github.com/Kropsk))_
- BungeeCord, Waterfall _(by [rchomczyk](https://github.com/rchomczyk))_
- Minestom _(by [Codestech1](https://github.com/Codestech1))_
- Sponge _(by [BlackBaroness](https://github.com/BlackBaroness))_
- Fabric _(by [huanmeng_qwq](https://github.com/huanmeng-qwq))_
- JDA
- TelegramBots _(by [BlackBaroness](https://github.com/BlackBaroness))_

## 💡 Command Example
This is an example of `/hello <name> <amount>` command:

```java
@Command(name = "hello")
@Permission("dev.rollczi.helloworld")
public class HelloCommand {

    @Execute
    void command(@Sender CommandSender sender, @Arg String name, @Arg int amount) {
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
implementation("dev.rollczi:{artifact}:3.10.9")
```
```xml
<dependency>
    <groupId>dev.rollczi</groupId>
    <artifactId>{artifact}</artifactId>
    <version>3.10.9</version>
</dependency>
```
> ⚠️ Replace `{artifact}` with [platform artifact](https://docs.rollczi.dev/documentation/litecommands/platforms.html#supported-platforms)  
> Add `-parameters` flag to your compiler to use all features [(read more)](https://docs.rollczi.dev/documentation/litecommands/getting-started/dependencies.html#parameters-compile-flag)

<h2> 💖 GitHub Sponsors</h2>
    
```diff
@@ Special thanks to all sponsors for supporting me and my projects! ❤️ @@
```

<!-- sponsors --><a href="https://github.com/Nadwey"><img src="https://images.weserv.nl/?url=https://github.com/Nadwey.png?v=4&mask=circle" width="60px" alt="Nadwey" /></a>&nbsp;&nbsp;&nbsp;<!-- sponsors -->

## 🔮 Official IntelliJ Plugin

With LiteCommands plugin, you can easily develop your commands within the **IntelliJ IDEA** environment. 
The plugin provides **annotation inspections**, **tools** for creating new command classes, **syntax highlighting**, **code completion**, 
and other useful features to make your development experience smoother and more efficient. How to [install](https://github.com/LiteDevelopers/LiteCommands-IntelliJPlugin?tab=readme-ov-file#installation)? 
![litecommandsbanner-intellij](https://github.com/Rollczi/LiteCommands/assets/49173834/fea96613-af58-42bb-b32d-ffd6946ba4cc)

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
        <img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jetbrains.svg" alt="JetBrains" width="20%"/>
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

