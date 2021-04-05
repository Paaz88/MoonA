package pzionit.lisenceplate;

import java.io.*;
import java.util.ArrayList;

public class FileReaderPlates {

    /**
     * read urls, line after line, from a given file
     *
     * @param filePath - file Path
     * @return url ArrayList from file
     */
    public static ArrayList<String> readPlates(String filePath) {
        ArrayList<String> urlList = new ArrayList<>();
        try {
            File file = new File(filePath);    //creates a new file instance
            FileReader fr = new FileReader(file);   //reads the file
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            String line;
            while ((line = br.readLine()) != null) {
                urlList.add(line);
            }
            fr.close();    //closes the stream and release the resources

        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlList;
    }
}


//
//https://3dwarehouse.sketchup.com/warehouse/v1.0/publiccontent/ca578273-50ee-4e74-993e-0a52a584756e
//        http://4liberty.eu/wp-content/uploads/2019/09/800px-Slovakia_2004_License_Plate_with_Euro_band_SA_335CO.jpg
//        https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/2020_New_York_Excelsior_License_Plate.jpg/1920px-2020_New_York_Excelsior_License_Plate.jpg
