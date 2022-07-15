package me.josesilveiraa.alura.module.manager;

import me.josesilveiraa.alura.module.Category;
import me.josesilveiraa.alura.module.Module;
import me.josesilveiraa.alura.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private static final ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        addModules();
    }
    private void addModules() {
        for (Category category : Category.values()) {
            modules.addAll(category.modules);
        }
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public List<Setting<?>> getSettingsForModule(Module module) {
        return module.settings;
    }
}
