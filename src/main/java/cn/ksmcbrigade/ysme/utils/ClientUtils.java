package cn.ksmcbrigade.ysme.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {

    public static Minecraft MC = Minecraft.getInstance();

    public static void send(String str,boolean value){
        if(MC.player==null) return;
        MC.player.sendSystemMessage(Component.literal(str).append(": ").append(String.valueOf(value)));
    }

    public static void sendNoFall(){
        if(MC.getConnection()==null) return;
        MC.getConnection().send(new ServerboundMovePlayerPacket.StatusOnly(true));
    }

    public static void sendAirJump(){
        if(MC.player==null) return;
        MC.player.jumpFromGround();
    }

    public static void sendCriticals(){
        if(MC.player==null) return;
        Vec3 pos = MC.player.getPosition(0);
        sendPos(pos.x,pos.y+0.0625D,pos.z,true);
        sendPos(pos.x,pos.y,pos.z,false);
        sendPos(pos.x,pos.y+1.1E-5D,pos.z,false);
        sendPos(pos.x,pos.y,pos.z,false);
    }

    public static void sendKillaura(){
        if(MC.player==null) return;
        if(MC.level==null) return;
        if(MC.gameMode==null) return;
        MC.level.getEntitiesOfClass(LivingEntity.class,new AABB(MC.player.position(),MC.player.position()).inflate(5D)).stream()
                .filter(e -> e!=MC.player)
                .filter(e -> MC.player.canAttack(e))
                .filter(Entity::isAttackable)
                .toList()
                .forEach(e -> MC.gameMode.attack(MC.player,e));
    }

    public static void sendCircle(){
        if(MC.player==null) return;
        if(MC.getConnection()==null) return;
        Random random = new Random();
        MC.getConnection().send(new ServerboundMovePlayerPacket.Rot(random.nextFloat(-180F,180F),random.nextFloat(-180F,180F),MC.player.onGround()));
    }

    public static void sendPos(double x, double y, double z, boolean onGround)
    {
        if(MC.getConnection()==null) return;
        MC.getConnection().send(new ServerboundMovePlayerPacket.Pos(x, y, z, onGround));
    }
}
