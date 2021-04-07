package pzionit.lisenceplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

class MainClass {
    //Should be edited
    static final String RELATIVE_FILE_PATH = File.separator + "src" + File.separator + "pzionit" + File.separator +
            "lisenceplate" + File.separator + "plates.txt";
    static final String PLATE_FILE_PATH = System.getProperty("user.dir") + RELATIVE_FILE_PATH;

    public static final String EXIT_CODE_SUCCESS = "1";
    public static final String EXIT_CODE_PARTIAL = "2";
    public static final String EXIT_CODE_FAIL = "3";
    public static final String EXIT_CODE_ERROR = "4";
    public static final String EXIT_CODE_RECOGNIZE = "99";


    public static void main(String[] args) {
        ArrayList<String> urlList = FileReaderPlates.readPlates(PLATE_FILE_PATH);
        DBUtils.createDBTables();
        for (String imageUrl : urlList) {
            VehicleData vehicleData = new VehicleData(imageUrl);
            LicensePlatePostRequest licensePlate = new LicensePlatePostRequest();
            try {
                JSONObject res = licensePlate.sendPost(true, imageUrl, "eng");
                if (!validateResult(res)) {
                    System.out.println("Error with OCRExitCode of imageUrl: " + imageUrl);
                    continue;
                }
                JSONArray parsedResults = (JSONArray) res.get("ParsedResults");
                if (!extractLicensePlate(vehicleData, parsedResults)) {
                    System.out.println("Failed to extract license plate from image: " + imageUrl);
                    continue;
                }
                if (DBUtils.isAlreadyExist(DBUtils.DECISION_TABLE, vehicleData.getPlate())) {
                    DBUtils.insertTimeTable(vehicleData.getPlate(), vehicleData.getTimestamp().toString());
                    continue;
                }
                checkParkingAvailability(vehicleData);
                DBUtils.insertDataTable(vehicleData.getPlate(), vehicleData.getDecision().toString());
                DBUtils.insertTimeTable(vehicleData.getPlate(), vehicleData.getTimestamp().toString());
                System.out.println("decision about image was pushed to the db. imageUrl: " + imageUrl);
            } catch (Exception e) {
                System.out.println("There is an error while processing image, please check stacktrace. " + imageUrl);
                e.printStackTrace();
            }
        }
    }

    /**
     * Decide if a vehicle can enter.
     * Loop - if there is a pdf with more than ome page
     *
     * @param vehicleData - vehicleData obj
     */
    private static void checkParkingAvailability(VehicleData vehicleData) {
        if (isPublicVehicle(vehicleData)) {
            vehicleData.setDecision(false);
        } else if (isMilitaryVehicle(vehicleData)) {
            vehicleData.setDecision(false);
        } else if (StringUtils.noAB(vehicleData.getPlate())) {
            vehicleData.setDecision(false);
        } else {
            vehicleData.setDecision(true);
        }
    }

    /**
     * check if it's a Military Vehicle
     *
     * @param vehicleData - vehicleData obj
     * @return true if it is.
     */
    private static boolean isMilitaryVehicle(VehicleData vehicleData) {
        return vehicleData.getPlate().contains("L") || vehicleData.getPlate().contains("M");
    }

    /**
     * check if it's a Public Vehicle
     *
     * @param vehicleData - vehicleData obj
     * @return true if it is.
     */
    private static boolean isPublicVehicle(VehicleData vehicleData) {
        return vehicleData.getPlate().endsWith("6") || vehicleData.getPlate().endsWith("G");
    }

    /**
     * extract License Plate from result and set it in vehicleData obj
     * Assumption - pdf with more than 1 page is not allowed, only images. therefore the get(0)
     *
     * @param vehicleData   - vehicleData obj
     * @param parsedResults - result json obj
     * @return true if succeeded
     */
    private static boolean extractLicensePlate(VehicleData vehicleData, JSONArray parsedResults) {
        //No text recognize by the engine
        if (parsedResults.getJSONObject(0).get("ParsedText").toString().equals("")) {
            return false;
        }
        String textFromLine = getTheMainText(parsedResults);
        //There is no text in pic, or it's blur
        if (textFromLine.trim().equals("")) {
            return false;
        }
        String cleanTextFromPlate = StringUtils.removeSpecialChars(textFromLine);
        boolean isValidPlate = StringUtils.isAtLeastOneNum(cleanTextFromPlate) && StringUtils.isPlatePattern(cleanTextFromPlate);
        if (isValidPlate) {
            vehicleData.setPlate(cleanTextFromPlate);
            return true;
        } else return false;
    }

    /**
     * Get the biggest text from the image
     *
     * @param parsedResults - parsedResults
     * @return - the text with the max size
     */
    private static String getTheMainText(JSONArray parsedResults) {
        JSONArray Lines = parsedResults.getJSONObject(0).getJSONObject("TextOverlay").getJSONArray("Lines");
        BigDecimal maxSize = new BigDecimal(0);
        String textFromLine = "";
        for (Object line : Lines) {
            BigDecimal textSize = (BigDecimal) ((JSONObject) line).get("MaxHeight");
            String extractedText = (String) ((JSONObject) line).get("LineText");
            if (textSize.compareTo(maxSize) > 0) {
                maxSize = textSize;
                textFromLine = extractedText;
            }
        }
        return textFromLine;
    }

    /**
     * validate response code
     *
     * @param res - response
     * @return true if succeeded
     */
    private static boolean validateResult(JSONObject res) {
        String orcExitCode = res.get("OCRExitCode").toString();
        switch (orcExitCode) {
            case EXIT_CODE_SUCCESS:
                return true;
            case EXIT_CODE_PARTIAL:
                System.out.println("The image partially parsed");
                return false;
            case EXIT_CODE_FAIL:
                System.out.println("Fail to parse image");
                return false;
            case EXIT_CODE_RECOGNIZE:
                System.out.println("Unable to recognize the file type");
                return false;
            case EXIT_CODE_ERROR:
                boolean isError = Boolean.parseBoolean(res.get("IsErroredOnProcessing").toString());
                String errorMsg = "";
                if (isError) {
                    errorMsg = res.get("ErrorMessage").toString();
                }
                System.out.println("There was an error to parse image: \n" + errorMsg);
                return false;
        }
        System.out.println("Unknown error code, please check API doc: " + orcExitCode);
        return false;
    }

}