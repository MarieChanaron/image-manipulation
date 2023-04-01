import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageProcessing {

    public static void main(String[] args) {

        // MANIPULATING IMAGES

        // The provided images are apple.jpg, flower.jpg, and kitten.jpg
        int[][] appleImageData = imgToTwoD("./images/apple.jpg");
        int[][] flowerImageData = imgToTwoD("./images/flower.jpg");
        int[][] kittenImageData = imgToTwoD("./images/kitten.jpg");

        // Or load your own image using a URL!
        // int[][] imageData = imgToTwoD("https://content.codecademy.com/projects/project_thumbnails/phaser/bug-dodger.png");

        // viewImageData(imageData);

        // Trim an image
        int[][] trimmedApple = trimBorders(appleImageData, 60);
        twoDToImage(trimmedApple, "./images/trimmed-apple.jpg");

        // Convert image colors into negative colors
        int[][] negativeApple = negativeColor(appleImageData);
        twoDToImage(negativeApple, "./images/negative-apple.jpg");

        // Stretch horizontally an image
        int[][] stretchedKitten = stretchHorizontally(kittenImageData);
        twoDToImage(stretchedKitten, "./images/stretched-kitten.jpg");

        // Shrink vertically an image
        int[][] shrinkedFlower = shrinkVertically(flowerImageData);
        twoDToImage(shrinkedFlower, "./images/shrinked-flower.jpg");

        // Flip an image both horizontally and vertically
        int[][] invertedFlower = invertImage(flowerImageData);
        twoDToImage(invertedFlower, "./images/inverted-flower.jpg");

        // Apply a color filter to an image
        int[][] coloredKitten = colorFilter(kittenImageData, -75, 30, -30);
        twoDToImage(coloredKitten, "./images/colored-kitten.jpg");

        // Apply all the filters on the same time
        int[][] allFilters = stretchHorizontally(shrinkVertically(colorFilter(negativeColor(trimBorders(invertImage(appleImageData), 50)), 200, 20, 40)));
        twoDToImage(allFilters, "./images/all-filters.jpg");


        // PAINTING WITH PIXELS

        // Paint a random image
        int[][] canvas1 = new int[500][500];
        int[][] randomPixels = paintRandomImage(canvas1);
        twoDToImage(randomPixels, "./images/random-pixels.jpg");

        // Make a dummy canva of rectangles
        int[][] canvas2 = new int[500][500];
        int[][] rectangles = generateRectangles(canvas2, 6);
        twoDToImage(rectangles, "./images/rectangle.jpg");
    }


    // Image Processing Methods
    public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
        if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
            int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
            for (int i = 0; i < trimmedImg.length; i++) {
                for (int j = 0; j < trimmedImg[i].length; j++) {
                    trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
                }
            }
            return trimmedImg;
        } else {
            System.out.println("Cannot trim that many pixels from the given image.");
            return imageTwoD;
        }
    }


    public static int[][] negativeColor(int[][] imageTwoD) {
        int[][] negativeImage = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                int[] negativeRgba = {255 - rgba[0], 255 - rgba[1], 255 - rgba[2], rgba[3]};
                int color = getColorIntValFromRGBA(negativeRgba);
                negativeImage[i][j] = color;
            }
        }
        return negativeImage;
    }


    public static int[][] stretchHorizontally(int[][] imageTwoD) {
        int[][] stretchedImage = new int[imageTwoD.length][imageTwoD[0].length * 2];
        for (int i = 0; i < imageTwoD.length; i++) {
            int pos = 0;
            for (int j = 0; j < imageTwoD[i].length; j++) {
                stretchedImage[i][pos ++] = imageTwoD[i][j];
                stretchedImage[i][pos ++] = imageTwoD[i][j];
            }
        }
        return stretchedImage;
    }


    public static int[][] shrinkVertically(int[][] imageTwoD) {
        int length = (imageTwoD.length / 2) % 2 == 0 ? imageTwoD.length / 2 : (int) Math.floor(imageTwoD.length / 2);
        int[][] shrinkedImage = new int[length][imageTwoD[0].length];
        int pos = 0;
        for (int i = 0; i < imageTwoD.length; i += 2) {
            for (int j = 0; j < imageTwoD[i].length && pos < length; j++) {
                shrinkedImage[pos][j] = imageTwoD[i][j];
            }
            pos ++;
        }
        return shrinkedImage;
    }


    public static int[][] invertImage(int[][] imageTwoD) {
        int[][] invertedImage = new int[imageTwoD.length][imageTwoD[0].length];
        int posY = imageTwoD.length - 1;
        for (int[] values : imageTwoD) {
            int posX = imageTwoD[0].length - 1;
            for (int value : values) {
                invertedImage[posY][posX--] = value;
            }
            posY--;
        }
        return invertedImage;
    }

    public static int[][] invertImageBis(int[][] imageTwoD) {
        int[][] invertedImage = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[0].length; j++) {
                invertedImage[i][j] = imageTwoD[(imageTwoD.length-1)-i][(imageTwoD[i].length-1)-j];
            }
        }
        return invertedImage;
    }


    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
        int[][] coloredImage = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                int red = rgba[0] + redChangeValue > 255 ? 255 : (Math.max(rgba[0] + redChangeValue, 0));
                int green = rgba[0] + greenChangeValue > 255 ? 255 : (Math.max(rgba[0] + greenChangeValue, 0));
                int blue = rgba[0] + blueChangeValue > 255 ? 255 : (Math.max(rgba[0] + blueChangeValue, 0));
                int[] newRgba = {red, green, blue, rgba[3]};
                coloredImage[i][j] = getColorIntValFromRGBA(newRgba);
            }
        }
        return coloredImage;
    }


    // Painting Methods
    public static int[][] paintRandomImage(int[][] canvas) {
        Random rand = new Random();
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                int red = rand.nextInt(256);
                int green = rand.nextInt(256);
                int blue = rand.nextInt(256);
                int[] rgba = {red, green, blue, 255};
                canvas[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return canvas;
    }


    public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[0].length; j++) {
                if (i >= rowPosition && i <= rowPosition + height && j >= colPosition && j <= colPosition + width) {
                    canvas[i][j] = color;
                }
            }
        }
        return canvas;
    }


    public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
        Random rand = new Random();
        for (int i = 0; i < numRectangles; i++) {
            int width = rand.nextInt(canvas[0].length);
            int height = rand.nextInt(canvas.length);
            int verticalSpacing = canvas.length - height;
            int horizontalSpacing = canvas[0].length - width;
            int rowPosition = rand.nextInt(verticalSpacing);
            int colPosition = rand.nextInt(horizontalSpacing);
            int randomRed = rand.nextInt(256);
            int randomGreen = rand.nextInt(256);
            int randomBlue = rand.nextInt(256);
            int[] rgbaColor = new int[] {randomRed, randomGreen, randomBlue, 255};
            int color = getColorIntValFromRGBA(rgbaColor);
            canvas = paintRectangle(canvas, width, height, rowPosition, colPosition, color);
        }
        return canvas;
    }


    // Utility Methods
    public static int[][] imgToTwoD(String inputFileOrLink) {
        try {
            BufferedImage image = null;
            if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
                URL imageUrl = new URL(inputFileOrLink);
                image = ImageIO.read(imageUrl);
                if (image == null) {
                    System.out.println("Failed to get image from provided URL.");
                }
            } else {
                image = ImageIO.read(new File(inputFileOrLink));
            }
            int imgRows = image.getHeight();
            int imgCols = image.getWidth();
            int[][] pixelData = new int[imgRows][imgCols];
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    pixelData[i][j] = image.getRGB(j, i);
                }
            }
            return pixelData;
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getLocalizedMessage());
            return null;
        }
    }


    public static void twoDToImage(int[][] imgData, String fileName) {
        try {
            int imgRows = imgData.length;
            int imgCols = imgData[0].length;
            BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }
            File output = new File(fileName);
            ImageIO.write(result, "jpg", output);
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e.getLocalizedMessage());
        }
    }


    public static int[] getRGBAFromPixel(int pixelColorValue) {
        Color pixelColor = new Color(pixelColorValue);
        return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
    }


    public static int getColorIntValFromRGBA(int[] colorData) {
        if (colorData.length == 4) {
            Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
            return color.getRGB();
        } else {
            System.out.println("Incorrect number of elements in RGBA array.");
            return -1;
        }
    }


    public static void viewImageData(int[][] imageTwoD) {
        if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
            int[][] rawPixels = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rawPixels[i][j] = imageTwoD[i][j];
                }
            }
            System.out.println("Raw pixel data from the top left corner.");
            System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
            int[][][] rgbPixels = new int[3][3][4];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
                }
            }
            System.out.println();
            System.out.println("Extracted RGBA pixel data from top the left corner.");
            for (int[][] row : rgbPixels) {
                System.out.print(Arrays.deepToString(row) + System.lineSeparator());
            }
        } else {
            System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
        }
    }


}