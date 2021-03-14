# Freeze
A simple plugin that allows you to stop players from moving and executing commands.  
[Spigot Link](https://www.spigotmc.org/resources/31822/)

### Freeze API
This plugin is hosted by CodeMC.  
Maven Details:
```xml
<!-- SirBlobman Public Repository -->
<repository>
    <id>sirblobman-public</id>
    <url>https://nexus.sirblobman.xyz/repository/public/</url>
</repository>

<-- Freeze -->
<dependency>
    <groupId>com.github.sirblobman</groupId>
    <artifactId>freeze</artifactId>
    <version>2.0.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

### API Usage
#### Check if a player is frozen:
Make sure that you have Freeze as a dependency and its installed on the server.
```java
public boolean isFrozen(Player player) {
    FreezePlugin plugin = JavaPlugin.getPlugin(FreezePlugin.class);
    FreezeManager freezeManager = plugin.getFreezeManager();
    return freezeManager.isFrozen(player);
}
```