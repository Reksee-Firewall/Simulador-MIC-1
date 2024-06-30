package mic.projectjavafxml.sample;

import mic.projectjavafxml.backend.ObservableResourceFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/img/app_icon.png")));
        ObservableResourceFactory resourceFactory = ObservableResourceFactory.getInstance();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"), resourceFactory.getResources());
        primaryStage.setTitle("MIC-1");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setResizable(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
