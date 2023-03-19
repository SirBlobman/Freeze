# Freeze

A simple plugin that allows you to stop players from moving and executing commands.  
[Spigot Link](https://www.spigotmc.org/resources/31822/)

### Freeze API

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
    <version>2.0.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

### API Usage

#### Check if a player is frozen:

Make sure that you have Freeze as a dependency and it's installed on the server.

**plugin.yml:**

```yaml
softdepend:
  - "Freeze"
```

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
