package com.dream.view;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private Image image;

	// 初始化时，加载的图片1.jpg
	public ImagePanel() {
//		try {
//			image = ImageIO.read(new File("Sample Pictures/1.jpg"));
//		} catch (IOException e) {
//			e.getStackTrace();
//		}
	}

	// 实现图片的更新
	public void loadPhoto(Image img) {
		image = img;
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image == null)
			return;

		int imageWidth = image.getWidth(this);
		int imageHeight = image.getHeight(this);

		// 将图片画在左上角
		g.drawImage(image, 0, 0, null);

	}
}