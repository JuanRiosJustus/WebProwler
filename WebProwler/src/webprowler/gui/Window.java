package webprowler.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import webprowler.utility.TASKHandler;

public class Window 
{
	// GUI COMPONENTS
	private JFrame window;
	private JPanel pane;
	private JComboBox<String> taskDisplay;
	private JComboBox<String> taskLimiter;
	private JTextArea searchBarDisplay;
	private JTextArea statusDisplay;
	private JTextArea queueDisplay;
	//private JTextPane mainDisplay;
	private JEditorPane mainDisplay;
	private JScrollPane scrollableMainDisplay;
	
	// Window dimensional settings
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = (int) screen.getWidth() * 5 / 12;
	private int height = (int) screen.getHeight() * 5 / 6;
	
	public Window(String appName)
	{
		initGUI(appName);
	}

	/* INITIALIZE OBJECTS HERE */
	private void initComponents()
	{
		pane = new JPanel();
		
		taskDisplay = new JComboBox<>(TASKHandler.getTasks());
		taskLimiter = new JComboBox<>(TASKHandler.getLimit());
		
		searchBarDisplay = new JTextArea(height/250, width/33);
		searchBarDisplay.setText("What would you like to find today?");
		statusDisplay = new JTextArea(height/70, width/20);
		queueDisplay = new JTextArea(height/70, width/30);
		
		mainDisplay = new JEditorPane();
		scrollableMainDisplay = new JScrollPane(mainDisplay);
	}
	private void initGUI(String appName) 
	{
		initComponents();
		
		BufferedImage image = new BufferedImage(5, 5, 5);
		
		// window settings
		window = new JFrame(appName);
		window.setVisible(true);
		window.setResizable(false);
		window.setSize(width, height);
		window.setIconImage(image);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// component settings
		searchBarDisplay.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				searchBarDisplay.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		statusDisplay.setEditable(false);
		queueDisplay.setEditable(false);
		mainDisplay.setEditable(false);
		scrollableMainDisplay.setPreferredSize(new Dimension(width*9/10, height*5/8));
		
		// add components to pane
		pane.add(taskDisplay);
		pane.add(taskLimiter);
		pane.add(searchBarDisplay);
		pane.add(statusDisplay);
		pane.add(queueDisplay);
		pane.add(scrollableMainDisplay);
		
		// add pane to window
		window.getContentPane().add(pane);
	}
	
	public void loadMainContent(String url)
	{
		try {
			mainDisplay.setPage(url);
		} catch (IOException ex) {
			mainDisplay.setContentType("text/html");
			mainDisplay.setText("<html> Could not load </html>");
		}
	}
}
