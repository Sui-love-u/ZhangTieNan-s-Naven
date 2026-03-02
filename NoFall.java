package com.heypixel.heypixelmod;

import com.heypixel.heypixelmod.Rn;
import com.heypixel.heypixelmod.Module;
import com.heypixel.heypixelmod.ModuleInfo;
import com.heypixel.heypixelmod.Category;
import com.heypixel.heypixelmod.events.MotionEvent;
import com.heypixel.heypixelmod.events.PacketEvent;
import net.minecraft.client.Minecraft;

@ModuleInfo(name = "NoFall", category = Category.MOVEMENT, description = "Prevent fall damage")
public class NoFall extends Module {

    private boolean isSpoofing;
    private int fallTicks;

    @Override
    public void onEnable(Object[] args) {
        this.isSpoofing = false;
        this.fallTicks = 0;
        super.onEnable(new Object[0]);
    }

    @SubscribeEvent
    public void onMotion(MotionEvent event) {
        this.setSuffix("NoFall");

        if (event.getState() == Rn.PRE) {
            if (!event.isOnGround()) {
                if (Minecraft.getInstance().player.fallDistance > 3.0f) {
                    this.fallTicks++;
                }
            }

            if (!event.isOnGround() && this.fallTicks > 0) {
                if (!this.isSpoofing) {
                    event.setY(event.getY() + 0.1);
                    this.isSpoofing = true;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        if (this.isSpoofing) {
            event.setOnGround(true);
            this.fallTicks = 0;
            this.isSpoofing = false;
        }
    }
}