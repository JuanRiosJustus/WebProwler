package webprowler.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CollectionWindow extends Stage
{	
	private TableView<Object> table;
	private Scene scene;
	private FlowPane layout; // TODO maybe just do an aboslute pane (Pane pane)
	
	private Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
 	private int width = (int) screen.getWidth() * 9 / 13;
 	private int height = (int) screen.getHeight() * 5 / 6;
	
	public CollectionWindow()
	{
		initComponents();
	
		setScene(scene);
	}
	
	@SuppressWarnings("unchecked")
	private void initComponents()
	{
		layout = new FlowPane();
		
		table = new TableView<Object>();
		
		TableColumn<Object, Object> firstCol = new TableColumn<Object, Object>("Website");
	    TableColumn<Object, Object> secCol = new TableColumn<Object, Object>("is Relevant");
	    TableColumn<Object, Object> thirdCol = new TableColumn<Object, Object>("Email");
	    
	    table.getColumns().addAll(firstCol, secCol, thirdCol);
	    table.setPrefHeight(height*1.5);
	    //table.scroll
	    
	    layout.getChildren().add(table);
	    
	    scene = new Scene(layout, width, height);
	    
	    
	}
}