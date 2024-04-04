import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

public class BlueDetection {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture videoCapture = new VideoCapture(0);

        if (!videoCapture.isOpened()) {
            System.err.println("Error: Unable to open webcam.");
            System.exit(1);
        }

        Mat frame = new Mat();
        do {
            videoCapture.read(frame);
            HighGui.imshow("Webcam Feed", frame);


            //Converts frame to blue, green and red color value
            Mat hsvFrame = new Mat();
            Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

            //Colors for Blue
            Scalar lowerBlue = new Scalar(90, 40, 40);
            Scalar upperBlue = new Scalar(150, 255, 255);


            //Create a mask for Red regions
            Mat blueMask = new Mat();
            Core.inRange(hsvFrame, lowerBlue, upperBlue, blueMask);

            // Find contours in the blue mask
            List<MatOfPoint> blueContours = new ArrayList<>();
            Mat blueHierarchy = new Mat();
            Imgproc.findContours(blueMask, blueContours, blueHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Draw bounding boxes around detected Red objects
            for (MatOfPoint blueContour : blueContours) {
                double area = Imgproc.contourArea(blueContour);
                if (area > 2000) {
                    Rect boundingRect = Imgproc.boundingRect(blueContour);
                    Imgproc.rectangle(frame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 255, 255), 2);
                }
            }

            HighGui.namedWindow("Webcam Feed", HighGui.WINDOW_AUTOSIZE);

        } while (HighGui.waitKey(30) != 'q');
        videoCapture.release();
        HighGui.destroyAllWindows();
    }
}
