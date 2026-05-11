package uniquindio.edu.co.eventos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uniquindio.edu.co.eventos.util.DataSeeder;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        DataSeeder.cargarDatosIniciales();

        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("/uniquindio/edu/co/eventos/view/LoginView.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        stage.setTitle("Plataforma de Eventos - Universidad del Quindio");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
