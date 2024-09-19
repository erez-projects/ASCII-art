package image;

import java.awt.*;
import java.io.IOException;

public class PaddedImage extends Image{
    private static final int LOG_BASE = 2;
    private static final int ARRAY_DIMENSION = 2;

    private static final int MAX_RGB = 255;
    private Color[][] paddedPixelArray;
    private int widthWithPadding;
    private int heightWithPadding;


    /**
     * Constructor for PaddedImage that takes a filename.
     *
     * @param filename the filename of the image to be loaded.
     * @throws IOException if an error occurs during reading the file.
     */
    public PaddedImage(String filename) throws IOException{
        super(filename);
        this.paddedPixelArray = this.imagePadding(extractPixelArray());
    }


    /**
     * Constructor for PaddedImage that takes a pixel array, width, and height.
     *
     * @param pixelArray the array of pixels representing the image.
     * @param width the width of the image.
     * @param height the height of the image.
     */
    public PaddedImage(Color[][] pixelArray, int width, int height) {
        super(pixelArray, width, height);
        this.paddedPixelArray = this.imagePadding(pixelArray);
    }

    /**
     * Divides the padded image into sub-images of a given resolution.
     *
     * @param resolution the resolution of the sub-images.
     * @return a 2D array of sub-images.
     */
    public Image[][] divideToSubImages(int resolution){
        int squareSize = this.widthWithPadding /resolution;
        int squaresPerCol = heightWithPadding / squareSize;
        Image[][] dividedImages = new Image[squaresPerCol][resolution];
        for(int i = 0; i < squaresPerCol; i++){
            for(int j = 0; j < resolution; j++){
                dividedImages[i][j] = new Image(extractSubImage(squareSize, i, j), squareSize, squareSize);
            }
        }
        return dividedImages;
    }


    private void calculateDimensionsWithPadding(){
        this.heightWithPadding = (int) Math.ceil(Math.log(super.getHeight())/Math.log(LOG_BASE));
        this.heightWithPadding = (int)Math.pow(2, this.heightWithPadding);
        this.widthWithPadding = (int) Math.ceil(Math.log(super.getWidth())/Math.log(LOG_BASE));
        this.widthWithPadding = (int)Math.pow(2, this.widthWithPadding);
    }

    private Color[][] imagePadding(Color[][] pixelArray){
        calculateDimensionsWithPadding();
        Color[][] newPixelArray = new Color[this.heightWithPadding][this.widthWithPadding];
        int heightDiff = (this.heightWithPadding - super.getHeight()) / ARRAY_DIMENSION;
        int widthDiff = (this.widthWithPadding - this.getWidth()) / ARRAY_DIMENSION;
        if(heightDiff == 0 && widthDiff == 0){
            return pixelArray;
        }
        for(int i = 0; i < this.heightWithPadding; i++){
            for(int j = 0; j < this.widthWithPadding; j++){
                if (i < heightDiff || j < widthDiff || i >= super.getHeight() + heightDiff || j >= super.getWidth() + widthDiff){
                    newPixelArray[i][j] = new Color(MAX_RGB, MAX_RGB, MAX_RGB);
                }
                else{
                    newPixelArray[i][j] = pixelArray[i - heightDiff][j - widthDiff];
                }
            }
        }
        return newPixelArray;
    }

    private Color[][] extractPixelArray(){
        Color[][] pixelArray = new Color[super.getHeight()][super.getWidth()];
        for(int x = 0; x < super.getHeight(); x++){
            for(int y = 0; y < super.getWidth(); y++){
                pixelArray[x][y] = super.getPixel(x, y);
            }
        }
        return pixelArray;

    }

    private Color[][] extractSubImage(int squareSize, int squareRow, int squareCol){
        Color[][] subImageArr = new Color[squareSize][squareSize];
        int topRow = squareRow * squareSize;
        int leftCol = squareCol * squareSize;
        for(int i = topRow; i < topRow + squareSize; i++){
            for(int j = leftCol; j < leftCol + squareSize; j++){
                subImageArr[i - topRow][j - leftCol] = this.paddedPixelArray[i][j];
            }
        }
        return subImageArr;
    }



}
