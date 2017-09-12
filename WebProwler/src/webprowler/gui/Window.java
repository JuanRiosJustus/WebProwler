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
import java.util.Collections;
import java.util.Random;

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
import webprowler.utility.ProwlTimer;
import webprowler.utility.TaskHandler;

public class Window
{
	// GUI COMPONENTS
	private JFrame window;
	private JPanel pane;
	private JComboBox<String> taskDisplay;
	private JComboBox<String> taskLimiter;
	private JTextArea searchBarDisplay;
	private JTextArea searchTargetDisplay;

	private static JTextArea statusDisplay;
	private static JTextArea queueDisplay;
	private JEditorPane mainDisplay;
	private JScrollPane scrollableMainDisplay;
	private JScrollPane scrollableStatusDisplay;
	private JScrollPane scrollableQueueDisplay;
	private JButton searchButton;
	
	// Backend
	private Backend backend;
	private static ProwlTimer timer;
	private Random random;
	
	// Main structure to store the Childsites
	private static ArrayList<Childsite> waitList;
	
	// Window dimension settings
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = (int) screen.getWidth() * 5 / 9;
	private int height = (int) screen.getHeight() * 5 / 6;
	
	private static int viewport = 0;
	private static int cleans = 0;
	private static int limit;
	private static int webTimer = 10;
	private static StringBuilder searchTerm;
	
	public Window(String appName)
	{
		initComponents();
		initGUI(appName);
	}
	/**
	 * Initialize all the needed objects for the GUI
	 */
	private void initComponents()
	{
		pane = new JPanel();
		
		taskDisplay = new JComboBox<>(TaskHandler.getTasks());
		taskLimiter = new JComboBox<>(TaskHandler.getLimit());
		
		searchBarDisplay = new JTextArea(height/250, width/75);
		searchTargetDisplay = new JTextArea(height/250, width/75);
		statusDisplay = new JTextArea(height/70, width/50);
		queueDisplay = new JTextArea(height/70, width/15);
		
		mainDisplay = new JEditorPane();
		scrollableMainDisplay = new JScrollPane(mainDisplay);
		scrollableStatusDisplay = new JScrollPane(statusDisplay);
		scrollableQueueDisplay = new JScrollPane(queueDisplay);
		
		searchButton = new JButton("Start");
		backend = new Backend(8);
		timer = new ProwlTimer(0);
		random = new Random();
		
		waitList = new ArrayList<Childsite>();
		searchTerm = new StringBuilder();
	}
	
