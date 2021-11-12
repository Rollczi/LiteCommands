# ☄️ LiteCommands
#### Command framework for Velocity and your other implementations.
Helpful links:
- [Support Discord](https://discord.gg/6cUhkj6uZJ)
- [GitHub issues](https://github.com/Rollczi/LiteCommands/issues)

### Dependencies (Maven or Gradle)
Framework Core
```xml
<dependency>
    <groupId>dev.rollczi.litecommands</groupId>
    <artifactId>core</artifactId>
    <version>1.0</version>
</dependency>
```
```gradle
implementation 'dev.rollczi.litecommands:core:1.0'
```

Velocity Extension
```xml
<dependency>
    <groupId>dev.rollczi.litecommands</groupId>
    <artifactId>velocity</artifactId>
    <version>1.0</version>
</dependency>
```
```gradle
implementation 'dev.rollczi.litecommands:velocity:1.0'
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
this.liteCommands = VelocityFactory.builder(proxy)
        .command(HelloWorldCommand.class)
        .register();
```

#### Other examples: 
 - [Velocity Example](https://github.com/Rollczi/LiteCommands/wiki/Velocity-Example-(simple))
 - [Velocity Example (advanced)](https://github.com/Rollczi/LiteCommands/wiki/Velocity-Example-(advanced))

See (Important dependencies used)
 - [panda-lang/expressible](https://github.com/panda-lang/expressible)
 - [panda-lang/panda (panda-utilities)](https://github.com/panda-lang/panda)
