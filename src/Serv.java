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
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Serv {

private static String line;
    public static void main(String args[]) throws IOException, InterruptedException {

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

                System.out.println("0");
                ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                image = ImageIO.read(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String sdfs = sdf.toString();
            File outputfile = new File("C:\\Users\\vrh\\IdeaProjects\\demo\\tf_files\\PHOTO_FROM_APP\\image"+sdf+".png");
            ImageIO.write(image, "png", outputfile);
            Process p = null;
            Process p1 = null;
            StringBuilder stringBuilder=new StringBuilder();
            if (outputfile.exists()) {
                System.out.println("1");
                p1 = Runtime.getRuntime().exec("cmd /c start cmd.exe /K " +
                        "\"autocrop -i tf_files/PHOTO_FROM_APP -o tf_files/PHOTO_FROM_APP1 -w 400 -H 400 --facePercent 80 >faceCropResult.txt\"");
                System.out.println("2");
                p1.waitFor();
                Thread.sleep(2000);
                BufferedReader bufferedReader1 = new BufferedReader(new FileReader("faceCropResult.txt"));
                line = bufferedReader1.readLine();


                String ok = "";
                while (line != null) {

                    //stringBuilder1.append(line);
                    line = bufferedReader1.readLine();
                    if (line != null) {
                        ok = line;
                    }

                }
                System.out.println(ok);
                bufferedReader1.close();
                String isFaceCroped = " 0 files have been cropped";
                if (isFaceCroped.equals(ok)) {
                    stringBuilder.append("face not found");
                } else {

                    System.out.println("3");
                    p = Runtime.getRuntime().exec("cmd /c start cmd.exe /K " +
                            "\" python -m scripts.label_image" +
                            " --graph=tf_files\\retrained_graph.pb" +
                            " --image=C:\\Users\\vrh\\IdeaProjects\\demo\\tf_files\\PHOTO_FROM_APP1\\image" + sdf + ".png > output.txt\"");
                    System.out.println("4");
                    try {
                        p.waitFor();
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    System.out.println("4");
                    BufferedReader bufferedReader = new BufferedReader(new FileReader("output.txt"));
                    line = bufferedReader.readLine();


                    while (line != null) {

                        stringBuilder.append(line);
                        line = bufferedReader.readLine();
                        System.out.println(line);

                    }
                    bufferedReader.close();
                }
            }
            System.out.println(stringBuilder.toString());
            ss=serverSocket.accept();
            DataOutputStream dataOutputStream = new DataOutputStream(ss.getOutputStream());
            dataOutputStream.writeUTF(stringBuilder.toString());
            dataOutputStream.close();
            ss.close();
            System.out.println("ready steady");




        }


    }


}
