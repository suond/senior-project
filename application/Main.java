package application;
	
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		// constructing our scene
	    URL url = getClass().getResource("GUI_Scene.fxml");
	    AnchorPane pane = null;
		try {
			pane = FXMLLoader.load( url );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Scene scene = new Scene(pane);
	    
	    // setting the stage
	    primaryStage.setScene( scene );
	    primaryStage.setTitle( "Highway Saftey System" );
	    primaryStage.show();
	    
	  }
	
	public static void main(String[] args) {
		launch(args);
	}
}
