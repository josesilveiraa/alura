package me.josesilveiraa.alura.client.module;

import com.lukflug.panelstudio.setting.ICategory;
import com.lukflug.panelstudio.setting.IClient;
import com.lukflug.panelstudio.setting.IModule;
import me.josesilveiraa.alura.client.module.impl.combat.AutoClicker;
import me.josesilveiraa.alura.client.module.impl.combat.OldHitReg;
import me.josesilveiraa.alura.client.module.impl.combat.Velocity;
import me.josesilveiraa.alura.client.module.impl.gui.ClickGUIModule;
import me.josesilveiraa.alura.client.module.impl.hud.HUDEditorModule;
import me.josesilveiraa.alura.client.module.impl.hud.TabGUIModule;
import me.josesilveiraa.alura.client.module.impl.hud.WatermarkModule;
import me.josesilveiraa.alura.client.module.impl.hud.ArrayListModule;
import me.josesilveiraa.alura.client.module.impl.movement.Sprint;
import me.josesilveiraa.alura.client.module.impl.render.Fullbright;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum Category implements ICategory {
    COMBAT("Combat"), HUD("HUD"), MOVEMENT("Movement"), OTHER("Other"), RENDER("Render"), WORLD("World");
    public final String displayName;
    public final List<Module> modules = new ArrayList<>();

    Category(String displayName) {
        this.displayName = displayName;
    }

    public static ClickGUIModule clickGUIModule;
    public static HUDEditorModule hudEditorModule;

    public static void init() {
        Category.MOVEMENT.modules.add(new Sprint());

        Category.OTHER.modules.add(clickGUIModule = new ClickGUIModule());

        Category.HUD.modules.add(hudEditorModule = new HUDEditorModule());
        Category.HUD.modules.add(new TabGUIModule());
        Category.HUD.modules.add(new WatermarkModule());
        Category.HUD.modules.add(new ArrayListModule());

        Category.RENDER.modules.add(new Fullbright());

        Category.COMBAT.modules.add(new Velocity());
        Category.COMBAT.modules.add(new OldHitReg());
        Category.COMBAT.modules.add(new AutoClicker());
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Stream<IModule> getModules() {
        return modules.stream().map(module -> module);
    }

    public static IClient getClient() {
        return () -> Arrays.stream(Category.values());
    }
}
