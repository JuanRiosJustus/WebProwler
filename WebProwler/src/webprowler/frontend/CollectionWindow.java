package webprowler.frontend;

import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import webprowler.backend.Database;
import webprowler.objects.Childsite;

public class CollectionWindow extends Stage
{	
	private TableView<Childsite> table;
	private Scene scene;
	private VBox layout; // TODO maybe just do an aboslute pane (Pane pane)
	
	private TableColumn<Childsite, String> firstCol;
	private TableColumn<Childsite, String> secondCol;
	private TableColumn<Childsite, String> thirdCol;
	private TableColumn<Childsite, String> fourthCol;
	
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
 	private int width = (int) screen.getWidth() * 9 / 13;
 	private int height = (int) screen.getHeight() * 5 / 6;
	
 	/* Constructor */
	public CollectionWindow()
	{
		initComponents();
		eventListeners();
		
		setScene(scene);
	}
	private void initComponents()
	{
		layout = new VBox();
		
		firstCol = new TableColumn<Childsite, String>("Domain");
		firstCol.setMinWidth(width/7);
		firstCol.setCellValueFactory(new PropertyValueFactory<Childsite, String>("domainURL"));
		
		secondCol = new TableColumn<Childsite, String>("URL");
		secondCol.setMinWidth(width/3);
		secondCol.setCellValueFactory(new PropertyValueFactory<Childsite, String>("websiteURL"));
		
		thirdCol = new TableColumn<Childsite, String>("Score");
		thirdCol.setMinWidth(width/10);
		thirdCol.setCellValueFactory(new PropertyValueFactory<Childsite, String>("score"));
		
		fourthCol = new TableColumn<Childsite, String>("Locality");
		fourthCol.setMinWidth(width/2);
		fourthCol.setCellValueFactory(new PropertyValueFactory<Childsite, String>("locality"));
		
		table = new TableView<>();
	    table.getColumns().add(firstCol);
	    table.getColumns().add(secondCol);
	    table.getColumns().add(thirdCol);
	    table.getColumns().add(fourthCol);
	    table.setPrefHeight(height);
	    
	    layout.getChildren().add(table);
	    scene = new Scene(layout, width, height);
	}
	private void eventListeners() 
	{
		table.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override public void handle(KeyEvent arg0) {
		}});
	}
	public void updateTable()
	{
		ObservableList<Childsite> list = FXCollections.observableArrayList();
		for (Childsite child : Database.database_collection())
		{
			list.add(child);
		}
		table.setItems(list);
	}
}