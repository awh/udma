package org.antispin.udma.ui.jfc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;

public class JItemTray extends JComponent {

	public static final int DEFAULT_ROWS = 0;
	public static final int DEFAULT_COLUMNS = 0;	
	public static final int DEFAULT_CELL_SIZE = 28;
	
	int rows;
	int columns;
	Icon backgroundIcon;
	
	public JItemTray() {
		this(DEFAULT_ROWS, DEFAULT_COLUMNS);
	}
	
	public JItemTray(int rows, int columns) {
		this(rows, columns, null);
	}

	public JItemTray(int rows, int columns, Icon backgroundIcon) {
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		setBackgroundIcon(backgroundIcon);
		setBackground(Color.WHITE);
		setOpaque(true);
		setTraySize(rows, columns);
	}
	
	public void setTraySize(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		updateSizes();
	}
	
	public void setBackgroundIcon(Icon backgroundIcon) {
		this.backgroundIcon = backgroundIcon;
	}
	
	public void paintComponent(Graphics g) {
		final Insets insets = getInsets();
		
				
		// overlay background icon
		if(backgroundIcon != null) {
			// fill background colour
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());

			backgroundIcon.paintIcon(this, g, insets.left, insets.top);
		} else {
			// render cells
			g.setColor(Color.LIGHT_GRAY);
			final int shrink = 2;
			for(int row = 0; row < rows; ++row) {
				for(int column = 0; column < columns; ++column) {
					final int x = insets.left + (column * DEFAULT_CELL_SIZE);
					final int y = insets.top + (row * DEFAULT_CELL_SIZE); 
					g.drawRect(x, y, DEFAULT_CELL_SIZE - shrink, DEFAULT_CELL_SIZE - shrink);
				}
			}
		}
	}
	
	private void updateSizes() {
		final Insets insets = getInsets();
		final int width = insets.left + (columns * DEFAULT_CELL_SIZE) + insets.right;
		final int height = insets.top + (rows * DEFAULT_CELL_SIZE) + insets.bottom;
		final Dimension size = new Dimension(width, height);
		
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);		
	}
	
}