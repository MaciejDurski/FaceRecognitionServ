import com.sun.xml.internal.messaging.saaj.util.Base64;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Serv {

private static String line;
    public static void main(String args[]) throws IOException {

        ServerSocket serverSocket = new ServerSocket(839);


        while (true) {

            Socket ss = serverSocket.accept();
            InputStreamReader isr = new InputStreamReader(ss.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            String currentLine;
            String encodedImage = "";


            while ((currentLine = br.readLine()) != null) {
                encodedImage = encodedImage + currentLine;

            }

            isr.close();
            br.close();
            ss.close();



            BufferedImage image = null;
            byte[] imageByte;
            try {
                BASE64Decoder decoder = new BASE64Decoder();
                imageByte = decoder.decodeBuffer(encodedImage);

                //System.out.println(imageByte.length);
                ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                image = ImageIO.read(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            File outputfile = new File("C:\\Users\\vrh\\Desktop\\staz\\image.png");
            ImageIO.write(image, "png", outputfile);
            Process p = null;
            if (outputfile.exists()) {
                p = Runtime.getRuntime().exec("cmd /c start cmd.exe /K " +
                        "\" python -m scripts.label_image" +
                        " --graph=tf_files\\retrained_graph.pb" +
                        " --image=C:\\Users\\vrh\\Desktop\\staz\\image.png > output.txt\"");

                try {
                    p.waitFor();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader("output.txt"));
            line = bufferedReader.readLine();


            StringBuilder stringBuilder = new StringBuilder();

            while (line != null) {

                stringBuilder.append(line);
                line = bufferedReader.readLine();
                System.out.println(line);

            }

            ss=serverSocket.accept();
            DataOutputStream dataOutputStream = new DataOutputStream(ss.getOutputStream());
            dataOutputStream.writeUTF(stringBuilder.toString());
            dataOutputStream.close();
            ss.close();
            System.out.println("ready steady");




        }


    }


}
