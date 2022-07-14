package me.josesilveiraa.alura.module;

import com.lukflug.panelstudio.setting.ICategory;
import com.lukflug.panelstudio.setting.IClient;
import com.lukflug.panelstudio.setting.IModule;
import me.josesilveiraa.alura.module.impl.combat.Velocity;
import me.josesilveiraa.alura.module.impl.gui.ClickGUIModule;
import me.josesilveiraa.alura.module.impl.gui.HUDEditorModule;
import me.josesilveiraa.alura.module.impl.gui.TabGUIModule;
import me.josesilveiraa.alura.module.impl.gui.WatermarkModule;
import me.josesilveiraa.alura.module.impl.movement.Sprint;
import me.josesilveiraa.alura.module.impl.render.Fullbright;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum Category implements ICategory {
    COMBAT("Combat"), EXPLOITS("Exploits"), HUD("HUD"), MISCELLANEOUS("Miscellaneous"), MOVEMENT("Movement"), OTHER("Other"), RENDER("Render"), WORLD("World");
    public final String displayName;
    public final List<Module> modules = new ArrayList<>();

    Category(String displayName) {
        this.displayName = displayName;
    }

    public static void init() {
        Category.MOVEMENT.modules.add(new Sprint());

        Category.OTHER.modules.add(new ClickGUIModule());
        Category.OTHER.modules.add(new HUDEditorModule());

        Category.HUD.modules.add(new TabGUIModule());
        Category.HUD.modules.add(new WatermarkModule());

        Category.RENDER.modules.add(new Fullbright());

        Category.COMBAT.modules.add(new Velocity());
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