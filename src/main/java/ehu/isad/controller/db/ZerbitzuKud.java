package ehu.isad.controller.db;

import ehu.isad.Book;
import ehu.isad.Details;
import ehu.isad.utils.Utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ZerbitzuKud {

    private static final ZerbitzuKud instance = new ZerbitzuKud();

    public static ZerbitzuKud getInstance() {
        return instance;
    }

    private ZerbitzuKud() {
    }

    public List<Book> lortuLiburuak() {

        String query = "select isbn,title from liburua";
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        ResultSet rs = dbKudeatzaile.execSQL(query);

        List<Book> emaitza = new ArrayList<>();
        try {
            while (rs.next()) {

                String kodea = rs.getString("isbn");
                String izena = rs.getString("title");
                System.out.println(kodea + ":" + izena);
                emaitza.add(new Book(kodea,izena));

            }
        } catch(SQLException throwables){
            throwables.printStackTrace();
        }

        return emaitza;
    }

    public boolean liburuaKargatutaDago(String isbn) throws SQLException {

        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        String query = "select orriKop from liburua where isbn='" + isbn + "'";
        ResultSet rs = dbKudeatzaile.execSQL(query);

        boolean emaitza=false;
        try {
            while (rs.next()) {
                int orriKopurua = rs.getInt("orriKop");
                if(orriKopurua>0){
                    emaitza=true;
                }
            }
        } catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return emaitza;
    }

    public Book KargatutakoLiburua(Book b) {

        Book liburua = b;
        Details details = new Details();
        liburua.setDetails(details);

        String query = "select isbn,title,orriKop,argitaletxea,irudia from liburua where isbn="+b.getIsbn()+";";
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        ResultSet rs = dbKudeatzaile.execSQL(query);

        try {
            while(rs.next()) {
                String kodea = rs.getString("isbn");
                String izena = rs.getString("title");
                int orriKop = rs.getInt("orriKop");
                String argitaletxea = rs.getString("argitaletxea");
                String irudia=rs.getString("irudia");

                liburua=datuakGorde(kodea,izena,irudia,orriKop,argitaletxea);
            }

        } catch(SQLException | IOException throwables){
            throwables.printStackTrace();
        }

        return liburua;
    }


    public Book datuakGorde(String kodea,String izena,String irudia,int orriKop,String argitaletxea) throws IOException, SQLException {

        Book book=new Book(kodea,izena);
        Details det = new Details();
        book.setDetails(det);

        book.setThumbnail_url(irudia);
        book.getDetails().setNumber_of_pages(orriKop);
        book.getDetails().setTitle(izena);

        String[] aux = new String[20];
        aux[0] = argitaletxea;
        book.getDetails().setPublishers(aux);

        return book;
    }
    public void datuBaseanSartu(Book b,Book details){
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        String query = "update liburua set orriKop = '"+details.getDetails().getNumber_of_pages()+"' , argitaletxea = '"+details.getDetails().getPublishers()[0].replace("\'", "''")+"' , irudia = '"+details.getThumbnail_url().replace("S","M")+"' where (isbn = '"+b.getIsbn()+"');";
        dbKudeatzaile.execSQL(query);
        System.out.println("Datu basean ondo kargatu da");
        System.out.println ("Ez da arazorik egon");
    }

    public Image createImage(String url) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        try (InputStream stream = conn.getInputStream()) {
            return new Image(stream);
        }
    }
    public String irudiagorde(String irudia,String kodea) throws IOException {
        Image img=createImage(irudia);
        return saveToFile(img,kodea);
    }

    private String saveToFile(Image img, String isbn) {
        Properties properties = Utils.lortuEzarpenak();
        File outputFile = new File(properties.getProperty("imagepath")+isbn+".jpg");
        BufferedImage bImage = SwingFXUtils.fromFXImage(img, null);
        try {
            ImageIO.write(bImage, "jpg", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputFile.getAbsolutePath();
    }

    public Image irudialortu(String path){
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Image image = SwingFXUtils.toFXImage(img, null);
        return image;
    }
}
