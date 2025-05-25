package pl.yasinvolved.dcwebhook.discord.webhook;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import pl.yasinvolved.dcwebhook.discord.embed.Embed;

public class ExecuteParams {
    @Expose private String content;
    @Expose private String username;
    @Expose private String avatar_url;
    @Expose private List<Embed> embeds;

    public ExecuteParams() {
        this.embeds = new ArrayList<>();
    }

    public ExecuteParams(String content, String username, String avatar_url, List<Embed> embeds) {
        this.content = content;
        this.username = username;
        this.avatar_url = avatar_url;
        this.embeds = embeds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void addEmbed(Embed embed) {
        this.embeds.add(embed);
    }
}
