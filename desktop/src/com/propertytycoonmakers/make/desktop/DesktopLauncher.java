package com.propertytycoonmakers.make.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.propertytycoonmakers.make.PropertyTycoon;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Property Tycoon";
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		config.width = (int)dimension.getWidth();
		config.height = (int)dimension.getHeight();
		config.resizable = false;
		config.addIcon("icons/128.png", Files.FileType.Internal);
		config.addIcon("icons/32.png", Files.FileType.Internal);
		config.addIcon("icons/16.png", Files.FileType.Internal);
		new LwjglApplication(new PropertyTycoon(), config);
	}
}