package pl.yasinvolved.dcwebhook.discord.embed;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Embed {    
    @Expose private String              title;
    @Expose private String              type;
    @Expose private String              description;
    @Expose private String              url;
    @Expose private String              timestamp;
    @Expose private int                 color;
    @Expose private EmbedFooter         footer;
    @Expose private EmbedMedia          image;
    @Expose private EmbedMedia          thumbnail;
    @Expose private EmbedMedia          video;
    @Expose private EmbedProvider       provider;
    @Expose private EmbedAuthor         author;
    @Expose private List<EmbedField>    fields = new ArrayList<>();

    public Embed(
        String title, String description, String url, 
        String timestamp, int color, EmbedFooter footer, EmbedMedia image, 
        EmbedMedia thumbnail, EmbedMedia video, EmbedProvider provider, 
        EmbedAuthor author, List<EmbedField> fields
    ) {
        this.title = title;
        this.type = "rich";
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.color = color;
        this.footer = footer;
        this.image = image;
        this.thumbnail = thumbnail;
        this.video = video;
        this.provider = provider;
        this.author = author;
        this.fields = fields;
    }

    public Embed() {
        this.type = "rich";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getColor() {
        return color;
    }
    
    public void setColor(int color) {
        this.color = color;
    }
    
    public EmbedFooter getFooter() {
        return footer;
    }
    
    public void setFooter(EmbedFooter footer) {
        this.footer = footer;
    }
    
    public EmbedMedia getImage() {
        return image;
    }
    
    public void setImage(EmbedMedia image) {
        this.image = image;
    }
    
    public EmbedMedia getThumbnail() {
        return thumbnail;
    }
    
    public void setThumbnail(EmbedMedia thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    public EmbedMedia getVideo() {
        return video;
    }
    
    public void setVideo(EmbedMedia video) {
        this.video = video;
    }
    
    public EmbedProvider getProvider() {
        return provider;
    }
    
    public void setProvider(EmbedProvider provider) {
        this.provider = provider;
    }
    
    public EmbedAuthor getAuthor() {
        return author;
    }
    
    public void setAuthor(EmbedAuthor author) {
        this.author = author;
    }
    
    public EmbedField[] getFields() {
        return fields.toArray(new EmbedField[fields.size()]);
    }
    
    public void setFields(List<EmbedField> fields) {
        this.fields = fields;
    }

    public void addField(EmbedField field) {
        this.fields.add(field);
    }
}
