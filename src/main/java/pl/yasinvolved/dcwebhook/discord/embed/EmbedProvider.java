package pl.yasinvolved.dcwebhook.discord.embed;

import com.google.gson.annotations.Expose;

public class EmbedProvider {
    @Expose private String name;
    @Expose private String url;

    public EmbedProvider(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
