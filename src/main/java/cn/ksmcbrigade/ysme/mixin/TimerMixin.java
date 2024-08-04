package cn.ksmcbrigade.ysme.mixin;

import cn.ksmcbrigade.ysme.client.ClientEventsHandler;
import cn.ksmcbrigade.ysme.client.YouSeeMeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Timer.class)
public class TimerMixin {
    @Mutable
    @Shadow @Final private float msPerTick;

    @Inject(method = "advanceTime",at = @At("HEAD"))
    public void time(long p_92526_, CallbackInfoReturnable<Integer> cir){
        if(msPerTick==(1000.0F / (20.0F * 1.65F)) && (!ClientEventsHandler.onServer() || !YouSeeMeClient.rnacEnabled)) msPerTick = 1000.0F / 20.0F;
        if(!ClientEventsHandler.onServer()) return;
        if(!YouSeeMeClient.rnacEnabled) return;
        Minecraft MC = Minecraft.getInstance();
        if(MC.options.keyUp.isDown() || MC.options.keyDown.isDown() || MC.options.keyLeft.isDown() || MC.options.keyRight.isDown() || MC.options.keySprint.isDown()){
            msPerTick = 1000.0F / (20.0F * 1.65F);
        }
        else{
            msPerTick = 1000.0F / 20.0F;
        }
    }
}
