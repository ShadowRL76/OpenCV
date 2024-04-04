import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Create a VideoCapture object to capture video from the webcam
        VideoCapture videoCapture = new VideoCapture(0); // 0 for the default webcam, change if necessary

        // Check if the VideoCapture object is opened successfully
        if (!videoCapture.isOpened()) {
            System.err.println("Error: Unable to open webcam.");
            System.exit(1);
        }

        // Create a window to display the webcam feed
        HighGui.namedWindow("Webcam Feed");

        while (true) {
            // Capture a frame from the webcam
            Mat frame = new Mat();
            videoCapture.read(frame);

            // Check if the frame is empty
            if (frame.empty()) {
                System.err.println("Error: Unable to capture frame.");
                break;
            }

            // Convert frame to HSV color space
            Mat hsvFrame = new Mat();
            Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

            // Define lower and upper bounds for yellow color in HSV
            Scalar lowerYellow = new Scalar(20, 100, 100);
            Scalar upperYellow = new Scalar(30, 255, 255);

            // Create a mask to segment yellow regions
            Mat yellowMask = new Mat();
            Core.inRange(hsvFrame, lowerYellow, upperYellow, yellowMask);

            // Find contours in the yellow mask
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(yellowMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Draw bounding boxes around detected yellow objects
            for (MatOfPoint contour : contours) {
                Rect boundingRect = Imgproc.boundingRect(contour);
                Imgproc.rectangle(frame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 255, 255), 2);
            }

            // Display the frame with bounding boxes
            HighGui.imshow("Webcam Feed", frame);

            // Check for key press
            if (HighGui.waitKey(10) == 27) { // 27 is the ASCII code for Escape key
                break;
            }
        }

        // Release the VideoCapture object and close the window
        videoCapture.release();
        HighGui.destroyAllWindows();
    }
}
