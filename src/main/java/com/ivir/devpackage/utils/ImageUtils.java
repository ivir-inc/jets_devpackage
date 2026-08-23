/*
 * Copyright 2026 IVIR Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivir.devpackage.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Image Utilities
 *
 */
public final class ImageUtils {


    /**
     * Private Constructor to prevent instantiation
     */
    private ImageUtils() {
    }

    /**
     * Calculates new horizontal value of an image when its height is resized
     * @param oldVertical original vertical of image you are resizing
     * @param newVertical new resized vertical of the image
     * @param currentWidth original horizontal of image you are resizing
     * @return
     */
    public static int calculateHorizontalWithVerticalResize(int oldVertical, int newVertical, int currentWidth) {
        double heightRatio = newVertical/oldVertical;
        int newHorizontal = (int)(currentWidth * heightRatio);
        return  newHorizontal;
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }


    /**
     * Resizes Targetted Image
     * @param originalImage
     * @param targetWidth
     * @param targetHeight
     * @return
     * @throws IOException
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }


}
