package graphics;

import javax.swing.*;

public class Display extends JFrame {

    private final JFrame window;
    private final PixelatedCanvas pixelatedCanvas;

    public Display(PixelatedCanvas pixelatedCanvas) {
        this.pixelatedCanvas = pixelatedCanvas;
        this.window = new JFrame("Window");

        window.add(pixelatedCanvas);
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setColour(int x, int y, String colour) {
        pixelatedCanvas.setColour(x, y, colour);
        window.repaint();
    }

}
