package webprowler.frontend;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import webprowler.backend.Database;
import webprowler.handlers.TaskHandler;
import webprowler.objects.Childsite;
import webprowler.utilities.ProwlTimer;

public class MainWindow extends Application
{
	/* Necessary Components and objects */
    private FlowPane layout;
    private Scene scene;
    private Region newLine;
    private Random random; // TODO
    private PolicyWindow policy;
    private CollectionWindow collection;
    
    /* JavaFX GUI Components */
    private ComboBox<String> entryPointsDropdown;
    private ComboBox<String> limitDropdown;
    private TextField seedBar;
    private Button startButton;
    private TextField targetBar;
    private Button stopButton;
    private Button threadButton;
    private Button browserButton;
    private Button collectionButton;
    private Button policyButton;
    private static TextArea statusDisplay;
    private static TextArea queueDisplay;
    private static WebView mainDisplay;
    private static WebEngine engine;
    private static ProwlTimer timer;
    
    private static DecimalFormat formatter;
    
 	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
 	private int width = (int) screen.getWidth() * 9 / 13;
 	private int height = (int) screen.getHeight() * 5 / 6;
 	
 	private static ArrayList<Childsite> queue;
 	
 	/* Data structures, types, etc */
 	private static int viewport = 0;
	private static int limit;
	private static int threadCount = 0;
	private static boolean threadIsRunning;
	private static boolean terminateThreads;
	private static StringBuilder entryTerm;
	private static StringBuilder targetTerm;
	private static String currentSite = "";
    
