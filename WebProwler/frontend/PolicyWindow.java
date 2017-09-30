package webprowler.frontend;

import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class PolicyWindow extends Stage
{
	
	// SHOW THE USER THE CURRENT POLITENESS POLICY
	//TODO http://www.example.com/robots.txt
	
	
	private Scene scene;
	//private FlowPane layout; // TODO maybe just do an aboslute pane (Pane pane)
	
	public PolicyWindow()
	{
		FlowPane layout = new FlowPane();
    	layout.setStyle("-fx-background-color: gray");
    	
    	setScene(scene);
	}
}
