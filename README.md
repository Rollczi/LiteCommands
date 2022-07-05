# ☄️ LiteCommands [![dependency](https://repo.panda-lang.org/api/badge/latest/releases/dev/rollczi/litecommands/core?color=53a2f9&name=LiteCommands)](https://repo.panda-lang.org/#/releases/dev/rollczi/litecommands) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/paypalme/NDejlich) [![Discord](https://img.shields.io/discord/896933084983877662?color=8f79f4&label=Lite%20Discord)](https://img.shields.io/discord/896933084983877662?color=8f79f4&label=Lite%20Discord) [![OSCS Status](https://www.oscs1024.com/platform/badge/Rollczi/LiteCommands.svg?size=small)](https://www.oscs1024.com/project/Rollczi/LiteCommands?ref=badge_small)

#### Command framework for Velocity and your other implementations.
Helpful links:
- [Support Discord](https://discord.gg/6cUhkj6uZJ)
- [GitHub issues](https://github.com/Rollczi/LiteCommands/issues)
- [Example (Modern 2.2.1)](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit)
- [Docs (Legacy 1.7.2)](https://docs.rollczi.dev/)

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
    <version>2.2.1</version>
</dependency>
```
```groovy
implementation 'dev.rollczi.litecommands:core:2.2.1'
```

### First Simple Command
 `/helloworld <text...>`
```java
@Section(route = "helloworld")
@Permission("dev.rollczi.helloworld")
public class HelloWorldCommand {

    @Execute
    @Min(1)
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
    <version>2.2.1</version>
</dependency>
```
```groovy
implementation 'dev.rollczi.litecommands:velocity:2.2.1'
```

#### All extensions: 
 - [Velocity](https://github.com/Rollczi/LiteCommands/tree/master/litecommands-velocity)
 - [Bukkit](https://github.com/Rollczi/LiteCommands/tree/master/litecommands-bukkit)
 - [BungeeCord](https://github.com/Rollczi/LiteCommands/tree/master/litecommands-bungee)
#### Other examples: 
 - [Wiki Velocity Example](https://github.com/Rollczi/LiteCommands/wiki/Velocity-Example-(simple))
 - [Wiki Velocity Example (advanced)](https://github.com/Rollczi/LiteCommands/wiki/Velocity-Example-(advanced))
 - [Bukkit Example](https://github.com/Rollczi/LiteCommands/tree/master/examples/bukkit)

See (Important dependencies used)
 - [panda-lang/expressible](https://github.com/panda-lang/expressible)
 - [panda-lang/panda (panda-utilities)](https://github.com/panda-lang/panda) (v1.0.0 - v1.9.2) (in v2.0.0 and above a built-in DI modeled on it is used)
