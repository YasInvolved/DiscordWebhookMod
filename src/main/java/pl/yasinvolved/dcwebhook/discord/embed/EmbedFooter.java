package pl.yasinvolved.dcwebhook.discord.embed;

import com.google.gson.annotations.Expose;

public class EmbedFooter {
    @Expose private String text;
    @Expose private String icon_url;
    @Expose private String proxy_icon_url;

    public EmbedFooter(String text, String icon_url, String proxy_icon_url) {
        this.text = text;
        this.icon_url = icon_url;
        this.proxy_icon_url = proxy_icon_url;
    }
}
