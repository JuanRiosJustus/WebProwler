package webprowler.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import webprowler.backend.Backend;
import webprowler.objects.Childsite;
import webprowler.objects.Entries;
import webprowler.utility.TASKHandler;

public class Window
{
	// GUI COMPONENTS
	private JFrame window;
	private JPanel pane;
	private JComboBox<String> taskDisplay;
	private JComboBox<String> taskLimiter;
	private JTextArea searchBarDisplay;
	private static JTextArea statusDisplay;
	private static JTextArea queueDisplay;
	private JEditorPane mainDisplay;
	private JScrollPane scrollableMainDisplay;
	private JScrollPane scrollableStatusDisplay;
	private JScrollPane scrollableQueueDisplay;
	private JButton searchButton;
	
	//Backend
	private Backend backend;
	private static ArrayList<Childsite> waitList;
	
	// Window dimensional settings
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = (int) screen.getWidth() * 5 / 11;
	private int height = (int) screen.getHeight() * 5 / 6;
	
	private int linksAggregated;
	
	public Window(String appName)
	{
		initGUI(appName);
	}
	/**
	 * INITIALIZE OBJECTS 
	 */
	private void initComponents()
	{
		pane = new JPanel();
		
		taskDisplay = new JComboBox<>(TASKHandler.getTasks());
		taskLimiter = new JComboBox<>(TASKHandler.getLimit());
		
		searchBarDisplay = new JTextArea(height/250, width/33);
		statusDisplay = new JTextArea(height/70, width/50);
		queueDisplay = new JTextArea(height/70, width/15);
		
		mainDisplay = new JEditorPane();
		scrollableMainDisplay = new JScrollPane(mainDisplay);
		scrollableStatusDisplay = new JScrollPane(statusDisplay);
		scrollableQueueDisplay = new JScrollPane(queueDisplay);
		
		searchButton = new JButton("Start");
		backend = new Backend();
		waitList = new ArrayList<Childsite>();
	}
	/**
	 * CONSTRUCTION OF THE BASE GUI
	 * @param appName name of application
	 */
	private void initGUI(String appName) 
	{
		initComponents();
		
		BufferedImage image = new BufferedImage(5, 5, 5);
		
		// window settings
		window = new JFrame(appName);
		window.setResizable(false);
		window.setSize(width, height);
		window.setIconImage(image);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		// component settings
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
	              prowl();
	         }
		});
		searchBarDisplay.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				searchBarDisplay.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		
		// display limiters and features
		searchBarDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		statusDisplay.setLineWrap(true);
		statusDisplay.setEditable(false);
		statusDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		queueDisplay.setLineWrap(true);
		queueDisplay.setEditable(false);
		queueDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainDisplay.setEditable(false);
		//scrollableStatusDisplay.
		scrollableMainDisplay.setPreferredSize(new Dimension(width*27/28, height*5/8));
		scrollableMainDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		//loadMainContent(Entries.getGoogleProducts().get(0).getNonSearchableURL());
		
		// add components to pane
		pane.add(taskDisplay);
		pane.add(taskLimiter);
		pane.add(searchBarDisplay);
		pane.add(searchButton);
		pane.add(scrollableStatusDisplay);
		pane.add(scrollableQueueDisplay);
		pane.add(scrollableMainDisplay);
		
		// add pane to window
		window.getContentPane().add(pane);
		
		getQueueDisplay();
		getStatusDisplay();
	}
	/**
	 * Load the main content for the main display
	 * TODO
	 * @param url - String representing the url to be displayed
	 */
	public void loadMainContent(String url)
	{
		try {
			mainDisplay.setPage(url);
		} catch (IOException ex) {
			mainDisplay.setContentType("text/html");
			mainDisplay.setText("<html> Could not load </html>");
		}
	}
	/**
	 * MAIN METHOD TRIGGERED BY SEARCH BUTTON
	 */
	public void prowl()
	{
		new Thread(new Runnable() 
		{ 
			public void run() 
			{	
				if (waitList.isEmpty())
				{
					System.out.println(" - - - - - NEW THREAD CREATED - - - - - ");
			        waitList.add(backend.createAndConnect(Entries.getGoogleProducts().get(0).getSearchableURL() + searchBarDisplay.getText()));
				} else {
					while (!waitList.isEmpty())
					{
						System.out.println("Current site: " + waitList.get(0).getSearchableURL());
						waitList.add(backend.createAndConnect(waitList.get(0).getSearchableURL()));
						waitList.remove(0);
					}
				}
		    	 //TODO
				
		        
		        //loadMainContent(Entries.getGoogleProducts().get(0).getNonSearchableURL());
		        //loadMainContent(waitList.get(0).getSearchableURL());
		     }
		}).start();
	}
	//TODO
	/**
	 * Get the main display's queue and add all the changes 
	 * @return queueDisplay The display for the "waitList" on the main window
	 */
	public static JTextArea getQueueDisplay() 
	{
		queueDisplay.setText("");
		queueDisplay.append("THE QUEUE . . . ");
		System.out.println("Current waitlist size: " + waitList.size());
		for (int index = 0; index < waitList.size(); index++) 
		{
			//System.out.println("\n WOW" + waitList.get(index).getTitle());
			queueDisplay.append("\n - " + waitList.get(index).getSearchableURL());
		}
		//queueDisplay.setCaretPosition(queueDisplay.getDocument().getLength());
		return queueDisplay; 
	}
	/**
	 * Get the main display's status display and add all the changes
	 * @return statusDisplay The display for the status of the main window
	 */
	public static JTextArea getStatusDisplay() 
	{
		statusDisplay.setText("");
		statusDisplay.append("THE STATUS . . . ");
		
		
		return statusDisplay;
	}
	/* GET THE WAITLIST */
	public static ArrayList<Childsite> getWaitList() { return waitList; }
}
