package webprowler.gui;

import javax.swing.JFrame;

public class Window 
{
	JFrame mainWindow = new JFrame("Main window");
	
	public Window()
	{
		initWindow();
	}

	private void initWindow() {
		mainWindow.setSize(1280, 720);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
