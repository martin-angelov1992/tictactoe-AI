package coarseProject;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ClickBox extends JPanel {

	private byte x;
	private byte y;
	private Image background;
	private Border border;
	private Game game = Game.getInstance();

	private static Set<ClickBox> boxes = new HashSet<>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClickBox(int x, int y, View view) {
		this((byte)x, (byte)y, view);
	}

	public ClickBox(byte x, byte y, View view) {
		this.x = x;
		this.y = y;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
            	Position pos = Position.getPosition(ClickBox.this.x, ClickBox.this.y);
            	game.tryMakePlayerMove(pos);
            }
        });

        boxes.add(this);
	}

	@Override
    protected void paintComponent(Graphics g) {
	    super.paintComponent(g);

	    if (background != null) {
	    	g.drawImage(background, 0, 0, this);
	    }
	}

	public void putBackground(String fileName) {
		try {
			background = ImageIO.read(this.getClass().getClassLoader().getResource(fileName));
			Graphics g = this.getGraphics();
			g.drawImage(background, 0, 0, this);

			border = getBorder();
			setBorder(null);
			System.out.printf("Painting background %s for (%d, %d).\n", fileName, x, y);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clearBackground() {
		this.removeAll();

		background = null;

		if (border != null) {
			setBorder(border);
		}
	}

	public static Set<ClickBox> getBoxes() {
		return boxes;
	}

	public static ClickBox getBoxForPos(int x, int y) {
		for (ClickBox box : boxes) {
			if (box.x == x && box.y == y) {
				return box;
			}
		}

		throw new IllegalArgumentException("Box not found for ("+x+", "+y+").");
	}
}