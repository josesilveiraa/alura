package me.josesilveiraa.alura.client.module.impl.combat;

import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.setting.impl.BooleanSetting;
import me.josesilveiraa.alura.client.setting.impl.DoubleSetting;
import me.josesilveiraa.alura.client.setting.impl.EnumSetting;
import me.josesilveiraa.alura.client.setting.impl.IntegerSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Random;

public class AutoClicker extends Module {

    private final IntegerSetting minimumCps = new IntegerSetting("Minimum CPS", "minCps", "Minimum amount of clicks.", () -> true, 1, 60, 18);
    private final IntegerSetting maximumCps = new IntegerSetting("Maximum CPS", "maxCps", "Maximum amount of clicks.", () -> true, 1, 60, 20);
    private final EnumSetting<Event> eventType = new EnumSetting<>("Event", "eventType", "Which event do you want the module to work.", () -> true, Event.TICK, Event.class);
    private final BooleanSetting breakBlocks = new BooleanSetting("Break blocks", "brkBlocks", "Gives you the ability to break blocks.", () -> true, true);
    private final BooleanSetting invFill = new BooleanSetting("Inventory fill", "invFill", null, () -> true, false);
    private final BooleanSetting weaponOnly = new BooleanSetting("Weapon only", "wpnOnly", "The module just works with weapons.", () -> true, false);

    private final DoubleSetting jitterLeft = new DoubleSetting("Left jitter", "leftJitter", null, () -> true, 0.0, 3.0, 0.0);
    private boolean leftDown;
    private long leftDownTime;
    private long leftUpTime;
    private long leftk;
    private long leftl;
    private double leftm;
    private boolean leftn;
    private boolean breakHeld;

    private Random random;
    private Method playerMouseInput;

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks for you.", () -> true, true);

        settings.add(minimumCps);
        settings.add(maximumCps);
        settings.add(eventType);
        settings.add(breakBlocks);
        settings.add(invFill);
        settings.add(weaponOnly);
        settings.add(jitterLeft);

        try {
            playerMouseInput = ReflectionHelper.findMethod(GuiScreen.class, null, new String[]{"func_73864_a", "mouseClicked"}, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (playerMouseInput != null) playerMouseInput.setAccessible(true);
    }

    @Override
    public void onEnable() {
        if (playerMouseInput == null) disable();
        random = new Random();
    }

    @Override
    public void onDisable() {
        leftDownTime = 0;
        leftUpTime = 0;
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiInventory) && !(mc.currentScreen instanceof GuiChest))
            return;

        if (eventType.getValue() != Event.RENDER) return;

