package pl.yasinvolved.dcwebhook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ModConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("discordwebhook.json");
    private static final File CONFIG_FILE = CONFIG_PATH.toFile();

    private static ModConfig config;

    public static void load() {        
        if (!CONFIG_FILE.exists()) {
            ModConfigManager.config = new ModConfig("", false, "", false);
            save();
        } else {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ModConfigManager.config = GSON.fromJson(reader, ModConfig.class);
                DiscordWebhook.LOGGER.info("Config Loaded");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JsonIOException e) {
                e.printStackTrace();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }

        if (config == null) {
            throw new IllegalStateException("BUG: Config is null after initialization");
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModConfig getConfig() {
        if (config == null)
            load();

        return config;
    }
}
