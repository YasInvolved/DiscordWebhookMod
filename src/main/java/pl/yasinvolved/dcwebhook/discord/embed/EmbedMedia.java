package pl.yasinvolved.dcwebhook.discord.embed;

import com.google.gson.annotations.Expose;

public class EmbedMedia {
    @Expose private String url;
    @Expose private String proxy_url;
    @Expose private int height;
    @Expose private int width;

    public EmbedMedia(String url, String proxy_url, int height, int width) {
        this.url = url;
        this.proxy_url = proxy_url;
        this.height = height;
        this.width = width;
    }
}
