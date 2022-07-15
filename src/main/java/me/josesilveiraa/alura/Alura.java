package me.josesilveiraa.alura;

import me.josesilveiraa.alura.api.command.Command;
import me.josesilveiraa.alura.api.command.manager.CommandManager;
import me.josesilveiraa.alura.api.config.ConfigLoader;
import me.josesilveiraa.alura.api.config.ConfigSaver;
import me.josesilveiraa.alura.api.clickgui.ClickGUI;
import me.josesilveiraa.alura.api.event.ClientPreChatEvent;
import me.josesilveiraa.alura.client.module.Category;
import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.module.impl.gui.ClickGUIModule;
import me.josesilveiraa.alura.client.module.impl.gui.HUDEditorModule;
import me.josesilveiraa.alura.client.module.manager.ModuleManager;
import me.josesilveiraa.alura.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;


@Mod(modid = Alura.MODID, version = Alura.VERSION)
public class Alura {
    public static final String MODID = "alura";
    public static final String VERSION = "0.1";

    private static ModuleManager moduleManager;
    private static CommandManager commandManager;

    private static ClickGUI clickGUI;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Category.init();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();

        clickGUI = new ClickGUI();

        MinecraftForge.EVENT_BUS.register(this);

        ConfigLoader.load();

        Runtime.getRuntime().addShutdownHook(new Thread(ConfigSaver::save));
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            clickGUI.render();
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Category.clickGUIModule.getBinding())) clickGUI.enterGUI();
        if (Keyboard.isKeyDown(Category.hudEditorModule.getBinding())) clickGUI.enterHUDEditor();
        if (Keyboard.getEventKeyState()) clickGUI.handleKeyEvent(Keyboard.getEventKey());

        for(Module module : getModuleManager().getModules()) {
            if(module.getBinding() != 0 && Keyboard.isKeyDown(module.getBinding())) module.toggle();
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (Utils.isInGame()) {

                for (Module module : getModuleManager().getModules()) {
                    if (module.isModuleEnabled()) module.onUpdate();
                }
            }
        }
    }

    @SubscribeEvent
    public void onMessage(ClientPreChatEvent event) {
        if(event.message.startsWith(getCommandManager().getPrefix())) {
            getCommandManager().callCommand(event.message.replaceFirst(".", ""));
            event.setCanceled(true);
        }
    }


    public static ModuleManager getModuleManager() {
        return moduleManager;
    }

    public static ClickGUI getClickGUI() {
        return clickGUI;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }
}
