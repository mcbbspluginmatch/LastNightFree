package lvhaoxuan.last.night.listener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import lvhaoxuan.last.night.LastNight;
import lvhaoxuan.last.night.forge.LastNightGunForgeManagaer;
import lvhaoxuan.last.night.forge.LastNightGunRecipe;
import lvhaoxuan.last.night.forge.LastNightGunRecipeInventory;
import lvhaoxuan.last.night.forge.LastNightGunRecipeMakerInventory;
import lvhaoxuan.last.night.forge.RecipeItem;
import lvhaoxuan.last.night.gun.*;
import lvhaoxuan.last.night.mail.MailManager;
import lvhaoxuan.last.night.util.NBT;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MainListener implements Listener {

    public static HashMap<UUID, Boolean> enlargeMap = new HashMap<>();
    public static HashMap<UUID, PotionEffect> enlargePotionMap = new HashMap<>();
    public static HashMap<UUID, LastNightGunRecipe> recipeMap = new HashMap<>();
    public static HashMap<UUID, Integer> runnableMap = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerInteractEvent(PlayerInteractEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        NBT nbt = new NBT(item);
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (nbt.getInt("明日之后") == LastNight.GUN) {
                e.setCancelled(true);
                LastNightItemGun ibg = new LastNightItemGun(item);
                if (ibg.enlarge != 0) {
                    if (enlargeMap.get(p.getUniqueId()) != null && enlargeMap.get(p.getUniqueId()) == true) {
                        p.removePotionEffect(PotionEffectType.SLOW);
                        if (enlargePotionMap.get(p.getUniqueId()) != null) {
                            p.addPotionEffect(enlargePotionMap.get(p.getUniqueId()));
                            enlargePotionMap.put(p.getUniqueId(), null);
                        }
                        enlargeMap.put(p.getUniqueId(), false);
                    } else if (enlargeMap.get(p.getUniqueId()) == null || enlargeMap.get(p.getUniqueId()) == false) {
                        enlargePotionMap.put(p.getUniqueId(), p.getPotionEffect(PotionEffectType.SLOW));
                        p.removePotionEffect(PotionEffectType.SLOW);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, ibg.enlarge));
                        enlargeMap.put(p.getUniqueId(), true);
                    }
                }
            }
        } else if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!(e.hasBlock() && (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.ENDER_CHEST))) {
                switch (nbt.getInt("明日之后")) {
                    case LastNight.GUN:
                        e.setCancelled(true);
                        FireHandle.fire(p, item);
                        BukkitRunnable br = new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                if (i == 5) {
                                    cancel();
                                    return;
                                }
                                FireHandle.fire(p, p.getInventory().getItemInMainHand());
                                i++;
                            }
                        };
                        br.runTaskTimer(LastNight.plugin, 1, 1);
                        runnableMap.put(p.getUniqueId(), br.getTaskId());
                        break;
                    case LastNight.BULLET_CHEST:
                        e.setCancelled(true);
                        if (e.getClickedBlock() != null) {
                            if (e.getClickedBlock().getType() != Material.AIR) {
                                Location loc = e.getClickedBlock().getLocation().add(0, -0.3, 0);
                                loc.setPitch(p.getLocation().getPitch());
                                loc.setYaw(p.getLocation().getYaw() - 180);
                                ArmorStand as = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                                as.setCustomName(nbt.getString("弹药箱"));
                                as.setVisible(false);
                                as.setGravity(false);
                                as.setHelmet(new ItemStack(Material.CHEST));
                                as.setCustomNameVisible(true);
                                if (item.getAmount() == 1) {
                                    item.setType(Material.AIR);
                                } else {
                                    item.setAmount(item.getAmount() - 1);
                                }
                                p.getInventory().setItemInMainHand(item);
                            }
                        }
                        break;
                    case LastNight.RECIPE:
                        e.setCancelled(true);
                        LastNightGunRecipe lngr = new LastNightGunRecipe(item);
                        LastNightGunRecipeInventory lngri = new LastNightGunRecipeInventory(lngr);
                        lngri.open(p);
                        recipeMap.put(p.getUniqueId(), lngr);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        double damage = e.getDamage();
        if (((damager instanceof Projectile)) && (e.getEntity() instanceof Damageable)) {
            Projectile p = (Projectile) damager;
            if (!p.getMetadata("lastnight_damage").isEmpty() && p.getMetadata("lastnight_damage").get(0) != null) {
                if (!p.getMetadata("lastnight_range").isEmpty() && p.getMetadata("lastnight_range").get(0).asDouble() >= e.getEntity().getLocation().distance(((LivingEntity) p.getShooter()).getLocation())) {
                    damage = p.getMetadata("lastnight_damage").get(0).asDouble();
                    ((LivingEntity) e.getEntity()).setNoDamageTicks(0);
                } else {
                    damage = 1;
                }
                p.remove();
            }
        }
        e.setDamage(damage);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e) {
        Entity en = e.getRightClicked();
        if (en instanceof ArmorStand) {
            if (en.getCustomName() != null && !((ArmorStand) en).isVisible()) {
                if (e.getClickedPosition().getY() > 1.4 && BulletChestManager.isBulletChest(((ArmorStand) en))) {
                    BulletChestInventory bci = new BulletChestInventory(new BulletChest((ArmorStand) en));
                    bci.open(e.getPlayer());
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void ProjectileHitEvent(ProjectileHitEvent e) {
        Projectile p = (Projectile) e.getEntity();
        if (p.getShooter() instanceof LivingEntity) {
            if (!p.getMetadata("lastnight_damageType").isEmpty() && p.getMetadata("lastnight_damageType").get(0) != null) {
                if (!p.getMetadata("lastnight_explosionRange").isEmpty() && !p.getMetadata("lastnight_range").isEmpty() && p.getMetadata("lastnight_range").get(0).asDouble() >= p.getLocation().distance(((LivingEntity) p.getShooter()).getLocation())) {
                    createExplosion(p.getLocation(), p.getMetadata("lastnight_explosionRange").get(0).asDouble());
                    for (Entity en : p.getNearbyEntities(p.getMetadata("lastnight_explosionRange").get(0).asDouble(), p.getMetadata("lastnight_explosionRange").get(0).asDouble(), p.getMetadata("lastnight_explosionRange").get(0).asDouble())) {
                        if (en instanceof LivingEntity && !en.getUniqueId().equals(((LivingEntity) p.getShooter()).getUniqueId())) {
                            ((LivingEntity) en).damage(p.getMetadata("lastnight_explosionDamage").get(0).asDouble(), (LivingEntity) p.getShooter());
                        }
                    }
                }
                p.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerItemHeldEvent(PlayerItemHeldEvent e) {
        if (FireHandle.lastReplaceMap.get(e.getPlayer().getName()) != null) {
            Bukkit.getScheduler().cancelTask(FireHandle.lastReplaceMap.get(e.getPlayer().getName()));
            FireHandle.lastReplaceMap.put(e.getPlayer().getName(), null);
        }
        if (enlargeMap.get(e.getPlayer().getUniqueId()) != null && enlargeMap.get(e.getPlayer().getUniqueId()) == true) {
            e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            enlargeMap.put(e.getPlayer().getUniqueId(), false);
        }
        if (runnableMap.containsKey(e.getPlayer().getUniqueId())) {
            Bukkit.getScheduler().cancelTask(runnableMap.get(e.getPlayer().getUniqueId()));
        }
    }

    public void createExplosion(Location loc, double range) {
        if (range != 0) {
            loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0);
            loc.getWorld().playSound(loc, Sound.BLOCK_END_GATEWAY_SPAWN, 2, 1);
        }
    }

    public static double getRandom(double min, double max) {
        if (min == max) {
            return min;
        }
        return Math.random() * (max - min) + min;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void InventoryClickEvent(InventoryClickEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Inventory inv = e.getInventory();
        if (inv == null) {
            return;
        }
        if (e.getRawSlot() < 0) {
            return;
        }
        switch (inv.getName()) {
            case LastNight.BULLET_CHEST_INVENTORY:
                if (e.getRawSlot() <= 8) {
                    int slot = e.getRawSlot();
                    if ((slot <= 8 && slot >= 0) && slot != 3 && slot != 5 && slot != 8) {
                        e.setCancelled(true);
                    } else if (slot == 5) {
                        if (inv.getItem(3) == null) {
                            e.setCancelled(true);
                            return;
                        }
                        NBT nbt = new NBT(inv.getItem(3));
                        if (nbt.getInt("明日之后") == LastNight.GUN) {
                            nbt = new NBT(inv.getItem(1));
                            ArmorStand as = (ArmorStand) getEntity(UUID.fromString(nbt.getString("id")));
                            if (as != null) {
                                BulletChest bc = new BulletChest((ArmorStand) as);
                                LastNightItemGun ibg = new LastNightItemGun(inv.getItem(3));
                                ibg.repalce(bc);
                                inv.setItem(3, ibg.toItemStack());
                            }
                        }
                        e.setCancelled(true);
                    } else if (slot == 8) {
                        NBT nbt = new NBT(inv.getItem(1));
                        ArmorStand as = (ArmorStand) getEntity(UUID.fromString(nbt.getString("id")));
                        if (as != null) {
                            BulletChest bc = new BulletChest((ArmorStand) as);
                            e.getWhoClicked().getInventory().addItem(bc.toItemStack());
                            e.getWhoClicked().closeInventory();
                            e.setCancelled(true);
                        }
                    }
                }
                break;
            case LastNight.RECIPE_MAKER_INVENTORY:
                if (e.getRawSlot() <= 8) {
                    int slot = e.getRawSlot();
                    if ((slot <= 8 && slot >= 0) && slot != 3 && slot != 5) {
                        e.setCancelled(true);
                    } else if (slot == 5) {
                        if (inv.getItem(3) == null) {
                            e.setCancelled(true);
                            return;
                        }
                        NBT nbt = new NBT(inv.getItem(3));
                        if (nbt.getInt("明日之后") == LastNight.RECIPE) {
                            LastNightGunRecipe lngr = new LastNightGunRecipe(LastNight.guns.get(nbt.getString("武器种类")), nbt.getInt("剩余使用次数"));
                            inv.setItem(3, lngr.toItemStack());
                        }
                        e.setCancelled(true);
                    }
                }
                break;
            case LastNight.MAKER:
                int slot = e.getRawSlot();
                if (slot == e.getWhoClicked().getInventory().getHeldItemSlot() + 81) {
                    e.setCancelled(true);
                }
                break;
            case LastNight.MARKET_GUN:
                e.setCancelled(true);
                break;
            case LastNight.MARKET_ITEM:
                e.setCancelled(true);
                break;
            case LastNight.MAKER_INVENTORY:
                slot = e.getRawSlot();
                if (e.getClick() == ClickType.SHIFT_RIGHT) {
                    Player p = (Player) e.getWhoClicked();
                    if (!LastNightGunForgeManagaer.userMap.containsKey(p.getName()) || LastNightGunForgeManagaer.userMap.get(p.getName()).isEmpty()) {
                        LastNightGunForgeManagaer.userMap.put(p.getName(), new ArrayList<>());
                    }
                    if (slot >= 0 && slot < LastNightGunForgeManagaer.userMap.get(p.getName()).size()) {
                        LastNightGunRecipe lngr = LastNightGunForgeManagaer.userMap.get(p.getName()).get(slot);
                        for (RecipeItem ri : lngr.sources) {
                            p.getInventory().addItem(ri.toItemStack());
                        }
                        LastNightGunForgeManagaer.userMap.get(p.getName()).remove(slot);
                        LastNightGunRecipeMakerInventory lngrmi = new LastNightGunRecipeMakerInventory();
                        p.closeInventory();
                        lngrmi.open(p);
                        p.sendMessage(LastNight.FORGE_CANCEL_MESSAGE);
                    }
                }
                e.setCancelled(true);
                break;
            case LastNight.MAIL_INVENTORY:
                if (e.getRawSlot() > 53) {
                    return;
                }
                Player p = (Player) e.getWhoClicked();
                MailManager.playerDatas.get(p.getName()).setItems(e.getClickedInventory().getContents());
                break;
            default:
                break;
        }
    }

    public LivingEntity getEntity(UUID id) {
        for (World w : Bukkit.getWorlds()) {
            for (LivingEntity e : w.getLivingEntities()) {
                if (e.getUniqueId().equals(id)) {
                    return e;
                }
            }
        }
        return null;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getPlayer();
        if (inv.getName() != null) {
            switch (inv.getName()) {
                case LastNight.BULLET_CHEST_INVENTORY:
                    if (inv.getItem(3) != null) {
                        if (inv.getItem(3) != null) {
                            p.getInventory().addItem(inv.getItem(3));
                        }
                        p.sendMessage(LastNight.BACK_MESSAGE);
                    }
                    break;
                case LastNight.TREASURE_CHEST_INVENTORY: {
                    int i = 0;
                    for (ItemStack item : inv) {
                        if (item != null) {
                            if (item != null) { // 这是什么意思 —— 754503921
                                p.getInventory().addItem(item);
                                i++;
                            }
                        }
                    }
                    if (i > 0) {
                        p.sendMessage(LastNight.BACK_MESSAGE);
                    }
                    break;
                }
                case LastNight.MAKER: {
                    int count = 0;
                    if (LastNightGunForgeManagaer.userMap.containsKey(p.getName())) {
                        count = MailManager.playerDatas.get(p.getName()).items.size() + LastNightGunForgeManagaer.userMap.get(p.getName()).size();
                    } else {
                        count = MailManager.playerDatas.get(p.getName()).items.size();
                    }
                    if (count < 55) {
                        LastNightGunRecipe lngr = recipeMap.get(p.getUniqueId());
                        if (lngr != null) {
                            if (!LastNightGunForgeManagaer.userMap.containsKey(p.getName()) || LastNightGunForgeManagaer.userMap.get(p.getName()).isEmpty()) {
                                LastNightGunForgeManagaer.userMap.put(p.getName(), new ArrayList<>());
                            }
                            if (lngr.canForge(inv) && LastNightGunForgeManagaer.userMap.size() < 6) {
                                lngr.useRecipe();
                                LastNightGunForgeManagaer.userMap.get(p.getName()).add(lngr);
                                p.getInventory().setItemInMainHand(lngr.toItemStack());
                                p.sendMessage("§b锻造中");
                            }
                        }
                        int i = 0;
                        for (ItemStack item : inv) {
                            if (item != null && item.getType() != Material.AIR) {
                                p.getInventory().addItem(item);
                                i++;
                            }
                        }
                        if (i > 0) {
                            p.sendMessage(LastNight.BACK_MESSAGE);
                        }
                    } else {
                        p.sendMessage(LastNight.MAIL_FULL_MESSAGE);
                        int i = 0;
                        for (ItemStack item : inv) {
                            if (item != null && item.getType() != Material.AIR) {
                                p.getInventory().addItem(item);
                                i++;
                            }
                        }
                        if (i > 0) {
                            p.sendMessage(LastNight.BACK_MESSAGE);
                        }
                    }
                    break;
                }
                case LastNight.RECIPE_MAKER_INVENTORY:
                    if (inv.getItem(3) != null) {
                        if (inv.getItem(3) != null) {
                            p.getInventory().addItem(inv.getItem(3));
                        }
                        p.sendMessage(LastNight.BACK_MESSAGE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerDeathEvent(PlayerDeathEvent e) {
        e.setKeepInventory(true);
        Player p = e.getEntity();
        if (LastNight.limitDrop) {
            for (ItemStack item : p.getInventory().getContents()) {
                if (item != null) {
                    NBT nbt = new NBT(item);
                    if (nbt.getInt("明日之后") != LastNight.GUN && nbt.getInt("明日之后") != LastNight.RECIPE) {
                        inventoryRemoveItem(p.getInventory(), item);
                        p.getWorld().dropItem(p.getLocation(), item);
                    } else if (nbt.getInt("明日之后") == LastNight.GUN) {
                        if (nbt.getDouble("耐久值") >= 10) {
                            nbt.setDouble("耐久值", nbt.getDouble("耐久值") - 10);
                            inventoryRemoveItem(p.getInventory(), item);
                            p.getInventory().addItem(new LastNightItemGun(nbt.toItemStack()).toItemStack());
                        } else {
                            inventoryRemoveItem(p.getInventory(), item);
                        }
                    }
                }
            }
        }
    }

    public void inventoryRemoveItem(Inventory inv, ItemStack item) {
        int num = item.getAmount();
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR) {
                if (inv.getItem(i).isSimilar(item)) {
                    if (num > 0) {
                        int num2 = inv.getItem(i).getAmount();
                        if (inv.getItem(i).getAmount() - num > 0) {
                            inv.getItem(i).setAmount(inv.getItem(i).getAmount() - num);
                        } else {
                            inv.setItem(i, null);
                        }
                        num -= num2;
                    }
                }
            }
        }
    }
}
