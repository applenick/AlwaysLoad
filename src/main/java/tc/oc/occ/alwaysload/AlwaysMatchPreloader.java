package tc.oc.occ.alwaysload;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import tc.oc.pgm.api.PGM;

/**
 * Credit to
 * https://github.com/bolt-rip/ingame/blob/master/src/main/java/rip/bolt/ingame/ranked/MatchPreloader.java
 * *
 */
public class AlwaysMatchPreloader implements Listener {

  private final ReentrantLock lock;

  private AlwaysMatchPreloader() {
    this.lock = new ReentrantLock();
    Bukkit.getScheduler().runTaskAsynchronously(AlwaysLoad.get(), this::createMatch);
  }

  public static void create() {
    Bukkit.getPluginManager().registerEvents(new AlwaysMatchPreloader(), AlwaysLoad.get());
  }

  private void createMatch() {
    lock.lock();

    try {
      if (!PGM.get().getMatchManager().getMatches().hasNext())
        PGM.get().getMatchManager().createMatch(null).get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    } finally {
      HandlerList.unregisterAll(this);
      lock.unlock();
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onPrePlayerLoginLow(final AsyncPlayerPreLoginEvent event) {
    lock.lock();
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPrePlayerLoginHigh(final AsyncPlayerPreLoginEvent event) {
    lock.unlock();
  }
}