 	/* Constructive method */
    public void start(Stage stage)
    {
    	ComponentsAndStructuresManager(stage);
    	ActionManager(stage);
    	
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * To initialize all components, objects, variables for the stage, scene, functions, and etc.
     * @param stage The container for all of the components. 
     */
    private void ComponentsAndStructuresManager(Stage stage)
    {
    	stage.setTitle("Web Prowler");
    	stage.setResizable(true);
    	layout = new FlowPane();
    	layout.setStyle("-fx-background-color: gray");
    	newLine = new Region();
    	newLine.setPrefSize(Double.MAX_VALUE, 0.0);
    	
    	// GUI COMPONENTS
    	entryPointsDropdown = new ComboBox<String>(TaskHandler.getEntryPoints());
    	entryPointsDropdown.getSelectionModel().selectFirst();
    	entryPointsDropdown.setStyle("-fx-background-color: darkgray");
    	
    	limitDropdown = new ComboBox<String>(TaskHandler.getLimits());
    	limitDropdown.getSelectionModel().select(3);
    	limitDropdown.setStyle("-fx-background-color: darkgray");
    	
    	seedBar = new TextField("Entery term");
    	seedBar.setMinSize(width/20, height/75);
    	seedBar.setStyle("-fx-background-color: lightgray");
    	
    	startButton = new Button();
    	startButton.setText("Start");
    	startButton.setPrefSize(width/20, height/80);
    	startButton.setStyle("-fx-background-color: darkgray");
    	
    	targetBar = new TextField("Target term");
    	targetBar.setMinSize(width/20, height/75);
    	targetBar.setStyle("-fx-background-color: lightgray");
    	
    	stopButton = new Button();
    	stopButton.setText("Stop");
    	stopButton.setPrefSize(width/20, height/80);
    	stopButton.setStyle("-fx-background-color: darkgray");
    	stopButton.setDisable(true);
    	
    	threadButton = new Button("New Thread");
    	threadButton.setPrefSize(width/10, height/80);
    	threadButton.setStyle("-fx-background-color: darkgray");
    	threadButton.setDisable(true);
    	
    	browserButton = new Button();
    	browserButton.setText("View");
    	browserButton.setPrefSize(width/20, height/80);
    	browserButton.setStyle("-fx-background-color: darkgray");
    	browserButton.setDisable(true);
    	
    	collectionButton = new Button();
    	collectionButton.setText("Table");
    	collectionButton.setPrefSize(width/20, height/80);
    	collectionButton.setStyle("-fx-background-color: darkgray");
    	
    	policyButton = new Button();
    	policyButton.setText("Policy");
    	policyButton.setPrefSize(width/20, height/80);
    	policyButton.setStyle("-fx-background-color: darkgray");
    	
    	policy = new PolicyWindow();
    	collection = new CollectionWindow();
    	
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
    	mainDisplay.isDisabled();
    	
    	engine = mainDisplay.getEngine();
    	formatter = new DecimalFormat(".##");
    	timer = new ProwlTimer(0);
    	scene = new Scene(layout, width, height);
    	queue = new ArrayList<Childsite>();
    	random = new Random();
    	
    	entryTerm = new StringBuilder();
    	targetTerm = new StringBuilder();
    	
    	threadIsRunning = false;
    	terminateThreads = false;
    }
    /**
     * To set the specific handlers and events for the GUI components.
     * @param stage The container for all the components.
     */
    private void ActionManager(Stage stage)
    {
    	seedBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				seedBar.setText("");
			}
    	});
    	startButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override public void handle (ActionEvent e) {
    			Database.database_clear();
    			reset();
    			setGUI();
    			prowl();
    		}
    	});
    	targetBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				targetBar.setText("");
			}
    	});
    	stopButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override public void handle (ActionEvent e) {
    			terminateThreads();
    		}
    	});
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    		@Override public void handle(WindowEvent event) {
    			Platform.exit();
    			System.exit(0);
    		}
    	});
    	threadButton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	    	auxProwl();
    	    }
    	});
    	browserButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override public void handle (ActionEvent e) {
    			webViewer();
				currentSite = engine.getLocation();
    		}
    	});
    	collectionButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override public void handle (ActionEvent e) {
    			Database.database_removeExcess();
    			collection.updateTable();
    			collection.show();
    		}
    	});
    	
    	/* Layout for the stage */
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
    	layout.getChildren().add(threadButton);
    	layout.setVgap(5.0);
    	layout.getChildren().add(browserButton);
    	layout.setHgap(5.0);
    	layout.getChildren().add(collectionButton);
    	layout.setVgap(5.0);
    	layout.getChildren().add(policyButton);
    	layout.setHgap(5.0);
    	layout.getChildren().add(newLine);
    	layout.setHgap(5.0);
    	layout.getChildren().add(statusDisplay);
    	layout.setHgap(5.0);
    	layout.getChildren().add(queueDisplay);
    	layout.setHgap(5.0);
    	layout.getChildren().add(mainDisplay);
    }
     /**
	 * The main crawler method which launches a new thread. 
	 * This thread acts as the main thread which holds special properties.
	 * This will crawl and load data to the Database class
	 * 
	 * @see #waitForOtherThreads() Will be the last thread to crawling thread to execute.
	 * @throws java.lang.ArrayIndexOutOfBoundsException If the queue has been emptied in another thread.
	 */
	public void prowl()
	{
		new Thread(new Runnable() { @Override public void run() 
		{
			System.out.println("THE MAIN THREAD HAS STARTED");
			limit = TaskHandler.limit(limitDropdown.getSelectionModel().getSelectedItem());
			threadIsRunning = true;
			threadCount++;
			if (Database.queue_isEmpty())
			{
				/* Initialize the queue with the user */
				Database.queue_enqueue(Database.createAndConnect(TaskHandler.determineEntry(entryPointsDropdown.getSelectionModel().getSelectedItem()) + seedBar.getText()));
				while (Database.queue_isEmpty() == false && Database.queue_size() <= limit && threadIsRunning && startButton.isDisabled() && !terminateThreads)	
				{
					refreshDisplays();
					Database.database_add(Database.queue_dequeue());
					Database.createAndConnect(Database.queue_dequeue().getSearchableURL());
					if (Database.queue_size() > limit) { terminateThreads = true; }
				}
				browserButton.setDisable(true);
				while (Database.queue_size() > 0)
				{
					Database.database_add(Database.queue_dequeue());
				}
			}
			threadCount--;
			waitForOtherThreads();
			System.out.println("THE MAIN THREAD HAS STOPPED");
		}}).start();
	}
	/**
	 * Auxiliary thread(s) used to expedite the crawling and loading processes.
	 * 
	 * @throws	java.lang.ArrayIndexOutOfBoundsException If the queue has been emptied in another thread.
	 */
	public void auxProwl()
	{
		new Thread(new Runnable() { @Override public void run() 
		{
			System.out.println("Auxiliary thread: " + ((Thread.activeCount() + threadCount) - Thread.activeCount()) + " has began.");
			threadCount++;
			if (Database.queue_isEmpty() == false)
			{
				while (Database.queue_isEmpty() == false && Database.queue_size() <= limit && threadIsRunning && startButton.isDisabled() && !terminateThreads)	
				{
					Database.database_add(Database.queue_dequeue());
					refreshDisplays();
					Database.createAndConnect(Database.queue_dequeue().getSearchableURL());
					if (Database.queue_size() > limit) { terminateThreads = true; }
				}
				browserButton.setDisable(true);
				while (Database.queue_size() > 0)
				{
					Database.database_add(Database.queue_dequeue());
				}
			}
			threadCount--;
			System.out.println("Auxiliary thread: " + ((Thread.activeCount() + threadCount) - Thread.activeCount()) + " has stopped.");
		}}).start();
	}
	// TODO
	private void webViewer()
	{
		engine.load(queue.get(0).getSearchableURL());
	}
	/**
	 * Waiting method for the main prowl thread.
	 * Checks that the increased thread count is back to 0.
	 */
	public void waitForOtherThreads()
	{
		new Thread(new Runnable() { @Override public void run() 
		{
			while(threadCount > 0 && queue.size() > 0)
			{
				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
				
				System.out.println("Waiting for thread(s)...");
			}
			Database.database_removeExcess();
			reset();
		}}).start();
	}
    /**
	 * Resets all necessary values, objects, variables, and etc. to their initial state.
	 */
	private void reset()
	{
		collection.updateTable();
		queue.clear();
		terminateThreads = false;
		threadIsRunning = false;
		entryTerm.delete(0, entryTerm.length());
		targetTerm.delete(0, targetTerm.length());
		Database.resetHits();
		Database.resetLoops();
		Database.resetBlacklist();;
		resetGUI();
		collection.updateTable();
		timer.resetTimer(0);
	}
	/**
	 * Resets all the GUI components to their initial state.
	 */
	private void resetGUI()
	{
		startButton.setDisable(false);
		stopButton.setDisable(true);
		threadButton.setDisable(true);
		seedBar.setDisable(false);
		targetBar.setDisable(false);
		browserButton.setDisable(true);
	}
	/**
	 * Sets all the GUI components to their working state.
	 */
	private void setGUI()
	{
		startButton.setDisable(true);
		stopButton.setDisable(false);
		threadButton.setDisable(false);
		seedBar.setDisable(true);
		targetBar.setDisable(true);
		browserButton.setDisable(false);
		entryTerm.append(seedBar.getText());
		targetTerm.append(targetBar.getText());
	}
	/**
	 * Helper method to end threads
	 */
	private void terminateThreads() { terminateThreads = true; }
	/**
	 * Helper method to update displays.
	 */
	public static void refreshDisplays()
    {
    	getQueueDisplay();
		getStatusDisplay();
    }
    /**
	 * Updates the queueDisplay in the GUI.
	 * TODO
	 * @return queueDisplay The queueDisplay.
	 */
	public static void getQueueDisplay()
	{
		Task<Void> task = new Task<Void>() {
			@Override protected Void call() throws Exception {
				Platform.runLater( () -> queueDisplay.clear());
				
				return null;
		}};
		new Thread(task).start();
	}
	/**
	 * Updates the statusDisplay in the GUI.
	 * @return statusDisplay the statusDisplay.
	 */
	public static void getStatusDisplay() 
	{
		Platform.runLater( () -> statusDisplay.clear());
		Platform.runLater( () -> statusDisplay.appendText("" + currentSite));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Entry term: " + entryTerm));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Search limit: " + limit));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Hits: " + Database.getHits()));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Crawling threads: " + threadCount));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Passed time: " + timer.getTime()));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Websites Collected: " + Database.database_size()));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Queue size: " + Database.queue_size()));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Loops detected: " + Database.getLoops()));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Sites/Second: " + formatter.format(((double)Database.database_size()/timer.getTime()))));
	}
	/* GETTERS FOR BACKEND */
 	public static String getTargetTerm () { return targetTerm.toString(); }
 	public static boolean getTerminateThreadStatus() { return terminateThreads; } 
}
