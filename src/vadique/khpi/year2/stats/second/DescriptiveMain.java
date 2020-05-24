package vadique.khpi.year2.stats.second;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DescriptiveMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
        	BorderPane root = FXMLLoader.load(getClass().getResource("DescriptiveWindow.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Regression Analysis and Descriptive Statistics");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static void main(String[] args) {
		launch(args);
	}

}
