package ehu.isad.controller.ui;

import ehu.isad.Book;
import ehu.isad.Liburuak;
import ehu.isad.controller.db.ZerbitzuKud;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class XehetasunakKud implements Initializable {

  private Liburuak mainApp;

  @FXML
  private Label lblIzenburua;


  @FXML
  private Label lblArgitaletxea;

  @FXML
  private Label lblOrriKop;

  @FXML
  private ImageView imgIrudia;

  @FXML
  private Button buttonAtzera;

  public void setMainApp(Liburuak main) {
    this.mainApp = main;
  }

  @FXML
  public void onClick(ActionEvent actionEvent) throws IOException {
    mainApp.mainErakutsi();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  public void putInfo(Book b, String isbn) throws IOException, SQLException {
    lblIzenburua.setText(b.getDetails().getTitle());
    lblOrriKop.setText(Integer.toString(b.getDetails().getNumber_of_pages()));
    lblArgitaletxea.setText(b.getDetails().getPublishers()[0]);
    //Ez badago kargatuta kargatu
    if (ZerbitzuKud.getInstance().liburuaKargatutaDago(isbn)==false) {
      imgIrudia.setImage(ZerbitzuKud.getInstance().createImage(b.getThumbnail_url().replace("S", "M")));
    }
    else {
        //Kargatuta dagoela badakigu
        imgIrudia.setImage(ZerbitzuKud.getInstance().irudialortu(b.getThumbnail_url()));
    }
  }
}