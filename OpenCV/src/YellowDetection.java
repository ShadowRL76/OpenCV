import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;
import java.util.List;

public class YellowDetection {

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

            //Colors for Yellow

            Scalar lowerYellow = new Scalar(20, 100, 100);
            Scalar upperYellow = new Scalar(30, 255, 255);

            //Colors for Red
            Scalar lowerRed = new Scalar(0, 100, 100);
            Scalar upperRed = new Scalar(10, 255, 255);

            //Colors for Blue
            Scalar lowerBlue = new Scalar(90, 40, 40);
            Scalar upperBlue = new Scalar(150, 255, 255);

            //Create a mask for yellow regions
            Mat yellowMask = new Mat();
            Core.inRange(hsvFrame, lowerYellow, upperYellow, yellowMask);

            // Find contours in the yellow mask
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(yellowMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Draw bounding boxes around detected yellow objects
            for (MatOfPoint contour : contours) {
                double area = Imgproc.contourArea(contour);
                if (area > 2000) {
                    Rect boundingRect = Imgproc.boundingRect(contour);
                    Imgproc.rectangle(frame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 255, 255), 2);
                }
            }


            HighGui.namedWindow("Webcam Feed", HighGui.WINDOW_AUTOSIZE);

        } while (HighGui.waitKey(30) != 'q');
        videoCapture.release();
        HighGui.destroyAllWindows();
    }
}
