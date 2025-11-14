package jmeow.selectiveofflinemode.mixin;

import jmeow.selectiveofflinemode.NameExpiry;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Inject(method = "checkCanJoin", at = @At(value = "RETURN"), cancellable = true)
    private void checkCanJoin(SocketAddress address, PlayerConfigEntry profile, CallbackInfoReturnable<Text> cir) {

        if (NameExpiry.isAllowed(profile.name())) {
            cir.setReturnValue(null);
        } else {
            cir.setReturnValue(cir.getReturnValue());
        }
    }
}