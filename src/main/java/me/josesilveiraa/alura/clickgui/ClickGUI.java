package me.josesilveiraa.alura.clickgui;

import com.lukflug.panelstudio.base.*;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.component.IResizable;
import com.lukflug.panelstudio.component.IScrollSize;
import com.lukflug.panelstudio.container.IContainer;
import com.lukflug.panelstudio.hud.HUDGUI;
import com.lukflug.panelstudio.layout.*;
import com.lukflug.panelstudio.mc8forge.MinecraftHUDGUI;
import com.lukflug.panelstudio.popup.*;
import com.lukflug.panelstudio.setting.*;
import com.lukflug.panelstudio.theme.*;
import com.lukflug.panelstudio.widget.*;
import me.josesilveiraa.alura.Alura;
import me.josesilveiraa.alura.module.Category;
import me.josesilveiraa.alura.module.impl.gui.ClickGUIModule;
import me.josesilveiraa.alura.module.impl.gui.HUDEditorModule;
import me.josesilveiraa.alura.module.impl.gui.TabGUIModule;
import me.josesilveiraa.alura.module.impl.gui.WatermarkModule;
import me.josesilveiraa.alura.setting.impl.BooleanSetting;
import me.josesilveiraa.alura.setting.impl.ColorSetting;
import me.josesilveiraa.alura.setting.impl.IntegerSetting;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;

public class ClickGUI extends MinecraftHUDGUI {
    private final GUIInterface inter;
    public final HUDGUI gui;
    public static final int WIDTH = 120, HEIGHT = 12, DISTANCE = 6, BORDER = 2;

