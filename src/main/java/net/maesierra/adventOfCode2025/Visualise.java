package net.maesierra.adventOfCode2025;


import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import static javax.swing.SwingUtilities.invokeLater;

public class Visualise extends Runner{

    public static void main(String[] args) {
        invokeLater(() -> {
            try {
                run(args, (data, input) -> {
                    VisualiseProperties properties = data.solution().visualiseProperties();
                    JFrame frame = new JFrame("Day %s".formatted(data.day()));
                    Function<Graphics2D, Boolean> paintAction = switch (data.part()) {
                        case "1" -> data.solution().visualisePart1(input);
                        case "2" -> data.solution().visualisePart2(input);
                        default -> throw new IllegalStateException("Unexpected value: " + data.part());
                    };
                    AtomicBoolean completed = new AtomicBoolean(false);
                    frame.add(new JPanel() {
                        @Override
                        public void paint(Graphics g) {
                            completed.set(paintAction.apply((Graphics2D) g));
                        }
                    });
                    frame.setSize(properties.width(), properties.height());
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    Timer timer = new Timer(properties.speed(), (event) -> {
                        if (!completed.get()) {
                            frame.repaint();
                        }
                    });
                    timer.start();
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }});
    }
}
