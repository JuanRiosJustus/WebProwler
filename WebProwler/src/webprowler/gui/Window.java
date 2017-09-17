package webprowler.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.sun.management.ThreadMXBean;

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
	/* Necessary Components and objects */
    private FlowPane layout;
    private Scene scene;
    private Region newLine;
    private Backend backend;
    private Random random;
    
    /* JavaFX GUI Components */
    private ComboBox<String> entryPointsDropdown;
    private ComboBox<String> limitDropdown;
    private TextField seedBar;
    private Button startButton;
    private TextField targetBar;
    private Button stopButton;
    private Button threadButton;
    private static TextArea statusDisplay;
    private static TextArea queueDisplay;
    private static WebView mainDisplay;
    private static WebEngine engine;
    private static ProwlTimer timer;
    
 	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
 	private int width = (int) screen.getWidth() * 5 / 9;
 	private int height = (int) screen.getHeight() * 5 / 6;
 	
 	private static ArrayList<Childsite> queue;
 	
 	/* Data structures, types, etc */
 	private static int viewport = 0;
	private static int visits = 0;
	private static int limit;
	private static int webTimer = 10;
	private static int threadCount = 0;
	private static boolean threadIsRunning;
	private static boolean cleaningThreads;
	private static StringBuilder entryTerm;
	private static StringBuilder targetTerm;
    
 	/* Constructive method */
    public void start(Stage stage)
    {
    	ComponentsAndStructuresManager(stage);
    	AttributeManager(stage);
    	
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Need to initialize all components and objects for the stage and scene
     * @param stage the container for all of the JavaFX components 
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
    	limitDropdown.getSelectionModel().select(3);
    	limitDropdown.setStyle("-fx-background-color: darkgray");
    	seedBar = new TextField("Entery term");
    	seedBar.setMinSize(width/20, height/75);
    	seedBar.setStyle("-fx-background-color: lightgray");
    	// disable enable seedbar
    	startButton = new Button();
    	startButton.setText("Start");
    	startButton.setPrefSize(width/18, height/80);
    	startButton.setStyle("-fx-background-color: darkgray");
    	targetBar = new TextField("Target term");
    	targetBar.setMinSize(width/20, height/75);
    	targetBar.setStyle("-fx-background-color: lightgray");
    	//enable disable target bar
    	stopButton = new Button();
    	stopButton.setText("Stop");
    	stopButton.setPrefSize(width/15, height/80);
    	stopButton.setStyle("-fx-background-color: darkgray");
    	stopButton.setDisable(true);
    	threadButton = new Button("New Thread");
    	threadButton.setPrefSize(width/9, height/80);
    	threadButton.setStyle("-fx-background-color: darkgray");
    	threadButton.setDisable(true);
    	
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
    	//TODO
    	timer = new ProwlTimer(0);
    	scene = new Scene(layout, width, height, Color.RED);
    	
    	
    	
    	queue = new ArrayList<Childsite>();
    	entryTerm = new StringBuilder();
    	targetTerm = new StringBuilder();
    	
    	threadIsRunning = false;
    	cleaningThreads = true;
    	
    	backend = new Backend(8);
    	random = new Random();
    	
    	queueDisplay.appendText("THE QUEUE . . . ");
		statusDisplay.appendText("THE STATUS . . . ");
    }
    /**
     * Need to set the attributes for each component
     * @param stage the main components for GUI
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
    			threadCount++;
    			prowl();
    			cleanup();
    			setGUI();
    		}
    	});
    	targetBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				targetBar.setText("");
			}
    	});
    	stopButton.setOnAction(new EventHandler<ActionEvent>() {
    		@Override public void handle (ActionEvent e) {
    			resetGUI();
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
    	    	threadCount++;
    	    	auxProwl();
    	    	
    	        //Stage stage = new Stage();
    	        //stage.show();
    	        //TODO
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
    	layout.getChildren().add(newLine);
    	layout.setHgap(5.0);
    	layout.getChildren().add(statusDisplay);
    	layout.setHgap(5.0);
    	layout.getChildren().add(queueDisplay);
    	layout.setHgap(5.0);
    	layout.getChildren().add(mainDisplay);
    	
    }
    /**
	 * Creation of the main thread used to determine the state of the GUI components and some data
	 */
	public void prowl()
	{
		new Thread(new Runnable() { @Override public void run() {
			System.out.println(" THE MAIN THREAD HAS STARTED");
				limit = TaskHandler.limit(limitDropdown.getSelectionModel().getSelectedItem());
				threadIsRunning = true;
				if (queue.isEmpty())
				{
					/* Initialize the queue with the user */
					 queue.add(backend.createAndConnect(TaskHandler.determineEntry(entryPointsDropdown.getSelectionModel().getSelectedItem()) + seedBar.getText()));
					 
					 while (queue.isEmpty() == false && queue.size() < limit && threadIsRunning && startButton.isDisabled())	
					 {
						 queue.remove(0);
						 visits++;
						 queue.add(backend.createAndConnect(queue.get(0).getSearchableURL()));
					 }
					 cleaningThreads = false;
					 waitForOtherThreads();
					 reset();
					 threadCount--;
					 System.out.println("THE MAIN THREAD HAS STOPPED");
				}
		}}).start();
	}
	/**
	 * Create a new thread to expedite crawling
	 */
	public void auxProwl()
	{
		new Thread(new Runnable() { @Override public void run() {
			System.out.println("Thread: " + ((Thread.activeCount() + threadCount) - Thread.activeCount()) + " has began.");
				if (queue.isEmpty() == false)
				{
					 while (queue.isEmpty() == false && queue.size() < limit && threadIsRunning && startButton.isDisabled())	
					 {
						 queue.remove(0);
						 visits++;
						 queue.add(backend.createAndConnect(queue.get(0).getSearchableURL()));
					 }
					 System.out.println("Thread: " + ((Thread.activeCount() + threadCount) - Thread.activeCount()) + " has stopped.");
					 threadCount--;
				}
		}}).start();
	}
	public void waitForOtherThreads()
	{
		new Thread(new Runnable() {
			@Override public void run() {
				// TODO Auto-generated method stub
				while(threadCount > 0)
				{
					try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
					System.out.println("Waiting for auxilary threads...");
				}
			}}).start();
	}
    /**
     * Clear the queue for a new search session
     */
    public void cleanup()
	{
    	new Thread(new Runnable() { public void run() 
		{
    		queue.clear();
		}}).start();
	}
    /**
	 * Need to reset all values for the status on the next crawl
	 */
	private void reset()
	{
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		visits = 0;
		threadCount = 0;
		queue.clear();
		timer.resetTimer(0);
		entryTerm.delete(0, entryTerm.length());
		Backend.resetCollection();
		Backend.resetDuplicates();
		Backend.resetHashSet();
		resetGUI();
		
	}
	/**
	 * Need to set the GUI to it's crawling state
	 */
	private void setGUI()
	{
		startButton.setDisable(true);
		stopButton.setDisable(false);
		threadButton.setDisable(false);
		seedBar.setDisable(true);
		targetBar.setDisable(true);
		entryTerm.append(seedBar.getText());
		targetTerm.append(targetBar.getText());
	}
	/**
	 * Need to set the GUI to it's non-crawling state
	 */
	private void resetGUI()
	{
		startButton.setDisable(false);
		stopButton.setDisable(true);
		threadButton.setDisable(true);
		threadIsRunning = false;
		seedBar.setDisable(false);
		targetBar.setDisable(false);
	}
    /**
	 * Need to update the queue display to match the amount of sites in the queue
	 * @return queueDisplay the updated queue display
     * @throws InterruptedException 
	 */
	public static TextArea getQueueDisplay()
	{
		Platform.runLater( () -> queueDisplay.setText(""));
		Platform.runLater( () -> queueDisplay.appendText("THE QUEUE . . . "));
		for (int index = 0; index < viewport; index++) 
		{
			int temp = 0 + index;
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
		Platform.runLater( () -> statusDisplay.setText(""));
		Platform.runLater( () -> statusDisplay.appendText("THE STATUS . . . "));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Search term: " + entryTerm));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Search limit: " + limit));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Queue size: " + queue.size()));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Sites collected: " + Backend.getCollection()));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Loops detected : " + Backend.getDuplicates()));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Websites traversed: " + visits));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Crawling threads: " + threadCount));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "All threads: " + Thread.activeCount()));
		Platform.runLater( () -> statusDisplay.appendText("\n" + "Passed time: " + timer.getTime()));
		return statusDisplay;
	}
	/* GETTERS FOR BACKEND */
	public static ArrayList<Childsite> getQueue() { return queue; }
	public static int getViewPort() { return viewport; }
 	public static void setViewPortSize() { viewport++; }
 	public static String getTargetTerm () { return targetTerm.toString(); }
}