    public ClickGUI() {
        // Getting client structure ...
        IClient client = Category.getClient();
        /* Set to false to disable horizontal clipping, this may cause graphical glitches,
         * but will let you see long text, even if it is too long to fit in the panel. */
        inter = new GUIInterface(true) {
            @Override
            protected String getResourcePrefix() {
                return "alura:";
            }
        };
        // Instantiating theme ...
        ITheme theme = new OptimizedTheme(new ThemeSelector(inter));
        // Instantiating GUI ...
        IToggleable guiToggle = new SimpleToggleable(false);
        IToggleable hudToggle = new SimpleToggleable(false) {
            @Override
            public boolean isOn() {
                return guiToggle.isOn() ? HUDEditorModule.showHUD.isOn() : super.isOn();
            }
        };
        gui = new HUDGUI(inter, theme.getDescriptionRenderer(), (IPopupPositioner) new MousePositioner(new Point(10, 10)), guiToggle, hudToggle);
        // Creating animation ...
        Supplier<Animation> animation = () -> new SettingsAnimation(ClickGUIModule.animationSpeed::getValue, inter::getTime);

        // Populating HUD ...
        gui.addHUDComponent(TabGUIModule.getComponent(client, new IContainer<IFixedComponent>() {
            @Override
            public boolean addComponent(IFixedComponent component) {
                return gui.addHUDComponent(component, () -> true);
            }

            @Override
            public boolean addComponent(IFixedComponent component, IBoolean visible) {
                return gui.addHUDComponent(component, visible);
            }

            @Override
            public boolean removeComponent(IFixedComponent component) {
                return gui.removeComponent(component);
            }
        }, animation), TabGUIModule.getToggle(), animation.get(), theme, BORDER);
        gui.addHUDComponent(WatermarkModule.getComponent(), WatermarkModule.getToggle(), animation.get(), theme, BORDER);

        // Creating popup types ...
        BiFunction<Context, Integer, Integer> scrollHeight = (context, componentHeight) -> Math.min(componentHeight, Math.max(HEIGHT * 4, ClickGUI.this.height - context.getPos().y - HEIGHT));
        PopupTuple popupType = new PopupTuple(new PanelPositioner(new Point(0, 0)), false, new IScrollSize() {
            @Override
            public int getScrollHeight(Context context, int componentHeight) {
                return scrollHeight.apply(context, componentHeight);
            }
        });
        PopupTuple colorPopup = new PopupTuple(new CenteredPositioner(() -> new Rectangle(new Point(0, 0), inter.getWindowSize())), true, new IScrollSize() {
            @Override
            public int getScrollHeight(Context context, int componentHeight) {
                return scrollHeight.apply(context, componentHeight);
            }
        });
        // Defining resize behavior ...
        IntFunction<IResizable> resizable = width -> new IResizable() {
            final Dimension size = new Dimension(width, 320);

            @Override
            public Dimension getSize() {
                return new Dimension(size);
            }

            @Override
            public void setSize(Dimension size) {
                this.size.width = size.width;
                this.size.height = size.height;
                if (size.width < 75) this.size.width = 75;
                if (size.height < 50) this.size.height = 50;
            }
        };
        // Defining scroll behavior ...
        Function<IResizable, IScrollSize> resizableHeight = size -> new IScrollSize() {
            @Override
            public int getScrollHeight(Context context, int componentHeight) {
                return size.getSize().height;
            }
        };
        // Defining function keys ...
        IntPredicate keybindKey = scancode -> scancode == Keyboard.KEY_DELETE;
        IntPredicate charFilter = character -> {
            return character >= ' ';
        };
        ITextFieldKeys keys = new ITextFieldKeys() {
            @Override
            public boolean isBackspaceKey(int scancode) {
                return scancode == Keyboard.KEY_BACK;
            }

            @Override
            public boolean isDeleteKey(int scancode) {
                return scancode == Keyboard.KEY_DELETE;
            }

            @Override
            public boolean isInsertKey(int scancode) {
                return scancode == Keyboard.KEY_INSERT;
            }

            @Override
            public boolean isLeftKey(int scancode) {
                return scancode == Keyboard.KEY_LEFT;
            }

            @Override
            public boolean isRightKey(int scancode) {
                return scancode == Keyboard.KEY_RIGHT;
            }

            @Override
            public boolean isHomeKey(int scancode) {
                return scancode == Keyboard.KEY_HOME;
            }

            @Override
            public boolean isEndKey(int scancode) {
                return scancode == Keyboard.KEY_END;
            }

            @Override
            public boolean isCopyKey(int scancode) {
                return scancode == Keyboard.KEY_C;
            }

            @Override
            public boolean isPasteKey(int scancode) {
                return scancode == Keyboard.KEY_V;
            }

            @Override
            public boolean isCutKey(int scancode) {
                return scancode == Keyboard.KEY_X;
            }

            @Override
            public boolean isAllKey(int scancode) {
                return scancode == Keyboard.KEY_A;
            }
        };

        // Normal generator
        IComponentGenerator generator = new ComponentGenerator(keybindKey, charFilter, keys);
        // Use all the fancy widgets with text boxes
        IComponentGenerator csgoGenerator = new ComponentGenerator(keybindKey, charFilter, keys) {
            @Override
            public IComponent getBooleanComponent(IBooleanSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
                return new ToggleSwitch(setting, theme.getToggleSwitchRenderer(isContainer));
            }

            @Override
            public IComponent getEnumComponent(IEnumSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
                return new DropDownList(setting, theme, isContainer, false, keys, new IScrollSize() {
                }, adder::addPopup) {
                    @Override
                    protected Animation getAnimation() {
                        return animation.get();
                    }

                    @Override
                    public boolean allowCharacter(char character) {
                        return charFilter.test(character);
                    }

                    @Override
                    protected boolean isUpKey(int key) {
                        return key == Keyboard.KEY_UP;
                    }

                    @Override
                    protected boolean isDownKey(int key) {
                        return key == Keyboard.KEY_DOWN;
                    }

                    @Override
                    protected boolean isEnterKey(int key) {
                        return key == Keyboard.KEY_RETURN;
                    }
                };
            }

            @Override
            public IComponent getNumberComponent(INumberSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
                return new Spinner(setting, theme, isContainer, true, keys);
            }

            @Override
            public IComponent getColorComponent(IColorSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
                return new ColorPickerComponent(setting, new ThemeTuple(theme.theme, theme.logicalLevel, colorLevel));
            }
        };

        // Classic Panel
        IComponentAdder classicPanelAdder = new PanelAdder(gui, false, () -> ClickGUIModule.layout.getValue() == ClickGUIModule.Layout.ClassicPanel, title -> "classicPanel_" + title) {
            @Override
            protected IResizable getResizable(int width) {
                return resizable.apply(width);
            }

            @Override
            protected IScrollSize getScrollSize(IResizable size) {
                return resizableHeight.apply(size);
            }
        };
        ILayout classicPanelLayout = new PanelLayout(WIDTH, new Point(DISTANCE, DISTANCE), (WIDTH + DISTANCE) / 2, HEIGHT + DISTANCE, animation, level -> ChildUtil.ChildMode.DOWN, level -> ChildUtil.ChildMode.DOWN, popupType);
        classicPanelLayout.populateGUI(classicPanelAdder, generator, client, theme);
        // Category CSGO
        AtomicReference<IResizable> categoryResizable = new AtomicReference<IResizable>(null);
        IComponentAdder categoryCSGOAdder = new PanelAdder(gui, true, () -> ClickGUIModule.layout.getValue() == ClickGUIModule.Layout.CSGOCategory, title -> "categoryCSGO_" + title) {
            @Override
            protected IResizable getResizable(int width) {
                categoryResizable.set(resizable.apply(width));
                return categoryResizable.get();
            }
        };
        ILayout categoryCSGOLayout = new CSGOLayout(new Labeled("Alura Client v" + Alura.VERSION, null, () -> true), new Point(100, 100), 480, WIDTH, animation, "Enabled", false, false, 2, ChildUtil.ChildMode.POPUP, colorPopup) {
            @Override
            public int getScrollHeight(Context context, int componentHeight) {
                return resizableHeight.apply(categoryResizable.get()).getScrollHeight(null, height);
            }
        };
        categoryCSGOLayout.populateGUI(categoryCSGOAdder, csgoGenerator, client, theme);
        // Searchable CSGO
        AtomicReference<IResizable> searchableResizable = new AtomicReference<IResizable>(null);
        IComponentAdder searchableCSGOAdder = new PanelAdder(gui, true, () -> ClickGUIModule.layout.getValue() == ClickGUIModule.Layout.SearchableCSGO, title -> "searchableCSGO_" + title) {
            @Override
            protected IResizable getResizable(int width) {
                searchableResizable.set(resizable.apply(width));
                return searchableResizable.get();
            }
        };
        ILayout searchableCSGOLayout = new SearchableLayout(new Labeled("Alura Client v" + Alura.VERSION, null, () -> true), new Labeled("Search", null, () -> true), new Point(100, 100), 480, WIDTH, animation, "Enabled", 2, ChildUtil.ChildMode.POPUP, colorPopup, Comparator.comparing(ILabeled::getDisplayName), charFilter, keys) {
            @Override
            public int getScrollHeight(Context context, int componentHeight) {
                return resizableHeight.apply(searchableResizable.get()).getScrollHeight(null, height);
            }
        };
        searchableCSGOLayout.populateGUI(searchableCSGOAdder, csgoGenerator, client, theme);
    }

