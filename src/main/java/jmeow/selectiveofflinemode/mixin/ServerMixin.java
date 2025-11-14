package jmeow.selectiveofflinemode.mixin;

import jmeow.selectiveofflinemode.NameExpiry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerMixin {
	@Shadow @Final
    static Logger LOGGER;

    @Shadow
    @Nullable String profileName;

    @Redirect(method = "onHello", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isOnlineMode()Z"))
    private boolean isOnlineMode(MinecraftServer instance) {
        String username = this.profileName;
        if (NameExpiry.isAllowed(username)) {
            LOGGER.info("{} joined with permission", username);
            return false;
        }
        return true;
    }
}