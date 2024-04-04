import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Load the input image
        Mat inputImage = Imgcodecs.imread("src/multicolor_objects.jpg");

        if (inputImage.empty()) {
            System.err.println("Error: Unable to load image.");
            System.exit(1);
        }

        // Define the lower and upper bounds for each color
        Scalar lowerRedBound = new Scalar(0, 0, 60);
        Scalar upperRedBound = new Scalar(100, 100, 255);

        Scalar lowerBlueBound = new Scalar(90, 0, 0);
        Scalar upperBlueBound = new Scalar(255, 40, 40); // Adjusted bounds for blue color

        Scalar lowerOrangeBound = new Scalar(0, 50, 100);
        Scalar upperOrangeBound = new Scalar(50, 150, 255);

        // Create masks to segment regions of each color
        Mat redMask = new Mat();
        Mat blueMask = new Mat();
        Mat orangeMask = new Mat();

        Core.inRange(inputImage, lowerRedBound, upperRedBound, redMask);
        Core.inRange(inputImage, lowerBlueBound, upperBlueBound, blueMask);
        Core.inRange(inputImage, lowerOrangeBound, upperOrangeBound, orangeMask);

        // Find contours in the segmented images
        List<MatOfPoint> redContours = new ArrayList<>();
        List<MatOfPoint> blueContours = new ArrayList<>();
        List<MatOfPoint> orangeContours = new ArrayList<>();

        Imgproc.findContours(redMask, redContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(blueMask, blueContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(orangeMask, orangeContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Get the bounding rectangles for each contour
        List<Rect> redBoundingBoxes = new ArrayList<>();
        List<Rect> blueBoundingBoxes = new ArrayList<>();
        List<Rect> orangeBoundingBoxes = new ArrayList<>();

        for (MatOfPoint contour : redContours) {
            Rect boundingBox = Imgproc.boundingRect(contour);
            redBoundingBoxes.add(boundingBox);
        }

        for (MatOfPoint contour : blueContours) {
            Rect boundingBox = Imgproc.boundingRect(contour);
            blueBoundingBoxes.add(boundingBox);
        }

        for (MatOfPoint contour : orangeContours) {
            Rect boundingBox = Imgproc.boundingRect(contour);
            orangeBoundingBoxes.add(boundingBox);
        }

        // Draw bounding boxes on the original image for each color
        for (Rect box : redBoundingBoxes) {
            Imgproc.rectangle(inputImage, new Point(box.x, box.y), new Point(box.x + box.width, box.y + box.height),
                    new Scalar(0, 0, 255), 2); // Red color
        }

        for (Rect box : blueBoundingBoxes) {
            Imgproc.rectangle(inputImage, new Point(box.x, box.y), new Point(box.x + box.width, box.y + box.height),
                    new Scalar(255, 0, 0), 2); // Blue color
        }

        for (Rect box : orangeBoundingBoxes) {
            Imgproc.rectangle(inputImage, new Point(box.x, box.y), new Point(box.x + box.width, box.y + box.height),
                    new Scalar(0, 165, 255), 2); // Orange color
        }

        // Display the image with bounding boxes
        HighGui.imshow("Bounding Boxes", inputImage);
        HighGui.waitKey(0);
    }
}
