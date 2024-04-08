//import org.opencv.core.*;
//import org.opencv.highgui.HighGui;
//import org.opencv.imgproc.Imgproc;
//
//import java.awt.image.BufferedImage;
//import java.awt.image.DataBufferByte;
//import java.util.ArrayList;
//import java.util.List;
//
//public class OpenCVData {
//
//
//    public static void getYellow(BufferedImage frame) {
//
//        byte[] pixels = ((DataBufferByte) frame.getRaster().getDataBuffer()).getData();
//
//        Mat rgbFrame = new Mat(frame.getHeight(), frame.getWidth(), CvType.CV_8UC3);
//
//        rgbFrame.put(0, 0, pixels);
//
//        Mat bgrFrame = new Mat();
//        Imgproc.cvtColor(rgbFrame, bgrFrame, Imgproc.COLOR_RGB2BGR);
//
//        //Converts frame to blue, green and red color value
//        Mat hsvFrame = new Mat();
//        Imgproc.cvtColor(bgrFrame, hsvFrame, Imgproc.COLOR_BGR2HSV);
//
//
//
//        //Create a mask for yellow regions
//        Mat yellowMask = new Mat();
//        Core.inRange(hsvFrame, ColorRange.RED_RANGE.lower, ColorRange.RED_RANGE.upper, yellowMask);
//
//        // Find contours in the yellow mask
//        List<MatOfPoint> contours = new ArrayList<>();
//        Mat hierarchy = new Mat();
//        Imgproc.findContours(yellowMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//
//        // Draw bounding boxes around detected yellow objects
//        // Draw bounding boxes around detected yellow objects
//        for (MatOfPoint contour : contours) {
//            double area = Imgproc.contourArea(contour);
//            if (area > 2000) { // Adjust this threshold as needed
//                // Get bounding rectangle
//                Rect boundingRect = Imgproc.boundingRect(contour);
//
//                Imgproc.rectangle(bgrFrame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 255, 255), 2);
//
//                double cX = boundingRect.x + boundingRect.width / 2.0;
//                double cY = boundingRect.y + boundingRect.height / 2.0;
//
//                Imgproc.circle(bgrFrame, new Point(cX, cY), 5, new Scalar(255, 0, 0), -1);
//
//                String text = "X: " + (int) cX + ", Y: " + (int) cY;
//                Imgproc.putText(bgrFrame, text, new Point((int) cX + 10, (int) cY + 10), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 255), 2);
//            }
//        }
//
//        HighGui.imshow("Webcam Feed", bgrFrame);
//        HighGui.namedWindow("Webcam Feed", HighGui.WINDOW_AUTOSIZE);
//        HighGui.waitKey(1);
//
//
//    }
//}
//
