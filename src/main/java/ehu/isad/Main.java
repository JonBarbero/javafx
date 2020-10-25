package ehu.isad;

import ehu.isad.controller.ui.KautotuKud;
import ehu.isad.controller.ui.MainKud;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

  private Parent kautotuUI;
  private Parent mainUI;

  private Stage stage;

  private KautotuKud kautotuKud;
  private MainKud mainKud;


  @Override
  public void start(Stage primaryStage) throws Exception {

    stage = primaryStage;
    pantailakKargatu();

    stage.setTitle("Argazki Backup");
    stage.setScene(new Scene(kautotuUI, 550, 375));
    stage.show();
  }

  private void pantailakKargatu() throws IOException {

    FXMLLoader loaderKautotu = new FXMLLoader(getClass().getResource("/kautotu.fxml"));
    kautotuUI = (Parent) loaderKautotu.load();
    kautotuKud = loaderKautotu.getController();
    kautotuKud.setMainApp(this);

    FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("/main.fxml"));
    mainUI = (Parent) loaderMain.load();
    mainKud = loaderMain.getController();
    mainKud.setMainApp(this);
  }


  public static void main(String[] args) {
    launch(args);
  }

  public void mainErakutsi() {
    stage.setScene(new Scene(mainUI));
    stage.show();
  }
}
