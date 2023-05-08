# Freeze

A moderation plugin that prevents players from moving and doing certain actions.

## Requirements

- Java 17
- [Spigot](https://www.spigotmc.org/), [Paper](https://papermc.io/downloads/paper), or [Folia](https://papermc.io/software/folia) 1.8.8-1.19.4
- [BlueSlimeCore](https://hangar.papermc.io/SirBlobman/BlueSlimeCore)

## Downloads

[SpigotMC](https://www.spigotmc.org/resources/31822/)

## Freeze API

### Dependency Information

**Maven Repository:**

```xml
<!-- SirBlobman Public Repository -->
<repository>
    <id>sirblobman-public</id>
    <url>https://nexus.sirblobman.xyz/public/</url>
</repository>
```

**Maven Dependency:**

```xml
<!-- Freeze Plugin -->
<dependency>
    <groupId>com.github.sirblobman</groupId>
    <artifactId>freeze</artifactId>
    <version>2.5-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

**Bukkit plugin.yml:**

```yaml
depend:
  - "Freeze"
```

### Check if a player is frozen:

Ensure that you have Freeze as a dependency and that the plugin is installed on the server.

**FreezeHook.java**

```java
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.freeze.FreezePlugin;
import com.github.sirblobman.freeze.manager.FreezeManager;

public final class FreezeHook {
    public static boolean isEnabled() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        return pluginManager.isPluginEnabled("Freeze");
    }

    public static boolean isFrozen(Player player) {
        FreezePlugin plugin = JavaPlugin.getPlugin(FreezePlugin.class);
        FreezeManager freezeManager = plugin.getFreezeManager();
        return freezeManager.isFrozen(player);
    }
}
```

### Events

There are two events you can use from this plugin:

- `com.github.sirblobman.freeze.event.PlayerFreezeEvent`
- `com.github.sirblobman.freeze.event.PlayerMeltEvent`
