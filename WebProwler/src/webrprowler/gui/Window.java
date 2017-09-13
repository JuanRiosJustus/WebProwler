package webrprowler.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import webprowler.backend.Backend;
import webprowler.handlers.TaskHandler;
import webprowler.objects.Childsite;
import webprowler.objects.Entries;
import webprowler.utilities.ProwlTimer;

public class Window extends Application
{
	// JavaFX Main Components
    private FlowPane layout;
    private Scene scene;
    private Region newLine;
    private Backend backend;
    private Random random;
    
    
    // JavaFX main GUI Components
    private ComboBox<String> entryPointsDropdown;
    private ComboBox<String> limitDropdown;
    private TextField searchBar;
    private Button searchButton;
    private TextField termBar;
    private Button stopButton;
    private TextArea power;
    private static TextArea statusDisplay;
    private static TextArea queueDisplay;
    private static WebView mainDisplay;
    private static WebEngine engine;
    private static ProwlTimer timer;
    
    // Window dimension settings
 	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
 	private int width = (int) screen.getWidth() * 5 / 9;
 	private int height = (int) screen.getHeight() * 5 / 6;
 	
 	private static ArrayList<Childsite> queue;
 	
 	private static int viewport = 0;
	private static int cleans = 0;
	private static int limit;
	private static int webTimer = 10;
	private static StringBuilder target;
    
 	/* MAIN METHOD */
 	
