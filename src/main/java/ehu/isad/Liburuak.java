package ehu.isad;

import com.google.gson.Gson;
import ehu.isad.controller.ui.XehetasunakKud;
import ehu.isad.controller.db.ZerbitzuKud;
import ehu.isad.controller.ui.LiburuakKud;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

public class Liburuak extends Application {

  private Parent liburuUI;
  private Parent xehetasunUI;
  private Stage stage;
  private Scene sceneLiburuak;
  private Scene sceneXehetasunak;
  public LiburuakKud liburuakKud;
  public XehetasunakKud xehetasunakKud;
  private Book book;

  @Override
  public void start(Stage primaryStage) throws Exception {

    stage = primaryStage;
    fxmlKargatu();

    stage.setScene(sceneLiburuak);
    stage.show();
  }

  public void mainErakutsi(){
    stage.setScene(sceneLiburuak);
    stage.setX(750);
    stage.setY(250);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  public void liburuaLortu() throws IOException {
    Book b=(Book) liburuakKud.comboZerbitzua.getValue();
    readFromUrl(b.isbn);
  }

  public void xehetasunakErakutsi() throws IOException, SQLException {

    aukerak();
    stage.setScene(sceneXehetasunak);
    stage.setX(300);
    stage.setY((100));
    stage.show();
  }

  private void aukerak() throws SQLException, IOException {

    Book book=(Book) liburuakKud.comboZerbitzua.getValue();
    if(ZerbitzuKud.getInstance().liburuaKargatutaDago(book.getIsbn())==false){
      System.out.println("Datu basean kargatu egin behar da");
      liburuaLortu();
      String path=ZerbitzuKud.getInstance().irudiagorde(book.getIsbn(),this.book.getThumbnail_url().replace("S","M"));
      this.book.setThumbnail_url(path);
      ZerbitzuKud.getInstance().datuBaseanSartu(book,this.book);
      xehetasunakKud.putInfo(this.book,book.getIsbn());
    }
    else{
      Book book1=new Book(book.isbn,book.title);
      Book book2 = ZerbitzuKud.getInstance().KargatutakoLiburua(book1);
      System.out.println("Datu basean kargatuta dago");
      xehetasunakKud.putInfo(book2,book.getIsbn());
    }
  }

  private void fxmlKargatu() throws IOException {

    FXMLLoader loaderLiburu = new FXMLLoader(getClass().getResource("/Liburuak.fxml"));
    liburuUI = (Parent) loaderLiburu.load();
    liburuakKud = loaderLiburu.getController();
    liburuakKud.setMainApp(this);
    sceneLiburuak=new Scene(liburuUI);


    FXMLLoader loaderXehetasun = new FXMLLoader(getClass().getResource("/Xehetasunak.fxml"));
    xehetasunUI = (Parent) loaderXehetasun.load();
    xehetasunakKud= loaderXehetasun.getController();
    xehetasunakKud.setMainApp(this);
    sceneXehetasunak=new Scene(xehetasunUI);

  }

  public void readFromUrl(String isbn) throws IOException {
    URL api;
    String inputLine = "";

    try {
      api = new URL(" https://openlibrary.org/api/books?bibkeys=ISBN:"+isbn+"&jscmd=details&format=json");
      URLConnection yc = api.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
      inputLine = in.readLine();

      String[] zatiak = inputLine.split("ISBN:"+isbn+"\":");
      inputLine = zatiak[1].substring(0, zatiak[1].length()-1);

      Gson gson = new Gson();
      this.book = gson.fromJson(inputLine, Book.class);

      in.close();
    }
    catch (MalformedURLException e){
      e.printStackTrace();
    }
  }
}