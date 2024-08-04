package cn.ksmcbrigade.ysme.client;

import cn.ksmcbrigade.ysme.YouSeeMe;
import cn.ksmcbrigade.ysme.utils.ClientUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.ConnectionData;
import net.minecraftforge.network.NetworkHooks;
import org.lwjgl.glfw.GLFW;

import java.io.File;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = YouSeeMe.MODID,value = Dist.CLIENT)
public class ClientEventsHandler {

    public static int timer = 10;

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event){
        if(!onServer()) return;
        if(YouSeeMeClient.rnacEnabled){
            if(event.player.fallDistance>=3){
                ClientUtils.sendNoFall();
            }
            if(Minecraft.getInstance().options.keyJump.isDown()){
                ClientUtils.sendAirJump();
            }
            if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_R)){
                ClientUtils.sendKillaura();
            }
        }
        if(YouSeeMeClient.circleEnabled){
            if(timer!=0){
                timer--;
            }
            else{

                ClientUtils.sendCircle();

                timer = 10;
            }
        }
    }

    @SubscribeEvent
    public static void attackOn(AttackEntityEvent event){
        if(!YouSeeMeClient.rnacEnabled) return;
        if(!(event.getTarget() instanceof LivingEntity)) return;
        ClientUtils.sendCriticals();
    }

    public static boolean onServer(){
        if(new File("ced").exists()){
            return true;
        }
        Minecraft MC = Minecraft.getInstance();
        if(MC.isSingleplayer()) return true;
        if(MC.getConnection()==null) return true;
        ConnectionData context = NetworkHooks.getConnectionData(MC.getConnection().getConnection());
        if(context==null) return true;
        return context.getModList().contains(YouSeeMe.MODID);
    }
}
