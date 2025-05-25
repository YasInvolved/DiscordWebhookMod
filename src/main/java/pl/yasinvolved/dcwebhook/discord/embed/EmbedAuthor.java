package pl.yasinvolved.dcwebhook.discord.embed;

import com.google.gson.annotations.Expose;

public class EmbedAuthor {
    @Expose private String name;
    @Expose private String url;
    @Expose private String icon_url;
    @Expose private String proxy_icon_url;

    public EmbedAuthor(String name, String url, String icon_url, String proxy_icon_url) {
        this.name = name;
        this.url = url;
        this.icon_url = icon_url;
        this.proxy_icon_url = proxy_icon_url;
    }
}
