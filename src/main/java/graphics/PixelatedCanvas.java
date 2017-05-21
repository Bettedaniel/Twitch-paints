package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PixelatedCanvas extends JPanel {

    private BufferedImage bufferedImage;
    private final int width;
    private final int height;
    private final Map<String, Integer> colors = new HashMap<String, Integer>() {{
        put("BLACK", Color.BLACK.getRGB());
        put("WHITE", Color.WHITE.getRGB());
        put("RED", Color.RED.getRGB());
        put("GREEN", Color.GREEN.getRGB());
        put("BLUE", Color.BLUE.getRGB());
        put("YELLOW", Color.YELLOW.getRGB());
        put("GRAY", Color.GRAY.getRGB());
        put("ORANGE", Color.ORANGE.getRGB());
        put("MAGENTA", Color.MAGENTA.getRGB());
    }};

    public PixelatedCanvas(int width, int height) {
        this.width = width;
        this.height = height;
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
        this.setPreferredSize(new Dimension(width, height));
    }

    public void setColour(int x, int y, String colour) {
        if (colors.containsKey(colour)) {
            bufferedImage.setRGB(x, y, colors.get(colour));
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, this);
    }

}