    public void start(Stage stage)
    {
    	ComponentsAndStructuresManager(stage);
    	AttributeManager(stage);
    	
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Initialize each component to have their attributes set
     * @param stage
     */
    private void ComponentsAndStructuresManager(Stage stage)
    {
    	stage.setTitle("Web Prowler");
    	stage.setResizable(false);
    	
    	layout = new FlowPane();
    	layout.setStyle("-fx-background-color: gray");
    	newLine = new Region();
    	newLine.setPrefSize(Double.MAX_VALUE, 0.0);
    	
    	// GUI COMPONENTS
    	entryPointsDropdown = new ComboBox<String>(TaskHandler.getEntryPoints());
    	entryPointsDropdown.getSelectionModel().selectFirst();
    	entryPointsDropdown.setStyle("-fx-background-color: gray");
    	limitDropdown = new ComboBox<String>(TaskHandler.getLimits());
    	limitDropdown.getSelectionModel().selectFirst();
    	limitDropdown.setStyle("-fx-background-color: gray");
    	searchBar = new TextField("Entry term");
    	searchBar.setMinSize(width/10, height/75);
    	searchBar.setStyle("-fx-background-color: gray");
    	searchButton = new Button();
    	searchButton.setText("Search");
    	searchButton.setStyle("-fx-background-color: gray");
    	termBar = new TextField("Exit term");
    	termBar.setMinSize(width/10, height/75);
    	termBar.setStyle("-fx-background-color: gray");
    	stopButton = new Button();
    	stopButton.setText("Stop");
    	stopButton.setStyle("-fx-background-color: gray");
    	power = new TextArea();
    	power.setPrefColumnCount(width/400);
    	power.setPrefRowCount(height/550);
    	power.setText("OFF");
    	power.setStyle("-fx-background-color: gray");
    	power.setEditable(false);
    	statusDisplay = new TextArea();
    	statusDisplay.setPrefColumnCount(width/55);
    	statusDisplay.setPrefRowCount(height/60);
    	statusDisplay.setStyle("-fx-background-color: gray");
    	statusDisplay.setEditable(false);
    	queueDisplay = new TextArea();
    	queueDisplay.setPrefColumnCount(width/18);
    	queueDisplay.setPrefRowCount(height/60);
    	queueDisplay.setEditable(false);
    	queueDisplay.setStyle("-fx-background-color: gray");
    	mainDisplay = new WebView();
    	mainDisplay.setPrefWidth(width/1.120);
    	mainDisplay.setPrefHeight(height/1.6);
    	mainDisplay.setStyle("-fx-background-color: gray");
    	engine = mainDisplay.getEngine();
    	engine.load("https://www,google.com/");
    	timer = new ProwlTimer(0);
    	scene = new Scene(layout, width, height, Color.RED);
    	
    	// DATA STRUCTUES
    	queue = new ArrayList<Childsite>();
    	target = new StringBuilder();
    	
    	// OTHER OBJECTS
    	backend = new Backend(8);
    	random = new Random();
    	
    	queueDisplay.appendText("THE QUEUE . . . ");
    	queueDisplay.appendText("\n Enter a 'Search Term' to begin");
		statusDisplay.appendText("THE STATUS . . . ");
    }
    /**
     * Set the attributes for each component
     * @param stage
     */
    private void AttributeManager(Stage stage)
    {
    	searchBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				searchBar.setText("");
			}
    	});
    	searchButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override public void handle (ActionEvent e) {
    			System.out.println("Working");
    			
    			reset();
    			prowl();
    			target.append(searchBar.getText());
    			power.setText("ON");
    		}
    	});
    	termBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				termBar.setText("");
			}
    	});
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    		@Override public void handle(WindowEvent event) {
    			Platform.exit();
    			System.exit(0);
    		}
    	});
    	
    	
    	// Layout
    	layout.setPadding(new Insets(10, 10, 10, 10));
    	layout.getChildren().add(entryPointsDropdown);
    	layout.setHgap(5.0);
    	layout.getChildren().add(limitDropdown);
    	layout.setHgap(5.0);
    	layout.getChildren().add(searchBar);
    	layout.setHgap(5.0);
    	layout.getChildren().add(searchButton);
    	layout.setHgap(5.0);
    	layout.getChildren().add(termBar);
    	layout.setHgap(5.0);
    	layout.getChildren().add(stopButton);
    	layout.setHgap(5.0);
    	layout.getChildren().add(power);
    	layout.setVgap(5.0);
    	layout.getChildren().add(newLine);
    	layout.setHgap(5.0);
    	layout.getChildren().add(statusDisplay);
    	layout.setHgap(5.0);
    	layout.getChildren().add(queueDisplay);
    	layout.setHgap(5.0);
    	layout.getChildren().add(mainDisplay);
    	
    }
    /**
	 * Main method which starts a new thread triggered by the search button
	 */
	public void prowl()
	{
		limit = TaskHandler.limit(limitDropdown.getSelectionModel().getSelectedItem());
		new Thread(new Runnable() { public void run() 
		{
			timer.resetTimer(0);
			// While the waitList (our queue) is not empty, add more to our waitList as long as the child isn't null.
			if (queue.isEmpty())
			{
				// Add the starting point to the waitList
				 queue.add(backend.createAndConnect(Entries.getGoogleProducts().get(0).getSearchableURL() + searchBar.getText()));	 
				 // Main loop to be executed
				 while (queue.isEmpty() == false && queue.size() < limit)	
				 {
					 queue.add(backend.createAndConnect(queue.get(0).getSearchableURL()));
					 queue.remove(0);
					 
				 }
				 System.out.println("Search Stopped");
			}
			power.setText("OFF");
		}}).start();
	}
    // TODO
    public void refreshList()
	{
		new Thread(new Runnable() { public void run() 
		{
			while(power.getText().equals("ON")) {
				try {
					Thread.sleep(1000 * 60);
					queue.removeAll(Collections.singleton(null));
					queue.trimToSize();
					cleans++;
					System.out.println("waitList had been cleared");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}}).start();
	}
    /**
	 * Reset all Structures, objects, and data for next crawl
	 */
	private static void reset()
	{
		limit = 0;
		queue.clear();
		target.delete(0, target.length());
		Backend.resetCollection();
		Backend.resetDuplicates();
		Backend.resetHashSet();
	}
    /**
	 * Update the queue display to match the amount of sites in the waitList 
	 * @return queueDisplay the updated queue display
	 */
	public static TextArea getQueueDisplay() 
	{
		queueDisplay.setText("");
		queueDisplay.appendText("THE QUEUE . . . ");
		for (int index = 0; index < viewport; index++) 
		{
			queueDisplay.appendText("\n - " + queue.get(index).getSearchableURL());
		}
		return queueDisplay; 
	}
	/**
	 * Update the status display with all the newly updated variables
	 * @return statusDisplay the updated status display
	 */
	public static TextArea getStatusDisplay() 
	{
		statusDisplay.setText("");
		statusDisplay.appendText("THE STATUS . . . ");
		statusDisplay.appendText("\n" + "Search term: " + target);
		statusDisplay.appendText("\n" + "Search limit: " + limit);
		statusDisplay.appendText("\n" + "Queue size: " + queue.size());
		statusDisplay.appendText("\n" + "Sites collected: " + Backend.getCollection());
		statusDisplay.appendText("\n" + "Perents visited: " + Backend.getDuplicates());
		statusDisplay.appendText("\n" + "Passed time: " + timer.getTime());
		statusDisplay.appendText("\n" + "Queue wipes: " + cleans);
		statusDisplay.appendText("\n" + "Reset Timer: " + webTimer);
		return statusDisplay;
	}
	/* GETTERS FOR BACKEND */
	public static ArrayList<Childsite> getQueue() { return queue; }
	public static int getViewPort() { return viewport; }
 	public static void setViewPortSize() { viewport++; }
}