	/**
	 * Construct the GUI
	 * @param appName the name of the application
	 */
	private void initGUI(String appName) 
	{	
		BufferedImage image = new BufferedImage(5, 5, 5);
		
		// window settings
		window = new JFrame(appName);
		window.setResizable(false);
		window.setSize(width, height);
		window.setIconImage(image);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		/* LISTENERS */
		searchBarDisplay.addFocusListener(new FocusListener() 
		{
			@Override
			public void focusGained(FocusEvent arg0) 
			{ 
				searchBarDisplay.setText(""); 
			}
			@Override
			public void focusLost(FocusEvent arg0) { }
		});
		searchButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{	
				clearAll();
				searchTerm.append(searchBarDisplay.getText());
				System.out.println(taskLimiter.getSelectedItem() + " is the limit");
				prowl();
				nextPage();
				refreshList();
			}
		});
		
		// Display settings for the displays
		searchBarDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		searchTargetDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		statusDisplay.setLineWrap(true);
		statusDisplay.setEditable(false);
		statusDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		queueDisplay.setLineWrap(true);
		queueDisplay.setEditable(false);
		queueDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainDisplay.setEditable(false);
		scrollableMainDisplay.setPreferredSize(new Dimension(width*27/28, height*5/8));
		scrollableMainDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		// Add components to pane
		pane.add(taskDisplay);
		pane.add(taskLimiter);
		pane.add(searchBarDisplay);
		pane.add(searchButton);
		pane.add(searchTargetDisplay);
		pane.add(scrollableStatusDisplay);
		pane.add(scrollableQueueDisplay);
		pane.add(scrollableMainDisplay);
		
		// Add pane to window
		window.getContentPane().add(pane);
		window.setVisible(true);
		queueDisplay.append("THE QUEUE . . . ");
		statusDisplay.append("THE STATUS . . . ");
	}
	/**
	 * Load the content for/to the main display
	 * @param url the String representing the URL 
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
	 * Main method which starts a new thread triggered by the search button
	 */
	public void prowl()
	{
		limit = TaskHandler.limit(taskLimiter.getSelectedItem());
		new Thread(new Runnable() { public void run() 
		{
			timer.resetTimer(0);
			// While the waitList (our queue) is not empty, add more to our waitList as long as the child isn't null.
			if (waitList.isEmpty())
			{
				// Add the starting point to the waitList
				 waitList.add(backend.createAndConnect(Entries.getGoogleProducts().get(0).getSearchableURL() + searchBarDisplay.getText()));	 
				 // Main loop to be executed
				 while (waitList.isEmpty() == false && waitList.size() < limit)	
				 {
					 waitList.add(backend.createAndConnect(waitList.get(0).getSearchableURL()));
					 waitList.remove(0);
				 }
				 System.out.println("Search Stopped");
			}
		}}).start();
	}
	// TO BE DETERMINED
	public void nextPage()
	{
		new Thread(new Runnable() { public void run() 
		{
			while(true) {
				try {
					Thread.sleep(1000 * webTimer);
					loadMainContent(waitList.get(random.nextInt(waitList.size())).getSearchableURL());
					queueDisplay.update(queueDisplay.getGraphics());
					System.out.println("WORDKING");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}}).start();
	}
	public void refreshList()
	{
		new Thread(new Runnable() { public void run() 
		{
			while(true) {
				try {
					Thread.sleep(1000 * 60);
					waitList.removeAll(Collections.singleton(null));
					waitList.trimToSize();
					cleans++;
					System.out.println("waitList had been cleared");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}}).start();
	}
	
	/**
	 * Reset all data/timers/objects
	 */
	private static void clearAll()
	{
		searchTerm.delete(0, searchTerm.length());
		waitList.clear();
		limit = 0;
		Backend.resetCollection();
		Backend.resetDuplicates();
		Backend.resetHashSet();
	}
	/**
	 * Update the queue display to match the amount of sites in the waitList 
	 * @return queueDisplay the updated queue display
	 */
	public static JTextArea getQueueDisplay() 
	{
		queueDisplay.setText("");
		queueDisplay.append("THE QUEUE . . . ");
		for (int index = 0; index < viewport; index++) 
		{
			queueDisplay.append("\n - " + waitList.get(index).getSearchableURL());
		}
		queueDisplay.setCaretPosition(queueDisplay.getDocument().getLength());
		return queueDisplay; 
	}
	/**
	 * Update the status display with all the newly updated variables
	 * @return statusDisplay the updated status display
	 */
	public static JTextArea getStatusDisplay() 
	{
		/* THE INITIAL QUEUE SIZE WIL ALWAYS BE GREATER SINCE ENTRY SITE */
		statusDisplay.setText("");
		statusDisplay.append("THE STATUS . . . ");
		statusDisplay.append("\n" + "Search term: " + searchTerm.toString());
		statusDisplay.append("\n" + "Search limit: " + limit);
		statusDisplay.append("\n" + "Queue size: " + waitList.size());
		statusDisplay.append("\n" + "Sites collected: " + Backend.getCollection());
		statusDisplay.append("\n" + "Perents visited: " + Backend.getDuplicates());
		statusDisplay.append("\n" + "Passed time: " + timer.getTime());
		statusDisplay.append("\n" + "Queue wipes: " + cleans);
		statusDisplay.append("\n" + "Reset Timer: " + webTimer);
		return statusDisplay;
	}
	/* GETTERS FOR BACKEND */
	public static ArrayList<Childsite> getWaitList() { return waitList; }
	public static int getViewPort() { return viewport; }
 	public static void setViewPortSize() { viewport++; }
}
