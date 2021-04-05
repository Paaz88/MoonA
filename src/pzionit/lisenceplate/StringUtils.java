package pzionit.lisenceplate;

public class StringUtils {

    /**
     * Check if there are no alphabet in a string
     *
     * @param str - string
     * @return - true if there are no alphabet
     */
    public static boolean noAB(String str) {
        String regex = "[^a-zA-Z]+";
        return str.matches(regex);

    }

    /**
     * Removes special chars
     *
     * @param inputStr - inputStr
     * @return - str without special chars
     */
    public static String removeSpecialChars(String inputStr) {
        return inputStr.replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * check if it's 6 to 8 chars of alphabet+nums
     *
     * @param inputStr - input String
     * @return true if it's the pattern
     */
    static boolean isPlatePattern(String inputStr) {
        return inputStr.matches("^[a-zA-Z0-9_]{6,8}$");
    }

    /**
     * check if it contains al least 1 number
     *
     * @param inputStr - input String
     * @return true if it's the pattern
     */
    public static boolean isAtLeastOneNum(String inputStr) {
        return inputStr.matches(".*[0-9].*");
    }
}
