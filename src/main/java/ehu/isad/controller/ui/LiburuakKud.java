package ehu.isad.controller.ui;

import ehu.isad.Book;
import ehu.isad.Liburuak;
import ehu.isad.controller.db.ZerbitzuKud;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LiburuakKud implements Initializable {

  // Reference to the main application.
  private Liburuak mainApp;

  @FXML
  public ComboBox comboZerbitzua;


  @FXML
  private Button kautotuBotoia;


  public void setMainApp(Liburuak main) {
    this.mainApp = main;
  }

  @FXML
  public void onClick(ActionEvent actionEvent) throws IOException, SQLException {
    mainApp.xehetasunakErakutsi();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    ObservableList<Book> books = FXCollections.observableArrayList();
    books.addAll(ZerbitzuKud.getInstance().lortuLiburuak());

    comboZerbitzua.setItems(books);
    comboZerbitzua.getSelectionModel().selectFirst();
    comboZerbitzua.setEditable(false);

    comboZerbitzua.setConverter(new StringConverter<Book>() {
      @Override
      public String toString(Book book) {
        if (book==null)
          return "";
        return book.getTitle();
      }

      @Override
      public Book fromString(String string) {
        return null;
      }
    });

  }
}