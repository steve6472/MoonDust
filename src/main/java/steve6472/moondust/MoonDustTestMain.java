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
        try (var dirStream = Files.walk(Paths.get("modules/moondust"))) {
            dirStream
                .map(Path::toFile)
                .sorted(Comparator.reverseOrder())
                .forEach(File::delete);
        }
        MoonDust.init();
        Flare.start(new MoonDustTest());
    }
}
