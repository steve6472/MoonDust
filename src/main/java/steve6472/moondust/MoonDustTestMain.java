package steve6472.moondust;

import steve6472.flare.core.Flare;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustTestMain
{
    public static void main(String[] args) throws IOException, URISyntaxException
    {
        System.setProperty("joml.format", "false");
        if (new File("modules/moondust").exists())
        {
            try (var dirStream = Files.walk(Paths.get("modules/moondust"))) {
                dirStream
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder())
                    .forEach(file ->
                    {
                        if (!file.delete())
                        {
                            System.err.println("Failed to delete " + file.getAbsolutePath());
                        }
                    });
            }
        }
        MoonDust.getInstance().init();
        Flare.start(new MoonDustTest());
    }
}
