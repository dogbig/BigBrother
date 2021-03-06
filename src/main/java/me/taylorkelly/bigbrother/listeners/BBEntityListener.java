package me.taylorkelly.bigbrother.listeners;

import me.taylorkelly.bigbrother.ActionProvider;
import me.taylorkelly.bigbrother.BigBrother;
import me.taylorkelly.bigbrother.datablock.explosions.CreeperExplosion;
import me.taylorkelly.bigbrother.datablock.explosions.MiscExplosion;
import me.taylorkelly.bigbrother.datablock.explosions.TNTExplosion;
import me.taylorkelly.bigbrother.datablock.explosions.TNTLogger;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class BBEntityListener extends EntityListener {
    //private BigBrother plugin; // Not used - N3X
    //private List<World> worlds; // Not used - N3X
    
    public BBEntityListener(BigBrother bigBrother) {
        //this.plugin = bigBrother;
        //this.worlds = plugin.getServer().getWorlds();
    }
    
    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        // Err... why is this null when it's a TNT?
        if (!event.isCancelled()) {
            if (event.getEntity() == null) {
                if (!ActionProvider.isDisabled(TNTExplosion.class)) {
                    TNTLogger.createTNTDataBlock(event.blockList(), event.getLocation());
                }
            } else if (event.getEntity() instanceof LivingEntity) {
                if (!ActionProvider.isDisabled(CreeperExplosion.class)) {
                    CreeperExplosion.create(event.getEntity().getLocation(), event.blockList(), event.getLocation().getWorld().getName());
                }
            } else if (!ActionProvider.isDisabled(MiscExplosion.class)) {
                MiscExplosion.create(event.getEntity().getLocation(), event.blockList(), event.getLocation().getWorld().getName());
            }
        }
    }
}
