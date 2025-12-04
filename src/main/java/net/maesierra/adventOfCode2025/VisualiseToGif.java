package net.maesierra.adventOfCode2025;


import com.madgag.gif.fmsware.AnimatedGifEncoder;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.awt.image.BufferedImage.TYPE_BYTE_INDEXED;
import static javax.swing.SwingUtilities.invokeLater;

public class VisualiseToGif extends Runner{

    private static String outputFileName(RunData data) {
        try {
            return (new File(Paths.get("./src/main/resources").toAbsolutePath().normalize().toFile(), "visualise_%s_%s.gif".formatted(data.day(), data.part()))).getCanonicalPath();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            run(args, (data, input) -> {
                VisualiseProperties properties = data.solution().visualiseProperties();
                Function<Graphics2D, Boolean> paintAction = switch (data.part()) {
                    case "1" -> data.solution().visualisePart1(input);
                    case "2" -> data.solution().visualisePart2(input);
                    default -> throw new IllegalStateException("Unexpected value: " + data.part());
                };
                String outputFileName = outputFileName(data);
                AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
                gifEncoder.start(outputFileName);
                gifEncoder.setDelay(properties.speed());
                boolean finished = false;
                long counter = 0;
                while (!finished) {
                    BufferedImage image = new BufferedImage(properties.width(), properties.height(), TYPE_BYTE_INDEXED);
                    finished = paintAction.apply(image.createGraphics());
                    System.out.printf("Generating frame %d\n", counter);
                    counter++;
                    gifEncoder.addFrame(image);
                }
                gifEncoder.finish();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        };
    }
}