    @Override
    protected HUDGUI getGUI() {
        return gui;
    }

    @Override
    protected GUIInterface getInterface() {
        return inter;
    }

    @Override
    protected int getScrollSpeed() {
        return ClickGUIModule.scrollSpeed.getValue();
    }


    private class ThemeSelector implements IThemeMultiplexer {
        protected Map<ClickGUIModule.Theme, ITheme> themes = new EnumMap<>(ClickGUIModule.Theme.class);

        public ThemeSelector(IInterface inter) {
            BooleanSetting clearGradient = new BooleanSetting("Gradient", "gradient", "Whether the title bars should have a gradient.", () -> ClickGUIModule.theme.getValue() == ClickGUIModule.Theme.Clear, true);
            BooleanSetting ignoreDisabled = new BooleanSetting("Ignore Disabled", "ignoreDisabled", "Have the rainbow drawn for disabled containers.", () -> ClickGUIModule.theme.getValue() == ClickGUIModule.Theme.Rainbow, false);
            BooleanSetting buttonRainbow = new BooleanSetting("Button Rainbow", "buttonRainbow", "Have a separate rainbow for each component.", () -> ClickGUIModule.theme.getValue() == ClickGUIModule.Theme.Rainbow, false);
            IntegerSetting rainbowGradient = new IntegerSetting("Rainbow Gradient", "rainbowGradient", "How fast the rainbow should repeat.", () -> ClickGUIModule.theme.getValue() == ClickGUIModule.Theme.Rainbow, 150, 50, 300);
            ClickGUIModule.theme.subSettings.add(clearGradient);
            ClickGUIModule.theme.subSettings.add(ignoreDisabled);
            ClickGUIModule.theme.subSettings.add(buttonRainbow);
            ClickGUIModule.theme.subSettings.add(rainbowGradient);
            addTheme(ClickGUIModule.Theme.Clear, new ClearTheme(new ThemeScheme(ClickGUIModule.Theme.Clear), clearGradient::getValue, 9, 3, 1, ": " + EnumChatFormatting.GRAY));
            addTheme(ClickGUIModule.Theme.GameSense, new GameSenseTheme(new ThemeScheme(ClickGUIModule.Theme.GameSense), 9, 4, 5, ": " + EnumChatFormatting.GRAY));
            addTheme(ClickGUIModule.Theme.Rainbow, new RainbowTheme(new ThemeScheme(ClickGUIModule.Theme.Rainbow), ignoreDisabled::getValue, buttonRainbow::getValue, rainbowGradient::getValue, 9, 3, ": " + EnumChatFormatting.GRAY));
            addTheme(ClickGUIModule.Theme.Windows31, new Windows31Theme(new ThemeScheme(ClickGUIModule.Theme.Windows31), 9, 2, 9, ": " + EnumChatFormatting.DARK_GRAY));
            addTheme(ClickGUIModule.Theme.Impact, new ImpactTheme(new ThemeScheme(ClickGUIModule.Theme.Impact), 9, 4));
        }

