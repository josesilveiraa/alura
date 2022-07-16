package me.josesilveiraa.alura.client.module.manager;

import me.josesilveiraa.alura.client.module.Category;
import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.setting.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Module> getArrayListModules() {
        return modules.stream().filter(Module::isModuleEnabled).filter(Module::isShown).sorted(Comparator.comparingInt(o2 -> Minecraft.getMinecraft().fontRendererObj.getStringWidth(o2.getDisplayName()))).collect(Collectors.toList());
    }

    public void sort() {
        modules.sort(Comparator.comparingInt(o2 -> Minecraft.getMinecraft().fontRendererObj.getStringWidth(o2.getDisplayName())));
    }
}
