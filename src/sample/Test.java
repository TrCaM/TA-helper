package sample;

import java.io.IOException;

/**
 * Created by Gon on 2017-04-30.
 */
public class Test {
    public static void main(String[] args){
        System.out.println(args[0]);
        UnzipUtility u = new UnzipUtility();
        try {
            u.unzip("C:\\Users\\Vinh\\Desktop\\1406 TA\\Tri Cao.zip", "C:\\Users\\Vinh\\Desktop\\1406 TA\\Tri", 1);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