        @Override
        public ITheme getTheme() {
            return themes.getOrDefault(ClickGUIModule.theme.getValue(), themes.get(ClickGUIModule.Theme.GameSense));
        }

        private void addTheme(ClickGUIModule.Theme key, ITheme value) {
            themes.put(key, new OptimizedTheme(value));
            value.loadAssets(inter);
        }


        private class ThemeScheme implements IColorScheme {
            private final ClickGUIModule.Theme themeValue;
            private final String themeName;

            public ThemeScheme(ClickGUIModule.Theme themeValue) {
                this.themeValue = themeValue;
                this.themeName = themeValue.toString().toLowerCase();
            }

            @Override
            public void createSetting(ITheme theme, String name, String description, boolean hasAlpha, boolean allowsRainbow, Color color, boolean rainbow) {
                ClickGUIModule.theme.subSettings.add(new ColorSetting(name, themeName + "-" + name, description, () -> ClickGUIModule.theme.getValue() == themeValue, hasAlpha, allowsRainbow, color, rainbow));
            }

            @Override
            public Color getColor(String name) {
                return ((ColorSetting) ClickGUIModule.theme.subSettings.stream().filter(setting -> setting.configName.equals(themeName + "-" + name)).findFirst().orElse(null)).getValue();
            }
        }
    }
}
