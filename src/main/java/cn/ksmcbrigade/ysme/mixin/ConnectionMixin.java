package cn.ksmcbrigade.ysme.mixin;

import cn.ksmcbrigade.ysme.client.ClientEventsHandler;
import cn.ksmcbrigade.ysme.client.YouSeeMeClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Connection.class)
public class ConnectionMixin {

    @ModifyVariable(method = "doSendPacket",at = @At("HEAD"), argsOnly = true)
    public Packet<?> doSendPacket(Packet<?> p_243260_){
        if(!ClientEventsHandler.onServer()) return p_243260_;
        if(!YouSeeMeClient.rnacEnabled) return p_243260_;

        if(!(p_243260_ instanceof ServerboundMovePlayerPacket last)) return p_243260_;
        Minecraft MC = Minecraft.getInstance();
        if(MC.player==null) return p_243260_;
        if(!MC.player.onGround() || MC.player.fallDistance>0.5F) return p_243260_;
        if(MC.gameMode==null) return p_243260_;
        if(MC.gameMode.isDestroying()) return p_243260_;

        Vec3 pos = new Vec3(last.getX(0),last.getY(0),last.getZ(0));
        Vec2 rot = new Vec2(last.getXRot(0),last.getYRot(0));

        Packet<?> packet;
        if(last.hasPosition()){
            if(last.hasRotation()){
                packet = new ServerboundMovePlayerPacket.PosRot(pos.x,pos.y,pos.z,rot.y,rot.x,false);
            }
            else{
                packet = new ServerboundMovePlayerPacket.Pos(pos.x,pos.y,pos.z,false);
            }
        }
        else if(last.hasRotation()){
            packet = new ServerboundMovePlayerPacket.Rot(rot.y,rot.x,false);
        }
        else{
            packet = new ServerboundMovePlayerPacket.StatusOnly(false);
        }

        return packet;
    }
}
