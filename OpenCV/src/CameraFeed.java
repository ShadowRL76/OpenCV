//import com.github.sarxos.webcam.Webcam;
//
//import java.awt.image.BufferedImage;
//
//public class CameraFeed extends Thread {
//
//    private boolean running;
//
//    @Override
//    public void run() {
//
//        running = true;
//
//        while (running) {
//
//            BufferedImage frame = Camera.getCam().getImage(); // Gets image
//
//            if (frame != null) {
//                OpenCVData.getYellow(frame);
//            }
//
//        }
//
//    }
//
//    public void stopThread() {
//        running = false;
//    }
//
//}
