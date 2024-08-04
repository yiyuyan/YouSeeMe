package cn.ksmcbrigade.ysme.client;

import cn.ksmcbrigade.ysme.YouSeeMe;
import cn.ksmcbrigade.ysme.utils.ClientUtils;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = YouSeeMe.MODID,value = Dist.CLIENT)
public class YouSeeMeClient {
    public static final KeyMapping rnac = new KeyMapping("key.ysme.rnac", GLFW.GLFW_KEY_F12,KeyMapping.CATEGORY_GAMEPLAY);
    public static final KeyMapping circle = new KeyMapping("key.ysme.cle", GLFW.GLFW_KEY_F7,KeyMapping.CATEGORY_GAMEPLAY);

    public static boolean rnacEnabled = false;
    public static boolean circleEnabled = false;

    @SubscribeEvent
    public static void keyRegister(RegisterKeyMappingsEvent event){
        event.register(rnac);
        event.register(circle);
    }

    @SubscribeEvent
    public static void inputOn(InputEvent.Key event){
        if(rnac.isDown()){
            rnacEnabled=!rnacEnabled;
            ClientUtils.send("YouSeeMe",rnacEnabled);
        }
        if(circle.isDown()){
            circleEnabled=!circleEnabled;
            ClientUtils.send("RandomRot",circleEnabled);
        }
    }
}
