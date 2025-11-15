package jmeow.selectiveofflinemode;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jmeow.selectiveofflinemode.config.SelectiveOfflineModeConfig.readConfig;
import static net.minecraft.server.command.CommandManager.argument;

public class SelectiveOfflineMode implements ModInitializer {
    public static final String MOD_ID = "selective-offline-mode";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Selective Offline Mode starting");

        // read config file and load names
        NameExpiry.initNames(readConfig());

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment)
                -> dispatcher.register(CommandManager.literal("allowplayer")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("player", StringArgumentType.word())
                        .executes(context -> {
                            String playerName = StringArgumentType.getString(context, "player");
                            LOGGER.info("Allowing player {} to join in the next 60 seconds", playerName);
                            NameExpiry.addName(playerName, 60L);
                            context.getSource().sendFeedback(() -> Text.literal("Gave player \"" + playerName + "\" permission to join in the next 60 seconds. They may continue to stay on the server after joining."), true);
                            return 1;
                        })
                        .then(argument("duration", StringArgumentType.word())
                                .suggests(RealTimeArgument::getSuggestions)
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    String duration = StringArgumentType.getString(context, "duration");
                                    Long seconds = RealTimeArgument.parse(duration);
                                    LOGGER.info("Allowing player {} to join in the next {} seconds", playerName, seconds);
                                    NameExpiry.addName(playerName, seconds);
                                    context.getSource().sendFeedback(
                                            () -> Text.literal("Gave player \"" + playerName + "\" permission to join for the next " + seconds + " seconds.  They may continue to stay on the server after joining."),
                                            true
                                    );
                                    return 1;
                                }))
                ))
        );
    }
}