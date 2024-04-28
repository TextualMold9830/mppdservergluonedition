package textualmold9830;

import com.gluonhq.attach.storage.StorageService;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.network.Server;
import com.watabou.pixeldungeon.scenes.InterLevelSceneServer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class Main {
    public static Path base = StorageService.create().get().getPublicStorage("mppdserver").get().toPath();

    public static void main(String[] args) {
        initFolders();
        Preferences.load();
        Runtime.getRuntime().addShutdownHook(new Thread(Preferences::save));
        Server.startServer();
        Dungeon.init();
        InterLevelSceneServer.descend(null);
        //initTextures();
        //loadClassesForAgent();
        System.out.println("Server started");

    }

    private static void initFolders() {
        new File(base + "plugins").mkdirs();
        new File(base + "config").mkdirs();
        new File(base + "save").mkdirs();
        new File(base + "textures").mkdirs();
    }

    private static void initTextures() {
        File textureDir = new File("textures");
        for (File texture : textureDir.listFiles()) {
            if (texture.getName().endsWith(".zip")) {
                try {
                    Server.textures.add(Base64.getEncoder().encodeToString(Files.readAllBytes(texture.toPath())));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Added texture: " + texture.getName().replace(".zip", ""));
            }
        }
    }
}

