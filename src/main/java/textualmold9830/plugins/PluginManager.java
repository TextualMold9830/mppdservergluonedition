package textualmold9830.plugins;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {

    private List<Plugin> plugins = new ArrayList<>();

    public void loadPlugins() {
        try {
            plugins = PluginLoader.loadPlugins();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (Plugin plugin : plugins) {
            createPluginFiles(plugin);
            plugin.initialize();
        }
    }

    public void shutdownPlugins() {
        for (Plugin plugin : plugins) {
            plugin.shutdown();
        }
    }

    public void addPlugin(Plugin plugin) {
        plugins.add(plugin);
        plugin.initialize();
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }
    public void fireEvent(Event event) {

    }
    public void removePlugin(String pluginName){
        for (Plugin plugin: plugins) {
            if (plugin.getName().equals(pluginName)){
                plugins.remove(plugin);
                plugin.shutdown();

            }
        }
    }
    private static void createPluginFiles(Plugin plugin) {
        String name = plugin.getName();
        try {
            Path.of("config/"+name).toFile().mkdir();
            Path configPath = Path.of("config/" + name + "/config.txt");
            if (!Files.exists(configPath)) {
                PrintWriter writer = new PrintWriter(configPath.toFile());
                writer.write(plugin.defaultConfig());
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
