package ascii_art;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import exceptions.CharNotInCharSetException;
import image.PaddedImage;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

public class Shell {
    private static final int DEFAULT_RESOLUTION = 128;
    private static final char[] DEFAULT_CHAR_SET = new char[]{'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9'};
    private static final String DEFAULT_IMAGE_PATH = "images/cat.jpeg";
    private static final String EMPTY_STRING = "";
    private static final String EXIT_COMMAND = "exit";
    private static final String ARROWS = ">>>";
    private static final String CHARS_COMMAND = "chars";
    private static final String ADD_COMMAND = "add";
    private static final String ALL_COMMAND = "all";
    private static final String SPACE_COMMAND = "space";
    private static final int START_CHAR = 0;
    private static final int END_CHAR = 2;
    private static final char MIN_CHAR = ' ';
    private static final char MAX_CHAR = '~';
    private static final String INCORRECT_FORMAT_MESSAGE = "Did not add due to incorrect format.";
    private static final char RANGE_INDICATOR = '-';
    private static final int RANGE_INDICATOR_INDEX = 1;
    private static final String REMOVE_COMMAND = "remove";
    private static final String RES_COMMAND = "res";
    private static final String RESOLUTION_INFORAMTION_MESSAGE = "Resolution set to %d.\n";
    private static final String INCREASE_RES_COMMAND = "up";
    private static final String EXCEEDING_BOUNDARIES_MESSAGE = "Did not change resolution due" +
            " to exceeding boundaries.";
    private static final int CHANGE_RES_FACTOR = 2;
    private static final String DECREASE_RES_COMMAND = "down";
    private static final int RES_LOWER_BOUND = 1;
    private static final String INCORRECT_FORMAT_RES_MESSAGE = "Did not change resolution due to incorrect" +
            " format.";
    private static final String IMAGE_COMMAND = "image";
    private static final String INCORRECT_FORMAT_IMAGE_MESSAGE = "Did not change image method due to" +
            " incorrect format.";
    private static final String PROBLEM_WITH_IMAGE_FILE_MESSAGE = "Did not execute due to problem with" +
            " image file.";
    private static final String INCORRECT_FORMAT_OUTPUT_MESSAGE = "Did not change output method due to" +
            " incorrect format.";
    private static final String OUTPUT_COMMAND = "output";
    private static final String CONSOLE_COMMAND = "console";
    private static final String HTML_COMMAND = "html";
    private static final String OUT_HTML_FILENAME = "out.html";
    private static final String ASCII_ART_COMMAND = "asciiArt";
    private static final String OUTPUT_HTML_FONT = "Courier New";
    private static final int MIN_SIZE_CHAR_SET = 2;
    private static final String SMALL_CHAR_SET_MESSAGE = "Did not execute. Charset is too small.";
    private static final char SPACE = ' ';
    private static final String PROMPT_SPLIT_DELIMITER = " ";
    private static final int COMMAND_INDEX = 0;
    private static final int MIN_LEN_COMMAND = 2;
    private static final int MINIMAL_RES = 2;
    private static final int SECONDARY_COMMAND_INDEX = 1;
    private static final int SINGLE_CHAR_INDEX = 0;
    private static final int RANGE_COMMAND_LENGTH = 3;

    private final SubImgCharMatcher subImgCharMatcher;
    private PaddedImage paddedImage;
    private int resolution;
    private AsciiOutput output;


