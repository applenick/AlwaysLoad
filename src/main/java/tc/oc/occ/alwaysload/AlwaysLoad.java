package tc.oc.occ.alwaysload;

import org.bukkit.plugin.java.JavaPlugin;

public class AlwaysLoad extends JavaPlugin {

  private static AlwaysLoad plugin;

  public static AlwaysLoad get() {
    return plugin;
  }

  @Override
  public void onEnable() {
    plugin = this;
    AlwaysMatchPreloader.create();
  }
}
