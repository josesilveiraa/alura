package me.josesilveiraa.alura.client.module.impl.hud;

import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.hud.HUDList;
import com.lukflug.panelstudio.hud.ListComponent;
import me.josesilveiraa.alura.Alura;
import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.setting.impl.BooleanSetting;
import me.josesilveiraa.alura.client.setting.impl.ColorSetting;
import me.josesilveiraa.alura.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListModule extends Module {

    private static ArrayListModule INSTANCE;

    private static final ColorSetting color = new ColorSetting("Font color", "fontColor", "Changes the font color.", () -> true, false, true, Color.gray, true);
    private static final BooleanSetting sortRight = new BooleanSetting("Sort right", "sortRight", "Sorts the strings right.", () -> true, false);

    public ArrayListModule() {
        super("ArrayList", "Displays active modules on the HUD.", () -> true, true, false);
        INSTANCE = this;

        settings.add(color);
        settings.add(sortRight);
    }

    public static List<Module> shownModules; // This is to avoid ConcurrentModificationException

    @Override
    public void onEnable() {
        shownModules = Alura.getModuleManager().getArrayListModules();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if(event.phase == TickEvent.Phase.END && Utils.isInGame()) {
            shownModules = Alura.getModuleManager().getArrayListModules();
        }
    }

    public static IFixedComponent getComponent() {
        return new ListComponent(() -> "ArrayList", new Point(10, 10), "arraylist", new HUDList() {
            @Override
            public int getSize() {
                return shownModules.size();
            }

            @Override
            public String getItem(int index) {
                return shownModules.get(index).getDisplayName();
            }

            @Override
            public Color getItemColor(int index) {
                return color.getValue();
            }

            @Override
            public boolean sortUp() {
                return true;
            }

            @Override
            public boolean sortRight() {
                return sortRight.isOn();
            }
        }, 9, 2);
    }

    public static IToggleable getToggle() {
        return INSTANCE.isEnabled();
    }
}
