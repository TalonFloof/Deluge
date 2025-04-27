package sh.talonfloof.deluge.config;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import sh.talonfloof.deluge.Deluge;

public class DelugeConfig extends Config {
    public static class WindSection extends ConfigSection {
        @Comment("Sets the chance of the wind angle randomly changing (1 in x)")
        public int windAngleChangeChance = 3000;
        @Comment("Sets the chance of the wind speed randomly changing (1 in x)")
        public int windSpeedChangeChance = 3000;
    }

    public WindSection wind = new WindSection();

    public DelugeConfig() {
        super(Deluge.path("common"));
    }


}
