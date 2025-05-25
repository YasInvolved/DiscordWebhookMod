package pl.yasinvolved.dcwebhook;

import com.google.gson.annotations.Expose;

public class ModConfig {
    @Expose private String webhookUrl;
    @Expose private boolean enabled;
    @Expose private String pingRoleId;
    @Expose private boolean pingRole;

    public ModConfig(String webhookUrl, boolean enabled, String pingRoleId, boolean pingRole) {
        this.webhookUrl = webhookUrl;
        this.enabled = enabled;
        this.pingRoleId = pingRoleId;
        this.pingRole = pingRole;
    }

    public String getPingRoleId() {
        return pingRoleId;
    }

    public boolean isPingRole() {
        return pingRole;
    }

    public boolean togglePingRole() {
        return pingRole;
    }

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
}
