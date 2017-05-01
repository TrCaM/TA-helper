package sample;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.zip.*;

/**
 * Created by Tri Cao on 2017-04-30.
 * @author Tri Cao
 */

/*Adapt from a online tutorial: http://www.codejava.net/java-se/file-io/programmatically-extract-a-zip-file-using-java */
public class UnzipUtility {
    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
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
            if (!entry.isDirectory()){
                extractFile(zipIn, filePath);
                if (calls!= 0 && (FilenameUtils.getExtension(entry.getName())== "zip")){
                    unzip(filePath, FilenameUtils.getFullPathNoEndSeparator(filePath), calls-1);

                }

            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytes_in = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read= zipIn.read(bytes_in))!= -1){
            bos.write(bytes_in, 0 , read);
        }
        bos.close();
    }

}
