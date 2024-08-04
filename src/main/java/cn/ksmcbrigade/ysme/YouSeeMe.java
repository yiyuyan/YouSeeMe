package cn.ksmcbrigade.ysme;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(YouSeeMe.MODID)
public class YouSeeMe {

    public static final String MODID = "ysme";

    public YouSeeMe() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
