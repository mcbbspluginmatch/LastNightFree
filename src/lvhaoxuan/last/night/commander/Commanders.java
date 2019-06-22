package lvhaoxuan.last.night.commander;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import lvhaoxuan.last.night.LastNight;
import static lvhaoxuan.last.night.LastNight.itemsMap;
import lvhaoxuan.last.night.forge.LastNightGunForgeManagaer;
import lvhaoxuan.last.night.forge.LastNightGunRecipe;
import lvhaoxuan.last.night.forge.LastNightGunRecipeMakerInventory;
import lvhaoxuan.last.night.forge.LastNightGunRecipeReplaceInventory;
import lvhaoxuan.last.night.gun.*;
import lvhaoxuan.last.night.item.LastNightItem;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.CommandExecutor;
import org.bukkit.Bukkit;

public class Commanders implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender.isOp()) {
            switch (args.length) {
                case 6:
                    if (args[0].equalsIgnoreCase("gun") && args[1].equalsIgnoreCase("paper")) {
                        if (!LastNight.guns.get(args[2]).sources.isEmpty()) {
                            LastNightGunRecipe im = LastNight.guns.get(args[2]).getLastNightGunRecipe(Integer.parseInt(args[3]));
                            ItemStack item = im.toItemStack();
                            item.setAmount(Integer.parseInt(args[4]));
                            Player p = Bukkit.getPlayer(args[5]);
                            p.getInventory().addItem(item);
                            sender.sendMessage("获取成功");
                        } else {
                            sender.sendMessage("不存在的图纸");
                        }
                    }
                    break;
                case 4:
                    if (args[0].equalsIgnoreCase("gun")) {
                        LastNightItemGun im = LastNight.guns.get(args[1]);
                        ItemStack item = im.toItemStack0(sender.getName());
                        item.setAmount(Integer.parseInt(args[2]));
                        Player p = Bukkit.getPlayer(args[3]);
                        p.getInventory().addItem(item);
                        sender.sendMessage("获取成功");
                    } else if (args[0].equalsIgnoreCase("getItem")) {
                        Player p = Bukkit.getPlayer(args[3]);
                        ItemStack item = LastNight.itemsMap.get(args[1]).toItemStack();
                        item.setAmount(Integer.parseInt(args[2]));
                        p.getInventory().addItem(item);
                    }
                    break;
                case 2:
                    if (args[0].equalsIgnoreCase("bc")) {
                        if (args[1].equalsIgnoreCase("list")) {
                            sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l明日之后§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
                            for (String name : BulletChestManager.chestsNameMap.keySet()) {
                                sender.sendMessage(name);
                            }
                            sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l明日之后§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
                        } else {
                            BulletChest rbc = BulletChestManager.chestsNameMap.get(args[1]).clone();
                            rbc.set(((Player) sender).getLocation());
                        }
                    } else if (args[0].equalsIgnoreCase("item")) {
                        Player p = (Player) sender;
                        LastNightItem lni = new LastNightItem();
                        lni.item = p.getInventory().getItemInMainHand();
                        itemsMap.put(args[1], lni);
                        LastNight.saveItems();
                        sender.sendMessage("添加成功");
                    }
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("openRecipeInv")) {
                        Player p = (Player) sender;
                        LastNightGunRecipeReplaceInventory lngrri = new LastNightGunRecipeReplaceInventory();
                        lngrri.open(p);
                    } else if (args[0].equalsIgnoreCase("items")) {
                        sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l明日之后§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
                        for (String name : LastNight.itemsMap.keySet()) {
                            TextComponent tc = new TextComponent(name + " - " + LastNight.itemsMap.get(name).item.getItemMeta().getDisplayName());
                            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ln getItem " + name + " 1 " + sender.getName()));
                            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("获取这个物品").create()));
                            ((Player) sender).spigot().sendMessage(tc);
                        }
                        sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l明日之后§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
                    } else if (args[0].equalsIgnoreCase("guns")) {
                        sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l明日之后§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
                        for (String name : LastNight.guns.keySet()) {
                            TextComponent tc = new TextComponent(name + " - " + LastNight.guns.get(name).name);
                            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ln gun " + name + " 1 " + sender.getName()));
                            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("获取这个武器").create()));
                            ((Player) sender).spigot().sendMessage(tc);
                        }
                        sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l明日之后§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
                    } else if (args[0].equalsIgnoreCase("recipes")) {
                        if (!LastNightGunForgeManagaer.userMap.containsKey(sender.getName())) {
                            LastNightGunForgeManagaer.userMap.put(sender.getName(), new ArrayList<>());
                        }
                        LastNightGunRecipeMakerInventory lngrmi = new LastNightGunRecipeMakerInventory();
                        lngrmi.open((Player) sender);
                    }
                    break;
                default:
                    sendDefaultMessage(sender);
                    break;
            }
        }
        return true;
    }

    public static String getTime(long ms) {
        if (ms >= 3600000) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH时mm分ss秒");
            formatter.setTimeZone(TimeZone.getTimeZone("00分00秒"));
            return formatter.format(ms);
        } else if (ms >= 60000) {
            SimpleDateFormat formatter = new SimpleDateFormat("mm分ss秒");
            formatter.setTimeZone(TimeZone.getTimeZone("00分00秒"));
            return formatter.format(ms);
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("ss秒");
            formatter.setTimeZone(TimeZone.getTimeZone("00秒"));
            return formatter.format(ms);
        }
    }

    public void sendDefaultMessage(CommandSender sender) {
        sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l明日之后§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
        sender.sendMessage("§c§l▏   §c/ln openRecipeInv   打开配方制作台");
        sender.sendMessage("§c§l▏   §c/ln recipes   打开制作台");
        sender.sendMessage("§c§l▏   §c/ln gun [类型] [数量] [玩家]   给玩家枪");
        sender.sendMessage("§c§l▏   §c/ln guns   查看所有枪");
        sender.sendMessage("§c§l▏   §c/ln gun paper [类型] [最大使用次数] [数量] [玩家]   给玩家图纸");
        sender.sendMessage("§c§l▏   §c/ln bc [名称]  放置一个弹药箱");
        sender.sendMessage("§c§l▏   §c/ln bc list  查看所有弹药箱");
        sender.sendMessage("§c§l▏   §c/ln item [物品名称]   将手持物品以名称添加到物品组");
        sender.sendMessage("§c§l▏   §c/ln items     查看所有物品");
        sender.sendMessage("§c§l▏   §c/ln getItem [物品名称] [数量] [玩家]  获取一个物品");
        sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l明日之后§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
    }
}
