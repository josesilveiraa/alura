package me.josesilveiraa.alura.clickgui;

import com.google.gson.*;
import com.lukflug.panelstudio.config.IConfigList;
import com.lukflug.panelstudio.config.IPanelConfig;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GUIConfig implements IConfigList {

    private final String fileLocation;
    private JsonObject panelObject = null;

    public GUIConfig(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    @Override
    public void begin(boolean loading) {
        if(loading) {
            Path path = Paths.get(fileLocation + "ClickGUI.json");
            if(!Files.exists(path)) {
                return;
            }

            try {
                InputStream is = Files.newInputStream(path);
                JsonObject mainObj = new JsonParser().parse(new InputStreamReader(is)).getAsJsonObject();
                if(mainObj.get("panels") == null) return;

                panelObject = mainObj.get("panels").getAsJsonObject();
                is.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            panelObject = new JsonObject();
        }
    }

    @Override
    public void end(boolean loading) {
        if(panelObject == null) return;
        if(!loading) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                OutputStreamWriter fileOutputWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get(fileLocation + "ClickGUI.json")), StandardCharsets.UTF_8);
                JsonObject mainObject = new JsonObject();
                mainObject.add("panels", panelObject);

                String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
                fileOutputWriter.write(jsonString);
                fileOutputWriter.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        panelObject = null;
    }

    @Override
    public IPanelConfig addPanel(String title) {
        if(panelObject == null) return null;

        JsonObject valueObj = new JsonObject();
        panelObject.add(title, valueObj);

        return new AluraPanelConfig(valueObj);
    }

    @Override
    public IPanelConfig getPanel(String title) {
        if(panelObject == null) return null;

        JsonElement configObj = panelObject.get(title);
        if(configObj != null && configObj.isJsonObject()) {
            return new AluraPanelConfig(configObj.getAsJsonObject());
        }
        return null;
    }

    private static class AluraPanelConfig implements IPanelConfig {

        private final JsonObject config;

        public AluraPanelConfig(JsonObject config) {
            this.config = config;
        }

        @Override
        public void savePositon(Point position) {
            config.add("x", new JsonPrimitive(position.x));
            config.add("y", new JsonPrimitive(position.y));
        }

        @Override
        public void saveSize(Dimension size) {
            config.add("height", new JsonPrimitive(size.height));
            config.add("width", new JsonPrimitive(size.width));
        }

        @Override
        public Point loadPosition() {
            Point point = new Point();

            JsonElement panelPosX = config.get("x");
            if(panelPosX != null && panelPosX.isJsonPrimitive()) {
                point.x = panelPosX.getAsInt();
            } else return null;

            JsonElement panelPosY = config.get("y");
            if(panelPosY != null && panelPosY.isJsonPrimitive()) {
                point.y = panelPosY.getAsInt();
            } else return null;

            return point;
        }

        @Override
        public Dimension loadSize() {
            Dimension dimension = new Dimension();
            JsonElement panelHeight = config.get("height");

            int width = 0, height = 0;

            if(panelHeight != null && panelHeight.isJsonPrimitive()) {
                height = panelHeight.getAsInt();
            }

            JsonElement panelWidth = config.get("width");

            if(panelWidth != null && panelWidth.isJsonPrimitive()) {
                width = panelWidth.getAsInt();
            }

            dimension.setSize(width, height);

            return dimension;
        }

        @Override
        public void saveState(boolean state) {
            config.add("state", new JsonPrimitive(state));
        }

        @Override
        public boolean loadState() {
            JsonElement panelOpenObj = config.get("state");

            if(panelOpenObj != null && panelOpenObj.isJsonPrimitive()) {
                return panelOpenObj.getAsBoolean();
            }
            return false;
        }
    }
}
