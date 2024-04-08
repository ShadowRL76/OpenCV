import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;

public class ColorDetection {

    static {
        System.loadLibrary(NATIVE_LIBRARY_NAME);
    }

    private static class ColorRange {
        Scalar lower;
        Scalar upper;

        ColorRange(Scalar lower, Scalar upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    private static final ColorRange BLUE_RANGE = new ColorRange(new Scalar(90, 40, 40), new Scalar(150, 255, 255));
    private static final ColorRange GREEN_RANGE = new ColorRange(new Scalar(35, 40, 40), new Scalar(85, 255, 255));
    private static final ColorRange RED_RANGE = new ColorRange(new Scalar(0, 120, 70), new Scalar(10, 255, 255));

    public static void main(String[] args) {
        displayFrame(BLUE_RANGE);
    }

    private static void displayFrame(ColorRange colorRange) {
        VideoCapture vcap = new VideoCapture(0);

        if (!vcap.isOpened()) {
            System.err.println("Error: Unable to locate webcam.");
            return;
        }

        Mat frame = new Mat();
        Mat hsvFrame = new Mat();

        while (true) {
            vcap.read(frame);

            if (frame.empty()) {
                System.err.println("Error: Blank frame grabbed");
                break;
            }

            Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);
            detectAndDrawContours(frame, hsvFrame, colorRange.lower, colorRange.upper);

            HighGui.imshow("Webcam Feed", frame);
            if (HighGui.waitKey(30) == 'q') {
                break;
            }
        }

        vcap.release();
        HighGui.destroyAllWindows();
    }

    private static void detectAndDrawContours(Mat frame, Mat hsvFrame, Scalar lowerColor, Scalar upperColor) {
        Mat mask = new Mat();
        inRange(hsvFrame, lowerColor, upperColor, mask);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > 5000) {
                // Draw bounding rectangle
                org.opencv.core.Rect boundingRect = Imgproc.boundingRect(contour);
                Imgproc.rectangle(frame, boundingRect, new Scalar(255, 255, 0), 2);

                // Draw centroid
                double cX = boundingRect.x + boundingRect.width * 0.5;
                double cY = boundingRect.y + boundingRect.height * 0.5;
                Imgproc.circle(frame, new org.opencv.core.Point(cX, cY), 5, new Scalar(255, 0, 0), -1);

                // Display coordinates
                String text = "X: " + cX + ", Y: " + cY;
                Imgproc.putText(frame, text, new org.opencv.core.Point((int) cX + 10, (int) cY + 10),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(0, 0, 255), 2);
            }
        }
    }
}
