package services;

import play.mvc.Http;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Picture {

    public static byte[] FileToByte (Http.MultipartFormData.FilePart<File> file) {

        FileInputStream fileInputStream = null;
        byte[] byteStream = new byte[(int) file.getFile ().length ()];
        try {
            fileInputStream = new FileInputStream (file.getFile ());
            fileInputStream.read (byteStream);
            fileInputStream.close ();
            return byteStream;
        } catch (Exception e) {
            return null;
        }
    }

    public static BufferedImage ByteToFile (byte[] byteStream, int option) {

        File f = new File("public\\pictures\\picture_author.jpg");
        if(f.exists()) {
            f.delete ();
        }

        try {
            InputStream in = new ByteArrayInputStream (byteStream);
            BufferedImage bImageFromConvert = ImageIO.read (in);
            if(option == 1)
            {
                ImageIO.write (bImageFromConvert, "jpg", new File ("public\\pictures\\picture_author.jpg"));
            }
            else
            {
                ImageIO.write (bImageFromConvert, "jpg", new File ("public\\pictures\\picture_book.jpg"));
            }

            return bImageFromConvert;
        } catch (IOException e) {
            return null;
        }
    }
}
