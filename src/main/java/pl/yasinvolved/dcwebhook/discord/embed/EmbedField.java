package pl.yasinvolved.dcwebhook.discord.embed;

import com.google.gson.annotations.Expose;

public class EmbedField {
    @Expose private String name;
    @Expose private String value;
    @Expose private boolean inline;

    public EmbedField(String name, String value, boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

    public EmbedField() {
        
    }
}
