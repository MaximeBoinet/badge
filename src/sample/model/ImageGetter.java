package sample.model;

import javafx.embed.swing.SwingFXUtils;
import org.bytedeco.javacpp.RealSense;
import org.bytedeco.javacv.*;
import sample.view.CamOverView;

import java.awt.image.BufferedImage;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvFlip;


/**
 * Created by gtiwari on 1/3/2017.
 */

public class ImageGetter implements Runnable {

    public void setNotTaken(boolean notTaken) {
        this.notTaken = notTaken;
    }
    public ImageGetter(CamOverView cont, FrameGrabber grabber) {
        this.cont = cont;
        this.grabber = grabber;
    }

    final int INTERVAL = 33;
    CamOverView cont;
    public boolean notTaken = true;
    FrameGrabber grabber;

    public void run() {
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        IplImage img;
        try {

            while (notTaken) {
                Frame frame = grabber.grab();
                img = converter.convert(frame);
                cvFlip(img, img, 1);
                cont.getEc().setImage(SwingFXUtils.toFXImage(IplImageToBufferedImage(img), null));
                try {
                    Thread.sleep(INTERVAL);
                } catch (InterruptedException e) {}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage IplImageToBufferedImage(IplImage src) {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter paintConverter = new Java2DFrameConverter();
        Frame frame = grabberConverter.convert(src);
        return paintConverter.getBufferedImage(frame,1);
    }
}