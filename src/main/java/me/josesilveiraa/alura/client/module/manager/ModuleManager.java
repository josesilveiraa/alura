package me.josesilveiraa.alura.client.module.manager;

import me.josesilveiraa.alura.client.module.Category;
import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.setting.Setting;

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
