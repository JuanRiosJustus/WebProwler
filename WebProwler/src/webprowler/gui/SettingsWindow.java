package webprowler.gui;

import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class SettingsWindow extends Stage
{
	private Scene scene;
	private FlowPane layout; // TODO maybe just do an aboslute pane (Pane pane)
	public SettingsWindow()
	{
		FlowPane layout = new FlowPane();
    	layout.setStyle("-fx-background-color: gray");
    	
    	setScene(scene);
	}
	private void initComponents()
	{
		//scene = new Scene(layout, width, height);
	}
}
