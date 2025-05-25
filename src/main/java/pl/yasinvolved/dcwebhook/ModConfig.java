package pl.yasinvolved.dcwebhook;

import com.google.gson.annotations.Expose;

public class ModConfig {
    @Expose private String webhookUrl;
    @Expose private boolean enabled;

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String value) {
        this.webhookUrl = value;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean toggleEnabled() {
        enabled = !enabled;
        return enabled;
    } 

    public ModConfig(String webhookUrl, boolean enabled) {
        this.webhookUrl = webhookUrl;
        this.enabled = enabled;
    }
}
