package webprowler.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
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
    private TextField seedBar;
    private Button startButton;
    private TextField targetBar;
    private Button stopButton;
    private Button settingsButton;
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
    	entryPointsDropdown.setStyle("-fx-background-color: darkgray");
    	limitDropdown = new ComboBox<String>(TaskHandler.getLimits());
    	limitDropdown.getSelectionModel().selectFirst();
    	limitDropdown.setStyle("-fx-background-color: darkgray");
    	seedBar = new TextField("Entery term");
    	seedBar.setMinSize(width/20, height/75);
    	seedBar.setStyle("-fx-background-color: lightgray");
    	startButton = new Button();
    	startButton.setText("Start");
    	startButton.setPrefSize(width/15, height/80);
    	startButton.setStyle("-fx-background-color: darkgray");
    	targetBar = new TextField("Target term");
    	targetBar.setMinSize(width/20, height/75);
    	targetBar.setStyle("-fx-background-color: lightgray");
    	stopButton = new Button();
    	stopButton.setText("Stop");
    	stopButton.setPrefSize(width/15, height/80);
    	stopButton.setStyle("-fx-background-color: darkgray");
    	settingsButton = new Button("Settings");
    	settingsButton.setPrefSize(width/9, height/80);
    	settingsButton.setStyle("-fx-background-color: darkgray");
    	
    	statusDisplay = new TextArea();
    	statusDisplay.setPrefColumnCount(width/55);
    	statusDisplay.setPrefRowCount(height/60);
    	statusDisplay.setStyle("-fx-background-color: gray");
    	statusDisplay.setEditable(false);
    	queueDisplay = new TextArea();
    	queueDisplay.setPrefColumnCount(width/16);
    	queueDisplay.setPrefRowCount(height/60);
    	queueDisplay.selectPositionCaret(0);
    	queueDisplay.setEditable(false);
    	queueDisplay.setStyle("-fx-background-color: gray");
    	mainDisplay = new WebView();
    	mainDisplay.setPrefWidth(width/1.03);
    	mainDisplay.setPrefHeight(height/1.6);
    	mainDisplay.setStyle("-fx-background-color: gray");
    	engine = mainDisplay.getEngine();
    	timer = new ProwlTimer(0);
    	scene = new Scene(layout, width, height, Color.RED);
    	
    	// DATA STRUCTUES
    	queue = new ArrayList<Childsite>();
    	target = new StringBuilder();
    	
    	// OTHER OBJECTS
    	backend = new Backend(8);
    	random = new Random();
    	
    	queueDisplay.appendText("THE QUEUE . . . ");
		statusDisplay.appendText("THE STATUS . . . ");
    }
    /**
     * Set the attributes for each component
     * @param stage
     */
    private void AttributeManager(Stage stage)
    {
    	seedBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				seedBar.setText("");
			}
    	});
    	startButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override public void handle (ActionEvent e) {
    			System.out.println("Working");
    			
    			reset();
    			prowl();
    			target.append(seedBar.getText());
    		}
    	});
    	targetBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				targetBar.setText("");
			}
    	});
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    		@Override public void handle(WindowEvent event) {
    			Platform.exit();
    			System.exit(0);
    		}
    	});
    	settingsButton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	        Stage stage = new Stage();
    	        //Fill stage with content
    	        stage.show();
    	    }
    	});
    	
    	// Layout
    	layout.setPadding(new Insets(10, 10, 10, 10));
    	layout.getChildren().add(entryPointsDropdown);
    	layout.setHgap(5.0);
    	layout.getChildren().add(limitDropdown);
    	layout.setHgap(5.0);
    	layout.getChildren().add(seedBar);
    	layout.setHgap(5.0);
    	layout.getChildren().add(startButton);
    	layout.setHgap(5.0);
    	layout.getChildren().add(targetBar);
    	layout.setHgap(5.0);
    	layout.getChildren().add(stopButton);
    	layout.setHgap(5.0);
    	layout.getChildren().add(settingsButton);
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
				 queue.add(backend.createAndConnect(TaskHandler.determineEntry(entryPointsDropdown.getSelectionModel().getSelectedItem()) + seedBar.getText()));	 
				 // Main loop to be executed
				 while (queue.isEmpty() == false && queue.size() < limit)	
				 {
					 queue.add(backend.createAndConnect(queue.get(0).getSearchableURL()));
					 queue.remove(0);
				 }
				 System.out.println("Search Stopped");
			}
		}}).start();
	}
    // TODO
    public void refreshList()
	{
		new Thread(new Runnable() { public void run() 
		{
			while(true) {
				try {
					Thread.sleep(1000 * 30);
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
			final int temp = index;
			Platform.runLater( () -> queueDisplay.appendText("\n - " + queue.get(temp).getSearchableURL()));
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
