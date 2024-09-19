package image_char_matching;

import exceptions.CharNotInCharSetException;

import java.util.*;

/**
 * SubImgCharMatcher class provides functionality to match sub-images to characters based on brightness.
 */

public class SubImgCharMatcher {
    private static final char MIN_CHAR = ' ';
    private static final char MAX_CHAR = '~';
    private static final String CHAR_NOT_IN_SET_MESSAGE = "char set does not contain ";
    private SortedMap<Character, Double> charBrightnessMapNormalized;
    private SortedMap<Character, Double> charBrightnessMap;
    double min, max;

    /**
     * Constructor for SubImgCharMatcher.
     *
     * @param charset Array of characters to be used for matching.
     */
    public SubImgCharMatcher(char[] charset){
        this.charBrightnessMap = new TreeMap<>();
        for (char c :charset ){
            this.charBrightnessMap.put(c, computeCharBrightness(c));
        }

        this.min = Collections.min(charBrightnessMap.values());
        this.max = Collections.max(charBrightnessMap.values());
        this.normalizeCharBrightness();

    }

    /**
     * Gets the set of characters in the normalized brightness map.
     *
     * @return Set of characters.
     */
    public Set<Character> getCharSet(){
        return this.charBrightnessMapNormalized.keySet();
    }


    /**
     * Adds a character to the charset.
     *
     * @param c Character to be added.
     */
    public void addChar(char c){
        double charBrightness = computeCharBrightness(c);
        boolean changedMinMax = charBrightness > this.max || charBrightness < this.min;
        this.charBrightnessMap.put(c, computeCharBrightness(c));
        if (charBrightness > this.max){
            this.max = charBrightness;
        }
        if (charBrightness < this.min){
            this.min = charBrightness;
        }
        if (changedMinMax){
            normalizeCharBrightness();
        }
        else{

            double normalizedCharValue = (charBrightness - this.min) / (this.max - this.min);
            this.charBrightnessMapNormalized.put(c, normalizedCharValue);
        }
    }

    /**
     * Removes a character from the charset.
     *
     * @param c Character to be removed.
     * @throws CharNotInCharSetException if the character is not found in the charset.
     */
    public void removeChar(char c) throws CharNotInCharSetException{
        if (!charBrightnessMap.containsKey(c)){

            throw new CharNotInCharSetException(CHAR_NOT_IN_SET_MESSAGE + c);
        }
        boolean changedMinMax = charBrightnessMap.get(c) > this.max || charBrightnessMap.get(c) < this.min;
        if(charBrightnessMap.get(c) == this.min){
            this.min = Collections.min(charBrightnessMap.values());
        }
        if(charBrightnessMap.get(c) == this.max){
            this.max = Collections.max(charBrightnessMap.values());
        }
        this.charBrightnessMap.remove(c);
        if (changedMinMax){
            normalizeCharBrightness();
        }
        else{
            this.charBrightnessMapNormalized.remove(c);
        }
    }

    /**
     * Resets the charset, clearing all characters.
     */
    public void resetChar(){
        this.charBrightnessMap.clear();
        this.charBrightnessMapNormalized.clear();
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
    }

    /**
     * Gets a character that matches the given brightness.
     *
     * @param brightness Brightness value to match.
     * @return Character that closely matches the brightness.
     */
    public char getCharByImageBrightness(double brightness)
    {
        double minDistance = Float.POSITIVE_INFINITY;
        char minChar = this.charBrightnessMapNormalized.firstKey();
        for(Map.Entry<Character, Double> entry : this.charBrightnessMapNormalized.entrySet()){
            double curDistance = Math.abs(entry.getValue() - brightness);
            if (curDistance < minDistance){
                minDistance = curDistance;
                minChar = entry.getKey();
            }
        }
        return minChar;
    }

    /**
     * Adds a range of characters to the charset.
     *
     * @param start Starting character of the range.
     * @param end Ending character of the range.
     */
    public void addRangeChars(char start, char end)
    {
        for (char i = start; i <= end; i++) {
            this.addChar(i);
        }
    }

    /**
     * Removes a range of characters from the charset.
     *
     * @param start Starting character of the range.
     * @param end Ending character of the range.
     * @throws CharNotInCharSetException if any character in the range is not found in the charset.
     */
    public void removeRangeChars(char start, char end) throws CharNotInCharSetException
    {
        for (char i = start; i <= end; i++) {
            this.removeChar(i);
        }
    }

    /**
     * Adds all ASCII characters to the charset.
     */
    public void addAllAsciiTable(){
        this.addRangeChars(MIN_CHAR, MAX_CHAR);
    }

    private double computeCharBrightness(char c){
        boolean [][]charBoolArray = CharConverter.convertToBoolArray(c);
        int whitePixels = countWhitePixels(charBoolArray);
        return whitePixels / Math.pow(CharConverter.DEFAULT_PIXEL_RESOLUTION, 2);
    }

    private int countWhitePixels(boolean [][]charBoolArray){
        int numWhitePixels = 0;
        for(int i = 0; i < charBoolArray.length; i++){
            for(int j = 0; j <charBoolArray[i].length; j++){
                if(charBoolArray[i][j]){
                    numWhitePixels++;
                }
            }
        }
        return numWhitePixels;
    }

    private void normalizeCharBrightness(){
        this.charBrightnessMapNormalized = new TreeMap<>();
        for(Map.Entry<Character, Double> entry : this.charBrightnessMap.entrySet()){
            double normalizedCharValue = (entry.getValue() - this.min) / (this.max - this.min);
            this.charBrightnessMapNormalized.put(entry.getKey(), normalizedCharValue);
        }

    }


}
