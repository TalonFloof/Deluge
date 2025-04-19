package sh.talonfloof.deluge.config;

import me.fzzyhmstrs.fzzy_config.config.Config;
import sh.talonfloof.deluge.Deluge;

public class DelugeConfig extends Config {
    public DelugeConfig() {
        super(Deluge.path("common"));
    }
}
