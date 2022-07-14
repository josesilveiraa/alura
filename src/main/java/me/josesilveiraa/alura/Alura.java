package me.josesilveiraa.alura;

import me.josesilveiraa.alura.api.config.ConfigLoader;
import me.josesilveiraa.alura.api.config.ConfigSaver;
import me.josesilveiraa.alura.clickgui.ClickGUI;
import me.josesilveiraa.alura.module.Category;
import me.josesilveiraa.alura.module.impl.gui.ClickGUIModule;
import me.josesilveiraa.alura.module.impl.gui.HUDEditorModule;
import me.josesilveiraa.alura.module.manager.ModuleManager;
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
public class    Alura {
    public static final String MODID = "alura";
    public static final String VERSION = "0.1";

    private static ModuleManager moduleManager;

    private static ClickGUI clickGUI;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Category.init();
        moduleManager = new ModuleManager();

        clickGUI = new ClickGUI();

        MinecraftForge.EVENT_BUS.register(this);

        ConfigLoader.load();

        Runtime.getRuntime().addShutdownHook(new Thread(ConfigSaver::save));

        System.out.println(getModuleManager().getModules().size());
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            clickGUI.render();
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(ClickGUIModule.keybind.getKey())) clickGUI.enterGUI();
        if (Keyboard.isKeyDown(HUDEditorModule.binding.getKey())) clickGUI.enterHUDEditor();
        if (Keyboard.getEventKeyState()) clickGUI.handleKeyEvent(Keyboard.getEventKey());
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (Utils.isInGame()) {
                    getModuleManager().getModules().forEach(module -> {
                        if(Minecraft.getMinecraft().currentScreen == null) {
                            module.handleBinding();
                        }

                        if(module.isEnabled().isOn()) module.onUpdate();
                    });
            }
        }
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }

    public static ClickGUI getClickGUI() {
        return clickGUI;
    }
}
