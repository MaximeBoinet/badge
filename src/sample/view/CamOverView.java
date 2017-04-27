package sample.view;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bytedeco.javacv.FrameGrabber;
import sample.Main;
import sample.model.ImageGetter;

/**
 * Created by MADMAX on 22/04/2017.
 */
public class CamOverView {

    @FXML
    private ImageView ec;

    private Image photoTaken;
    private Thread th;
    private ImageGetter gs;
    private Stage dialogStage;
    private FrameGrabber fg;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public Image getPhotoTaken() {
        return photoTaken;
    }
    public void setPhotoTaken(Image photoTaken) {
        this.photoTaken = photoTaken;
    }
    public ImageView getEc() {
        return ec;
    }
    public void setFg(FrameGrabber fg) {
        this.fg = fg;
    }

    public void initialize() {
        if (fg == null) {
            fg = Main.grabber;
        }
        gs = new ImageGetter(this, fg);
        th = new Thread(gs);
        th.start();
        photoTaken = null;
    }

    public void close() {
        gs.setNotTaken(false);

        try {
            th.interrupt();
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        th = null;
    }

    @FXML
    public void handlePictureTaken() {
        setPhotoTaken(ec.getImage());
        close();
        dialogStage.close();
    }
}
