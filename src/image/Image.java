package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents an image with a 2D array of pixels.
 * Provides methods to read an image from a file, access pixel data, and save the image to a file.
 * This class is intended for internal use within the `image` package.
 *
 * @author Dan Nirel
 */

public class Image {
    private static final double RED_COEFFICIENT = 0.2126;
    private static final double GREEN_COEFFICIENT = 0.7152;
    private static final double BLUE_COEFFICIENT = 0.0722;
    private static final int MAX_RGB = 255;
    private final Color[][] pixelArray;
    private final int width;
    private final int height;

    /**
     * Constructs an Image object by reading the image from the specified file.
     *
     * @param filename the path to the image file.
     * @throws IOException if an error occurs during reading the image file.
     */
    public Image(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        width = im.getWidth();
        height = im.getHeight();


        pixelArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j]=new Color(im.getRGB(j, i));
            }
        }

    }

    /**
     * Constructs an Image object from the given pixel array, width, and height.
     *
     * @param pixelArray a 2D array of Color objects representing the pixels of the image.
     * @param width the width of the image.
     * @param height the height of the image.
     */
    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;


    }
    /**
     * Returns the width of the image.
     *
     * @return the width of the image.
     */
    public int getWidth() {
        return width;
    }
    /**
     * Returns the height of the image.
     *
     * @return the height of the image.
     */
    public int getHeight() {
        return height;
    }


    /**
     * Returns the color of the pixel at the specified coordinates.
     *
     * @param x the x-coordinate of the pixel.
     * @param y the y-coordinate of the pixel.
     * @return the color of the pixel at the specified coordinates.
     */
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }


    /**
    /**
     * Saves the image to a file with the specified name in JPEG format.
     *
     * @param fileName the name of the file to save the image to (without extension).
     */

    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName+".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculates the average brightness of the image.
     * Assumes the image is represented by a 2D array of pixels.
     * Uses the turnToGrey method to convert each pixel to grayscale.
     *
     * @return The average brightness of the image, normalized to [0, 1].
     */
    public double imageBrightness(){
        double imageBrightness = 0;
        for(int i  = 0; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                imageBrightness += turnToGrey(this.pixelArray[i][j]);
            }
        }
        return imageBrightness / (this.height * this.width * MAX_RGB);
    }

    private static double turnToGrey(Color color){
        return color.getRed() * RED_COEFFICIENT +
                color.getGreen() * GREEN_COEFFICIENT + color.getBlue() * BLUE_COEFFICIENT;
    }



}
