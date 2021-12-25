# ☄️ LiteCommands [![dependency](https://repo.panda-lang.org/api/badge/latest/releases/dev/rollczi/litecommands/core?color=53a2f9&name=LiteCommands)](https://repo.panda-lang.org/#/releases/dev/rollczi/litecommands) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/paypalme/NDejlich)
#### Command framework for Velocity and your other implementations.
Helpful links:
- [Support Discord](https://discord.gg/6cUhkj6uZJ)
- [GitHub issues](https://github.com/Rollczi/LiteCommands/issues)

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
    <groupId>dev.rollczi.litecommands</groupId>
    <artifactId>core</artifactId>
    <version>1.4.2</version>
</dependency>
```
```groovy
implementation 'dev.rollczi.litecommands:core:1.4.2'
```

### First Simple Command
 `/helloworld <text...>`
```java
@Section(route = "helloworld")
@Permission("dev.rollczi.helloworld")
public class HelloWorldCommand {

    @Execute
    @MinArgs(1)
    public void execute(LiteSender sender, String[] args) {
        sender.sendMessage(String.join(" ", args));
    }

}
```
Register your first command in plugin main class: (in this case for Velocity)
```java
this.liteCommands = LiteVelocityFactory.builder(proxy)
        .command(HelloWorldCommand.class)
        .register();
```
### Velocity Extension Dependencies (Maven or Gradle)
Add this to your dependencies if you want use ready-made implementation for velocity.
```xml
<dependency>
    <groupId>dev.rollczi.litecommands</groupId>
    <artifactId>velocity</artifactId>
    <version>1.4.2</version>
</dependency>
```
```groovy
implementation 'dev.rollczi.litecommands:velocity:1.4.2'
```

#### All extensions: 
 - [Velocity](https://github.com/Rollczi/LiteCommands/tree/master/litecommands-velocity)
 - [Bukkit](https://github.com/Rollczi/LiteCommands/tree/master/litecommands-bukkit)
 - [BungeeCord](https://github.com/Rollczi/LiteCommands/tree/master/litecommands-bungee)
#### Other examples: 
 - [Wiki Velocity Example](https://github.com/Rollczi/LiteCommands/wiki/Velocity-Example-(simple))
 - [Wiki Velocity Example (advanced)](https://github.com/Rollczi/LiteCommands/wiki/Velocity-Example-(advanced))
 - [(Rollczi) Bukkit Example](https://github.com/Rollczi/LiteCommands-BukkitExample)
 - [(shitzuu) BungeeCord Example](https://github.com/shitzuu/LiteCommands-BungeeExample)

See (Important dependencies used)
 - [panda-lang/expressible](https://github.com/panda-lang/expressible)
 - [panda-lang/panda (panda-utilities)](https://github.com/panda-lang/panda)
