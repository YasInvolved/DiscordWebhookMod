package pl.yasinvolved.dcwebhook;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import pl.yasinvolved.dcwebhook.discord.embed.Embed;
import pl.yasinvolved.dcwebhook.discord.embed.EmbedField;
import pl.yasinvolved.dcwebhook.discord.webhook.ExecuteParams;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

public class DiscordWebhook implements ModInitializer {
	public static final String MOD_ID = "discordwebhook";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Gson JSON_FORMATTER = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Loading config...");
		ModConfigManager.load();

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
			ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);
			ServerPlayConnectionEvents.JOIN.register(this::onPlayerJoined);
			ServerPlayConnectionEvents.DISCONNECT.register(this::onPlayerLeft);

			CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

				// set_url subcommand of config subcommand
				LiteralArgumentBuilder<ServerCommandSource> setUrlSubcommand = CommandManager.literal("set_url")
					.then(CommandManager.argument("url", StringArgumentType.greedyString()))
					.executes(this::setUrlSubcommand);
				
				// toggle subcommand of config subcommand
				LiteralArgumentBuilder<ServerCommandSource> toggleSubcommand = CommandManager.literal("toggle")
					.executes(this::toggleSubcommand);

				// config subcommand
				LiteralArgumentBuilder<ServerCommandSource> configSubcommand = CommandManager.literal("config")
					.then(setUrlSubcommand)
					.then(toggleSubcommand);

				LiteralArgumentBuilder<ServerCommandSource> modCommand = CommandManager.literal(MOD_ID)
					.requires(source -> source.hasPermissionLevel(4))
					.then(configSubcommand);

				dispatcher.register(modCommand);
				LOGGER.info("Registered all commands");
			});
		}
	}

	private void onServerStarted(MinecraftServer server) {
		Embed embed = new Embed();
		embed.setTitle("Server is running");
		embed.setColor(0x00ff00);

		embed.addField(new EmbedField("Server Name", server.getName(), true));
		embed.addField(new EmbedField("Server IP", server.getServerIp(), true));
		embed.addField(new EmbedField("Message of the day", server.getServerMotd(), false));
		embed.addField(new EmbedField("Gamemode", server.getDefaultGameMode().toString(), false));

		ExecuteParams params = new ExecuteParams();
		params.setUsername("Server Lifecycle Notifier - " + server.getName());
		params.addEmbed(embed);

		executeWebhook(params);
	}

	private void onServerStopped(MinecraftServer server) {
		Embed embed = new Embed();
		embed.setTitle("Server has stopped");
		embed.setColor(0xff0000);
		
		embed.addField(new EmbedField("Server Name", server.getName(), true));
		embed.addField(new EmbedField("Server IP", server.getServerIp(), true));
		embed.addField(new EmbedField("Message of the day", server.getServerMotd(), false));
		embed.addField(new EmbedField("Gamemode", server.getDefaultGameMode().toString(), false));

		ExecuteParams params = new ExecuteParams();
		params.setUsername("Server Lifecycle Notifier - " + server.getName());
		params.addEmbed(embed);

		executeWebhook(params);

		ModConfigManager.save();
	}

	private void onPlayerJoined(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		ServerPlayerEntity player = handler.player;
		String playerName = player.getDisplayName().getString();

		Embed embed = new Embed();
		embed.setTitle(playerName + " has joined the server");
		embed.setColor(0x00ff00);
		
		embed.addField(new EmbedField("Player Name", playerName, false));
		embed.addField(new EmbedField("X", String.valueOf(player.getX()), true));
		embed.addField(new EmbedField("Y", String.valueOf(player.getY()), true));
		embed.addField(new EmbedField("Z", String.valueOf(player.getZ()), true));

		ExecuteParams params = new ExecuteParams();
		params.setUsername("Server Networking Notifier - " + server.getName());
		params.addEmbed(embed);

		executeWebhook(params);
	}

	private void onPlayerLeft(ServerPlayNetworkHandler handler, MinecraftServer server) {
		ServerPlayerEntity player = handler.player;
		String playerName = player.getDisplayName().getString();

		Embed embed = new Embed();
		embed.setTitle(playerName + " has left the server");
		embed.setColor(0xff0000);
		
		embed.addField(new EmbedField("Player Name", playerName, false));
		embed.addField(new EmbedField("X", String.valueOf(player.getX()), true));
		embed.addField(new EmbedField("Y", String.valueOf(player.getY()), true));
		embed.addField(new EmbedField("Z", String.valueOf(player.getZ()), true));

		ExecuteParams params = new ExecuteParams();
		params.setUsername("Server Networking Notifier - " + server.getName());
		params.addEmbed(embed);

		executeWebhook(params);
	}

	private int setUrlSubcommand(CommandContext<ServerCommandSource> context) {
		String urlString = StringArgumentType.getString(context, "url");

		StringBuilder sb = new StringBuilder("Set webhook URL to: ");
		sb.append(urlString);
		context.getSource().sendFeedback(() -> Text.literal(sb.toString()), false);
		return 1;
	}

	private int toggleSubcommand(CommandContext<ServerCommandSource> context) {
		boolean current = ModConfigManager.getConfig().toggleEnabled();

		if (current) {
			context.getSource().sendFeedback(() -> Text.literal("Webhook is now enabled"), false);
		} else {
			context.getSource().sendFeedback(() -> Text.literal("Webhook is now disabled"), false);
		}

		return 1;
	}

	private void executeWebhook(ExecuteParams params) {
		ModConfig config = ModConfigManager.getConfig();

		if (config.isEnabled()) {
			HttpRequest request = HttpRequest.newBuilder(URI.create(config.getWebhookUrl()))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(JSON_FORMATTER.toJson(params)))
				.build();

			try {
				HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
				LOGGER.info("Webhook sent with status: " + response.statusCode());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				LOGGER.warn("Webhook message has been interrupted");
				e.printStackTrace();
			}
		}
	}
}