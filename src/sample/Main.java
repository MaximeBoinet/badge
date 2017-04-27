package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.VideoInputFrameGrabber;
import sample.view.CamOverView;

import java.io.IOException;

public class Main extends Application {
    public static Stage lestage;
    public static Main lemain;
    private Stage primaryStage;
    private BorderPane rootLayout;
    public static FrameGrabber grabber;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BadgeMaker");
        lestage = this.primaryStage;
        lemain = this;
        initRootLayout();
        showBadgeOverview();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showBadgeOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BadgeOverview.fxml"));
            AnchorPane badgeOverview = loader.load();
            rootLayout.setCenter(badgeOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image showCamCap() {
        try {
            grabber = new VideoInputFrameGrabber(0);
            grabber.start();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CamOverView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("PictureTaker");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            CamOverView controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.setOnCloseRequest(((observable) -> controller.close()));
            controller.setFg(grabber);
            dialogStage.showAndWait();
            return controller.getPhotoTaken();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
