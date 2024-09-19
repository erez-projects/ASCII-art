package ascii_art;

import image.Image;
import image.PaddedImage;
import image_char_matching.SubImgCharMatcher;



/**
 * The AsciiArtAlgorithm class converts an image into ASCII art based on a specified resolution
 * and character set. It divides the image into sub-images and matches each sub-image to a character
 * based on brightness.
 */
public class AsciiArtAlgorithm {
    private SubImgCharMatcher subImgCharMatcher;
    private PaddedImage paddedImage;
    private Image[][]subImages;
    //we should remember to be effective and save the last run to save calculations


    /**
     * Constructs an AsciiArtAlgorithm object with the specified character matcher, image, and resolution.
     *
     * @param subImgCharMatcher the character matcher used to map image brightness to characters.
     * @param paddedImage the image to be converted into ASCII art.
     * @param resolution the resolution to divide the image into sub-images.
     */
    public AsciiArtAlgorithm(SubImgCharMatcher subImgCharMatcher, PaddedImage paddedImage, int resolution){
        this.subImgCharMatcher = subImgCharMatcher;
        this.paddedImage = paddedImage;
        this.subImages = this.paddedImage.divideToSubImages(resolution);

    }

    /**
     * Runs the ASCII art conversion algorithm. It divides the image into sub-images,
     * calculates the brightness
     * of each sub-image, and maps it to a character from the character set.
     *
     * @return a 2D character array representing the ASCII art.
     */
    public char[][] run(){
        char[][] charImage = new char[this.subImages.length][this.subImages[0].length];
        for(int i = 0; i < subImages.length; i++){
            for(int j = 0; j < subImages[i].length; j++){
                double blockBrightness = subImages[i][j].imageBrightness();
                charImage[i][j] = subImgCharMatcher.getCharByImageBrightness(blockBrightness);
            }
        }
        return charImage;
    }
}

