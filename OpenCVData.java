import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

public class OpenCVData {

    public static void getYellow(BufferedImage frame) {

        byte[] pixels = ((DataBufferByte) frame.getRaster().getDataBuffer()).getData();

        Mat rgbFrame = new Mat(frame.getHeight(), frame.getWidth(), CvType.CV_8UC3);

        rgbFrame.put(0, 0, pixels);

        Mat bgrFrame = new Mat();
        Imgproc.cvtColor(rgbFrame, bgrFrame, Imgproc.COLOR_RGB2BGR);

        //Converts frame to blue, green and red color value
        Mat hsvFrame = new Mat();
        Imgproc.cvtColor(bgrFrame, hsvFrame, Imgproc.COLOR_BGR2HSV);

        //Colors for Yellow

        Scalar lowerYellow = new Scalar(20, 100, 100);
        Scalar upperYellow = new Scalar(30, 255, 255);

        //Colors for Red
        Scalar lowerRed = new Scalar(160, 100, 100);
        Scalar upperRed = new Scalar(180, 255, 255);

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
                Imgproc.rectangle(bgrFrame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 255, 255), 2);
            }
        }

        HighGui.imshow("Webcam Feed", bgrFrame);
        HighGui.namedWindow("Webcam Feed", HighGui.WINDOW_AUTOSIZE);
        HighGui.waitKey(1);

    }

}