        click();
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiInventory) && !(mc.currentScreen instanceof GuiChest)) return;

        if (eventType.getValue() != Event.TICK) return;

        click();
    }

    private void click() {
        if(mc.currentScreen != null || !mc.inGameHasFocus) {
            doInventoryClick();
            return;
        }

        Mouse.poll();
        if(!Mouse.isButtonDown(0) && !leftDown) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
            setMouseButtonState(0, false);
        }

        if(Mouse.isButtonDown(0) || leftDown) {
            if(weaponOnly.isOn() && !isPlayerHoldingWeapon()) return;
            leftClickExecute(mc.gameSettings.keyBindAttack.getKeyCode());
        }
    }

    private void leftClickExecute(int key) {
        if (isBlockBreakable()) return;

        if (jitterLeft.getValue() > 0.0) {
            double jitter = jitterLeft.getValue() * 0.45;
            EntityPlayerSP player = mc.thePlayer;

            if (random.nextBoolean()) {
                player.rotationYaw = (float) (player.rotationYaw + random.nextFloat() * jitter);
            } else {
                player.rotationYaw = (float) (player.rotationYaw - random.nextFloat() * jitter);
            }

            if (random.nextBoolean()) {
                player.rotationPitch = (float) (player.rotationPitch + random.nextFloat() * jitter * 0.45);
            } else {
                player.rotationPitch = (float) (player.rotationPitch - random.nextFloat() * jitter * 0.45);
            }
        }

        if (leftUpTime > 0 && leftDownTime > 0) {
            if (System.currentTimeMillis() > leftUpTime && leftDown) {
                KeyBinding.setKeyBindState(key, true);
                KeyBinding.onTick(key);
                genLeftTimings();
                setMouseButtonState(0, true);
                leftDown = false;
            } else if (System.currentTimeMillis() > leftDownTime) {
                KeyBinding.setKeyBindState(key, false);
                leftDown = true;
                setMouseButtonState(0, false);
            }
        } else genLeftTimings();
    }

    private void genLeftTimings() {
        double clickSpeed = randomValueBetweenSettings(minimumCps, maximumCps, this.random) + 0.4D * this.random.nextDouble();
        long delay = (int) Math.round(1000.0D / clickSpeed);
        if (System.currentTimeMillis() > this.leftk) {
            if (!this.leftn && this.random.nextInt(100) >= 85) {
                this.leftn = true;
                this.leftm = 1.1D + this.random.nextDouble() * 0.15D;
            } else {
                this.leftn = false;
            }

            this.leftk = System.currentTimeMillis() + 500L + (long) this.random.nextInt(1500);
        }

        if (this.leftn) {
            delay = (long) ((double) delay * this.leftm);
        }

        if (System.currentTimeMillis() > this.leftl) {
            if (this.random.nextInt(100) >= 80) {
                delay += 50L + (long) this.random.nextInt(100);
            }

            this.leftl = System.currentTimeMillis() + 500L + (long) this.random.nextInt(1500);
        }

        this.leftUpTime = System.currentTimeMillis() + delay;
        this.leftDownTime = System.currentTimeMillis() + delay / 2L - (long) this.random.nextInt(10);
    }

    public boolean isBlockBreakable() {
        if (breakBlocks.isOn() && mc.objectMouseOver != null) {
            BlockPos blockPosition = mc.objectMouseOver.getBlockPos();

            if (blockPosition != null) {
                Block block = mc.theWorld.getBlockState(blockPosition).getBlock();
                if (block != Blocks.air && !(block instanceof BlockLiquid)) {
                    if (!breakHeld) {
                        int e = mc.gameSettings.keyBindAttack.getKeyCode();
                        KeyBinding.setKeyBindState(e, true);
                        KeyBinding.onTick(e);
                        breakHeld = true;
                    }
                    return true;
                }
                if (breakHeld) {
                    breakHeld = false;
                }
            }
        }
        return false;
    }

    public void doInventoryClick() {
        if (invFill.isOn() && (mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChest)) {
            if (!Mouse.isButtonDown(0) || !Keyboard.isKeyDown(54) && !Keyboard.isKeyDown(42)) {
                this.leftDownTime = 0L;
                this.leftUpTime = 0L;
            } else if (this.leftDownTime != 0L && this.leftUpTime != 0L) {
                if (System.currentTimeMillis() > this.leftUpTime) {
                    this.genLeftTimings();
                    this.inInvClick(mc.currentScreen);
                }
            } else {
                this.genLeftTimings();
            }
        }
    }

    private void inInvClick(GuiScreen guiScreen) {
        int mouseInGUIPosX = Mouse.getX() * guiScreen.width / mc.displayWidth;
        int mouseInGUIPosY = guiScreen.height - Mouse.getY() * guiScreen.height / mc.displayHeight - 1;

        try {
            this.playerMouseInput.invoke(guiScreen, mouseInGUIPosX, mouseInGUIPosY, 0);
        } catch (IllegalAccessException | InvocationTargetException var5) {
        }

    }

    private void setMouseButtonState(int mouseButton, boolean held) {
        MouseEvent m = new MouseEvent();

        ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, m, mouseButton, "button");
        ObfuscationReflectionHelper.setPrivateValue(MouseEvent.class, m, held, "buttonstate");
        MinecraftForge.EVENT_BUS.post(m);

        ByteBuffer buttons = ObfuscationReflectionHelper.getPrivateValue(Mouse.class, null, "buttons");
        buttons.put(mouseButton, (byte) (held ? 1 : 0));
        ObfuscationReflectionHelper.setPrivateValue(Mouse.class, null, buttons, "buttons");

    }

    private boolean isPlayerHoldingWeapon() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemSword || item instanceof ItemAxe;
        }
    }


    private double randomValueBetweenSettings(IntegerSetting setting, IntegerSetting anotherSetting, Random random) {
        return setting.getValue().equals(anotherSetting.getValue()) ? setting.getValue() : setting.getValue() + random.nextInt() * (anotherSetting.getValue() - setting.getValue());
    }

    enum Event {
        RENDER,
        TICK;
    }

}
