package me.josesilveiraa.alura.client.module;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.base.IToggleable;
import com.lukflug.panelstudio.setting.IModule;
import com.lukflug.panelstudio.setting.ISetting;
import me.josesilveiraa.alura.client.setting.Setting;
import me.josesilveiraa.alura.client.setting.impl.KeybindSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class Module implements IModule {
    public final String displayName, description;
    public final IBoolean visible;
    public final List<Setting<?>> settings = new ArrayList<>();
    public final boolean toggleable;
    private boolean enabled = false;
    private int binding = 0;
    protected static Minecraft mc = Minecraft.getMinecraft();

    public Module(String displayName, String description, IBoolean visible, boolean toggleable) {
        this.displayName = displayName;
        this.description = description;
        this.visible = visible;
        this.toggleable = toggleable;
        this.settings.add(new KeybindSetting("Keybind", "keybind", "The module binding.", () -> true, 0) {
            @Override
            public int getKey() {
                return binding;
            }

            @Override
            public void setKey(int key) {
                binding = key;
            }

            @Override
            public String getKeyName() {
                return getBindName();
            }
        });
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onUpdate() {
    }

    public void enable() {
        this.enabled = true;
        this.onEnable();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void disable() {
        this.enabled = false;
        this.onDisable();

        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void toggle() {
        if (enabled) disable();
        else enable();
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public IBoolean isVisible() {
        return visible;
    }

    @Override
    public IToggleable isEnabled() {
        if (!toggleable) return null;
        return new IToggleable() {
            @Override
            public boolean isOn() {
                return enabled;
            }

            @Override
            public void toggle() {
                Module.this.toggle();
            }
        };
    }

    public int getBinding() {
        return binding;
    }

    public String getBindName() {
        if (this.binding <= 0 || this.binding > 255) return "None";
        return Keyboard.getKeyName(this.binding);
    }

    public void setBinding(int binding) {
        this.binding = binding;
    }

    public boolean isModuleEnabled() {
        return enabled;
    }

    @Override
    public Stream<ISetting<?>> getSettings() {
        Stream<ISetting<?>> result = settings.stream().filter(setting -> setting instanceof ISetting).sorted(Comparator.comparing(a -> a.displayName)).map(setting -> (ISetting<?>) setting);

//        return Stream.concat(result, Stream.of(new IKeybindSetting() {
//            @Override
//            public int getKey() {
//                return getBinding();
//            }
//
//            @Override
//            public void setKey(int key) {
//                Module.this.binding = key;
//            }
//
//            @Override
//            public String getKeyName() {
//                return getBindName();
//            }
//
//            @Override
//            public String getDisplayName() {
//                return "Keybind";
//            }
//        }));
        return result;
    }
}