package sh.talonfloof.deluge.client.config;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import sh.talonfloof.deluge.Deluge;

public class DelugeClientConfig extends Config {
    public static class IrisSection extends ConfigSection {
        @Comment("Darken textures on overcast cloud events when an Iris Shader is enabled (for compatibility)")
        public boolean irisDarkenClouds = false;
    }

    public IrisSection iris = new IrisSection();

    public DelugeClientConfig() {
        super(Deluge.path("client"));
    }
}
