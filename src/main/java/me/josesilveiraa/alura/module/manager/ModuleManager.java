package me.josesilveiraa.alura.module.manager;

import me.josesilveiraa.alura.module.Category;
import me.josesilveiraa.alura.module.Module;

import java.util.ArrayList;

public class ModuleManager {

    private static final ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        addModules();
    }
    private void addModules() {
        for (Category category : Category.values()) {
            if(category.modules.size() == 0) return;

            modules.addAll(category.modules);
        }
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
}
