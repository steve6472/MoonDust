package steve6472.moondust;

import steve6472.flare.core.Flare;
import steve6472.flare.util.PackerUtil;

import java.io.File;
import java.io.IOException;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static java.lang.foreign.ValueLayout.*;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustTestMain
{
    public static void main(String[] args) throws IOException, URISyntaxException
    {
        if (System.getProperty("os.name").startsWith("Windows 10"))
            enableWinConsoleColors();

        PackerUtil.PADDING = 0;
        PackerUtil.DUPLICATE_BORDER = false;
        System.setProperty("joml.format", "false");
        deleteDevModule("moondust");
        MoonDust.getInstance().init();
        Flare.start(new MoonDustTest());
    }

    private static void enableWinConsoleColors()
    {
        System.out.println("Init Colored Console");
        try (Arena arena = Arena.ofConfined())
        {
            Linker linker = Linker.nativeLinker();
            SymbolLookup kernel32 = SymbolLookup.libraryLookup("kernel32", arena);

            kernel32.find("GetStdHandle").ifPresent(func_getHandle -> {
                MethodHandle getStdHandle = linker.downcallHandle(func_getHandle, FunctionDescriptor.of(ADDRESS, JAVA_INT));

                kernel32.find("SetConsoleMode").ifPresent(func -> {
                    MethodHandle methodHandle = linker.downcallHandle(func, FunctionDescriptor.of(JAVA_BOOLEAN, ADDRESS, JAVA_INT));

                    try
                    {
                        MemorySegment handle = (MemorySegment) getStdHandle.invoke(-11);
                        /*
                         * ENABLE_PROCESSED_OUTPUT 0x0001
                         * ENABLE_VIRTUAL_TERMINAL_PROCESSING 0x0004
                         */
                        boolean success = (boolean) methodHandle.invoke(handle, 0x0004 | 0x0001 | 0x0008);
                        System.out.println("Setting colored console: " + success);
                    } catch (Throwable e)
                    {
                        throw new RuntimeException(e);
                    }
                });
            });
        }
    }

    public static void deleteDevModule(String module) throws IOException
    {
        if (new File("modules/" + module).exists())
        {
            try (var dirStream = Files.walk(Paths.get("modules/" + module))) {
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
    }
}