    /**
     * Constructs a new Shell object and initializes it with default values.
     *
     * @throws IOException if there is an error loading the default image.
     */
    public Shell() throws IOException {

        this.paddedImage = new PaddedImage(DEFAULT_IMAGE_PATH);

        this.subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHAR_SET);
        this.resolution = DEFAULT_RESOLUTION;
        this.output = new ConsoleAsciiOutput();

    }


    /**
     * Runs the main shell loop, processing user input and commands.
     * This method continuously prompts the user for input, splits the input into commands,
     * and executes the appropriate actions based on the commands. The following commands are supported:
     *   chars: Displays the current character set used for ASCII art.
     *   add: Adds a character or a range of characters to the character set.
     *   remove : Removes a character or a range of characters from the character set.
     *   res: Displays the current resolution.
     *   res up: Increases the resolution.
     *   res down: Decreases the resolution.
     *   image: Changes the image used for ASCII conversion to the specified path.
     *   output console: Sets the output method to console.
     *   output html: Sets the output method to HTML.
     *   asciiArt: Generates and outputs the ASCII art based on the current settings.
     *   exit: Exits the shell.
     *   Any other input results in an incorrect format message.
     */
    public void run(){
        String command = EMPTY_STRING;
        while(true){
            System.out.println(ARROWS);
            String prompt = KeyboardInput.readLine();
            String[] splitCommand = prompt.split(PROMPT_SPLIT_DELIMITER);
            command = splitCommand[COMMAND_INDEX];
            switch (command){
                case CHARS_COMMAND:
                    System.out.println(subImgCharMatcher.getCharSet());
                    break;
                case ADD_COMMAND:
                    if(splitCommand.length < MIN_LEN_COMMAND){
                        System.out.println(INCORRECT_FORMAT_MESSAGE);
                    }
                    else {
                        String toAdd = splitCommand[SECONDARY_COMMAND_INDEX];
                        addCommand(toAdd);
                    }

                    break;
                case REMOVE_COMMAND:
                    if(splitCommand.length < MIN_LEN_COMMAND){
                        System.out.println(INCORRECT_FORMAT_MESSAGE);
                    }
                    else {
                        String toRemove = splitCommand[SECONDARY_COMMAND_INDEX];
                        try{
                            removeCommand(toRemove);
                        }
                        catch (CharNotInCharSetException e){
                            System.out.println(e.getMessage());
                        }

                    }
                    break;
                case RES_COMMAND:
                    if(splitCommand.length == 1){
                        System.out.printf(RESOLUTION_INFORAMTION_MESSAGE, this.resolution);
                    }
                    else{
                        changeRes(splitCommand);
                    }

                    break;

                case IMAGE_COMMAND:
                    try{
                        changeImage(splitCommand);
                    }
                    catch (IOException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case OUTPUT_COMMAND:
                    changeOutput(splitCommand);
                    break;
                case ASCII_ART_COMMAND:
                        asciiArtCommand();
                    break;
                case EXIT_COMMAND:
                    return;
                default:
                    System.out.println(INCORRECT_FORMAT_MESSAGE);
            }



        }
    }

    private void addCommand(String toAdd) {
        if (toAdd.equals(ALL_COMMAND)) {
            subImgCharMatcher.addAllAsciiTable();
        } else if (toAdd.equals(SPACE_COMMAND)) {
            subImgCharMatcher.addChar(SPACE);
        } else if (toAdd.length() == RANGE_COMMAND_LENGTH
                && toAdd.charAt(RANGE_INDICATOR_INDEX) == RANGE_INDICATOR) {
            char start = toAdd.charAt(START_CHAR);
            char end = toAdd.charAt(END_CHAR);
            subImgCharMatcher.addRangeChars((char) Math.min(start, end), (char) Math.max(start, end));
        } else if (toAdd.length() == 1) {
            char charToAdd = toAdd.charAt(SINGLE_CHAR_INDEX);
            if (charToAdd >= MIN_CHAR && charToAdd <= MAX_CHAR) {
                subImgCharMatcher.addChar(charToAdd);
            }
        }
        else {
            System.out.println(INCORRECT_FORMAT_MESSAGE);
        }
    }

    private void removeCommand(String toRemove) throws CharNotInCharSetException {
        if (toRemove.equals(ALL_COMMAND)) {
            subImgCharMatcher.resetChar();
        } else if (toRemove.equals(SPACE_COMMAND)) {
            subImgCharMatcher.removeChar(' ');
        } else if (toRemove.length() == RANGE_COMMAND_LENGTH &&
                toRemove.charAt(RANGE_INDICATOR_INDEX) == RANGE_INDICATOR) {
            char start = toRemove.charAt(START_CHAR);
            char end = toRemove.charAt(END_CHAR);
            subImgCharMatcher.removeRangeChars((char) Math.min(start, end), (char) Math.max(start, end));
        } else if (toRemove.length() == 1) {
            char CharToRemove = toRemove.charAt(SINGLE_CHAR_INDEX);
            if (CharToRemove >= MIN_CHAR && CharToRemove <= MAX_CHAR) {
                subImgCharMatcher.removeChar(CharToRemove);
            }
        }
        else {
            System.out.println(INCORRECT_FORMAT_MESSAGE);
        }
    }

    private void changeRes(String[] splitCommand) {
        if(splitCommand[SECONDARY_COMMAND_INDEX].equals(INCREASE_RES_COMMAND)){
            if(paddedImage.getWidth() >= this.resolution * CHANGE_RES_FACTOR){
                this.resolution *= CHANGE_RES_FACTOR;
                System.out.printf(RESOLUTION_INFORAMTION_MESSAGE, this.resolution);
            }
            else {
                System.out.println(EXCEEDING_BOUNDARIES_MESSAGE);
            }
        } else if (splitCommand[SECONDARY_COMMAND_INDEX].equals(DECREASE_RES_COMMAND)) {
            int minCharsInRow = Math.max(RES_LOWER_BOUND, paddedImage.getWidth())/ paddedImage.getHeight();
            if (this.resolution / CHANGE_RES_FACTOR > minCharsInRow){
                this.resolution /= CHANGE_RES_FACTOR;
                System.out.printf(RESOLUTION_INFORAMTION_MESSAGE, this.resolution);
            }
            else{
                System.out.println(EXCEEDING_BOUNDARIES_MESSAGE);
            }
        }
        else{
            System.out.println(INCORRECT_FORMAT_RES_MESSAGE);
        }
    }

    private void changeImage(String[] splitCommand) throws IOException{
        if(splitCommand.length < MIN_LEN_COMMAND){
            System.out.println(INCORRECT_FORMAT_IMAGE_MESSAGE);
            return;
        }
        String imagePath = splitCommand[SECONDARY_COMMAND_INDEX];

        this.paddedImage = new PaddedImage(imagePath);
        if(this.paddedImage.getWidth() < this.resolution){
            this.resolution = MINIMAL_RES;
        }


    }

    private void changeOutput(String[] splitCommand) {
        if(splitCommand.length < MIN_LEN_COMMAND){
            System.out.println(INCORRECT_FORMAT_OUTPUT_MESSAGE);
            return;
        }
        if (splitCommand[1].equals(CONSOLE_COMMAND)){
            this.output = new ConsoleAsciiOutput();
        } else if (splitCommand[SECONDARY_COMMAND_INDEX].equals(HTML_COMMAND)) {
            this.output = new HtmlAsciiOutput(OUT_HTML_FILENAME, OUTPUT_HTML_FONT);
        }
        else{
            System.out.println(INCORRECT_FORMAT_OUTPUT_MESSAGE);
        }
    }

    private void asciiArtCommand() {
        if (this.subImgCharMatcher.getCharSet().size() < MIN_SIZE_CHAR_SET){
            System.out.println(SMALL_CHAR_SET_MESSAGE);
            return;
        }
        ascii_art.AsciiArtAlgorithm asciiArtAlgorithm =
                new ascii_art.AsciiArtAlgorithm(this.subImgCharMatcher, this.paddedImage, this.resolution);
        char[][] charImage = asciiArtAlgorithm.run();
        this.output.out(charImage);
    }


    /**
     * The main method that serves as the entry point of the application.
     * It initializes a Shell object and starts the shell loop.
     *
     * @param args command line arguments (not used).
     */
    public static void main(String[] args) {
        Shell shell = null;
        try {
            shell = new Shell();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
        shell.run();
    }
}


