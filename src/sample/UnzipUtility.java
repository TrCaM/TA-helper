package sample;

import javafx.application.Platform;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.*;

/**
 * Created by Tri Cao on 2017-04-30.
 * @author Tri Cao
 */

/*Adapt from a online tutorial: http://www.codejava.net/java-se/file-io/programmatically-extract-a-zip-file-using-java */
public class UnzipUtility {

    Controller controller;

    public UnzipUtility(Controller controller){
        this.controller = controller;
        threads = new LinkedList<>();
    }


    /**
     * Size of the buffer to read/write data
     */
    private static final int NUM_THREAD = 50;
    private static final int BUFFER_SIZE = 4096;

    private LinkedList<Thread> threads;

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath the file to unzip
     * @param destDirectory the folder where the unzip files will be in
     * @param calls The number of recursive levels
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory, int calls) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()){
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while(entry != null){
            String filePath = destDirectory + File.separator + entry.getName();

            updateScreen(entry.getName());

            if (!entry.isDirectory()){
                extractFile(zipIn, filePath);
                if (calls!= 0 && (FilenameUtils.getExtension(entry.getName()).equals("zip"))){
//                    unzip(filePath, filePath.replace(".zip", ""), calls-1);
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            try {
                                unzip(filePath, filePath.replace(".zip", ""), calls== -1? -1:(calls-1));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (!threads.isEmpty()) {
//                                System.out.println(threads.toString());
                                threads.removeFirst().start();
                            }
                            new File(filePath).delete();
                        }
                    };
                    if (Thread.activeCount() <= NUM_THREAD) {
                        thread.start();
                    } else{
                        threads.addLast(thread);
                    }


                }

            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        if (controller instanceof MarkerController)
            ((MarkerController)controller).updateTable();
        zipIn.close();
//        System.out.println("done");
    }

    private synchronized void updateScreen(String filePath) {
        if (controller instanceof UnzipController)
            Platform.runLater(() -> ((UnzipController)controller).getLoadingField().appendText("Unzipping: " + filePath + "\n"));
        else{
            if (FilenameUtils.getExtension(filePath).equals("zip")){
                String name = filePath.substring(0, filePath.indexOf('_')).replaceAll("-", " ");
                Platform.runLater(() -> ((MarkerController)controller).addRecord(name));
            }
        }
    }

    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        byte[] bytes_in = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read= zipIn.read(bytes_in))!= -1){
            bos.write(bytes_in, 0 , read);
        }
        bos.close();
    }

}
