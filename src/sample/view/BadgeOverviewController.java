package sample.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.bytedeco.javacv.FrameGrabber;
import sample.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by MADMAX on 20/04/2017.
 */
public class BadgeOverviewController {
    @FXML
    private ImageView port,logo,fond;
    @FXML
    private Pane badge,panIMG,panFond,panLogo;
    @FXML
    private TextField nomprenomTF,titreTF;
    @FXML
    private ChoiceBox<String> title;
    @FXML
    private Slider horNomS,verNomS,tailleNomS,horTitleS,verTitleS,tailleTitleS,horPicS,verPicS,taillePicS,horLogS,verLogS,tailleLogS;
    @FXML
    private Label horNomL,verNomL,tailleNomL,horTitleL,verTitleL,tailleTitleL,horPicL,verPicL,titreL,nomprenomL,taillePicL,horLogL,verLogL,tailleLogL;
    @FXML
    private CheckBox nomprenomCB,titleCB,backCB;
    @FXML
    private Region backgroundLess;

    private Main main;
    private int fontPoliceTitle,fontPolicedtnomPrenom,basefontPoliceTitle,basefontPolicenomPrenom;
    private FontWeight fontWeightTitle,fontWeightnomPrenom,basefontWeightTitle,basefontWeightnomPrenom;
    private Image baseBackground,baseLogo,lastBackground;
    private double ratioPort,ratioLog;

    public void initialize() {
        main = Main.lemain;
        initializeValue();
        initializeSlider();
        initializeChoiceBox();
    }

    private void initializeValue() {
        fontPolicedtnomPrenom = 18;
        fontPoliceTitle = 16;
        basefontPolicenomPrenom = 18;
        basefontPoliceTitle = 16;
        baseBackground = fond.getImage();
        lastBackground = fond.getImage();
        baseLogo = logo.getImage();
        fontWeightnomPrenom = FontWeight.BOLD;
        fontWeightTitle = FontWeight.NORMAL;
        basefontWeightnomPrenom = FontWeight.BOLD;
        basefontWeightTitle = FontWeight.NORMAL;
        handleNewRatioPort();
        handleNewRatioLogo();
        port.imageProperty().addListener(((observable, oldValue, newValue) -> handleNewRatioPort()));
        logo.imageProperty().addListener(((observable, oldValue, newValue) -> handleNewRatioLogo()));
    }

    private void initializeSlider() {
        horNomL.setText("X: "+String.valueOf((int)horNomS.getValue()));
        verNomL.setText("Y: "+String.valueOf((int)verNomS.getValue()));
        tailleNomL.setText("Police: "+ String.valueOf((int)tailleNomS.getValue()));
        horTitleL.setText("X: "+String.valueOf((int)horTitleS.getValue()));
        verTitleL.setText("Y: "+String.valueOf((int)verTitleS.getValue()));
        tailleTitleL.setText("Police: "+ String.valueOf((int)tailleTitleS.getValue()));
        horPicL.setText("X: "+String.valueOf((int)horPicS.getValue()));
        verPicL.setText("Y: "+String.valueOf((int)verPicS.getValue()));
        taillePicL.setText("Taille: "+String.valueOf((int)taillePicS.getValue()));
        horLogL.setText("X: "+String.valueOf((int)horLogS.getValue()));
        verLogL.setText("Y: "+String.valueOf((int)verLogS.getValue()));
        tailleLogL.setText("Taille: "+String.valueOf((int)tailleLogS.getValue()));
        horNomS.valueProperty().addListener(((observable, oldValue, newValue) -> handleSliderHorNom(oldValue, newValue)));
        verNomS.valueProperty().addListener(((observable, oldValue, newValue) -> handleSliderVerNom(oldValue, newValue)));
        tailleNomS.valueProperty().addListener((observable,oldValue, newValue) -> handleTaileName(newValue));
        horTitleS.valueProperty().addListener(((observable, oldValue, newValue) -> handleSliderHorTitle(oldValue, newValue)));
        verTitleS.valueProperty().addListener(((observable, oldValue, newValue) -> handleSliderVerTitle(oldValue, newValue)));
        tailleTitleS.valueProperty().addListener((observable,oldValue, newValue) -> handleTaileTitle(newValue));
        horPicS.valueProperty().addListener(((observable, oldValue, newValue) -> handleSliderHorPic(oldValue, newValue)));
        verPicS.valueProperty().addListener(((observable, oldValue, newValue) -> handleSliderVerPic(oldValue, newValue)));
        taillePicS.valueProperty().addListener(((observable, oldValue, newValue) -> handleTaillePort(oldValue, newValue)));
        horLogS.valueProperty().addListener(((observable, oldValue, newValue) -> handleSliderHorLog(oldValue, newValue)));
        verLogS.valueProperty().addListener(((observable, oldValue, newValue) -> handleSliderVerLog(oldValue, newValue)));
        tailleLogS.valueProperty().addListener(((observable, oldValue, newValue) -> handleTailleLog(oldValue, newValue)));

    }

    private void handleSliderHorNom(Number oldVal, Number newVal) {
        horNomL.setText("X: "+String.valueOf(newVal.intValue()));
        nomprenomL.setLayoutX((nomprenomL.getLayoutX() + (newVal.doubleValue() -oldVal.doubleValue())));
    }

    private void handleSliderVerNom(Number oldVal, Number newVal) {
        verNomL.setText("Y: "+String.valueOf(newVal.intValue()));
        nomprenomL.setLayoutY((nomprenomL.getLayoutY() - (newVal.doubleValue() -oldVal.doubleValue())));
    }

    private void handleTaileName(Number newValue) {
        fontPolicedtnomPrenom = newValue.intValue();
        tailleNomL.setText("Police: "+String.valueOf(fontPolicedtnomPrenom));
        nomprenomL.setFont(Font.font("Calibri", fontWeightnomPrenom, fontPolicedtnomPrenom ));
    }

    private void handleSliderHorTitle(Number oldVal, Number newVal) {
        horTitleL.setText("X: "+String.valueOf(newVal.intValue()));
        titreL.setLayoutX((titreL.getLayoutX() + (newVal.doubleValue() -oldVal.doubleValue())));
    }

    private void handleSliderVerTitle(Number oldVal, Number newVal) {
        verTitleL.setText("Y: "+String.valueOf(newVal.intValue()));
        titreL.setLayoutY((titreL.getLayoutY() - (newVal.doubleValue() -oldVal.doubleValue())));
    }

    private void handleTaileTitle(Number newValue) {
        fontPoliceTitle = newValue.intValue();
        tailleTitleL.setText("Police: "+String.valueOf(fontPoliceTitle));
        titreL.setFont(Font.font("Calibri", fontWeightTitle, fontPoliceTitle));
    }

    private void handleSliderHorPic(Number oldVal, Number newVal) {
        horPicL.setText("X: "+String.valueOf(newVal.intValue()));
        port.setLayoutX((port.getLayoutX() + (newVal.doubleValue() -oldVal.doubleValue())));
    }

    private void handleSliderVerPic(Number oldVal, Number newVal) {
        verPicL.setText("Y: "+String.valueOf(newVal.intValue()));
        port.setLayoutY((port.getLayoutY() - (newVal.doubleValue() -oldVal.doubleValue())));
    }

    private void handleTaillePort(Number oldValue, Number newValue) {
        taillePicL.setText("Taile: "+String.valueOf(newValue.intValue()));
        port.setFitHeight(port.getFitHeight() + ((newValue.doubleValue() - oldValue.doubleValue()) * ratioPort));
        port.setFitWidth(port.getFitWidth() + ((newValue.doubleValue() - oldValue.doubleValue())));
    }

    private void handleSliderHorLog(Number oldVal, Number newVal) {
        horLogL.setText("X: "+String.valueOf(newVal.intValue()));
        logo.setLayoutX((logo.getLayoutX() + (newVal.doubleValue() -oldVal.doubleValue())));
    }

    private void handleSliderVerLog(Number oldVal, Number newVal) {
        verLogL.setText("Y: "+String.valueOf(newVal.intValue()));
        logo.setLayoutY((logo.getLayoutY() - (newVal.doubleValue() -oldVal.doubleValue())));
    }

    private void handleTailleLog(Number oldValue, Number newValue) {
        tailleLogL.setText("Taile: "+String.valueOf(newValue.intValue()));
        logo.setFitHeight(logo.getFitHeight() + ((newValue.doubleValue() - oldValue.doubleValue()) * ratioLog));
        logo.setFitWidth(logo.getFitWidth() + ((newValue.doubleValue() - oldValue.doubleValue())));
    }

    private void initializeChoiceBox() {
        ArrayList arr = new ArrayList<String>();
        arr.add("Aide de vie");
        arr.add("Auxiliaire de vie");
        arr.add("Aide Ménagère");
        title.setItems(FXCollections.observableList(arr));
        title.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> handleChoiceBox(newValue));
        title.setValue("Aide de vie");
        handleChoiceBox(0);
    }

    @FXML
    private void handleChoosePicPort() {
        choosePic(port);
    }

    @FXML
    private void handleChoosePicLogo() {
        choosePic(logo);
    }

    @FXML
    private void handleChoosePicFond() {
        choosePic(fond);
    }

    private void choosePic(ImageView iv) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        FileChooser.ExtensionFilter img = new FileChooser.ExtensionFilter("Image", "*.JPG","*.PNG","*.GIF");

        fileChooser.getExtensionFilters().addAll(img);
        File file = fileChooser.showOpenDialog(Main.lestage);
        if (file != null) {
            iv.setImage(new Image(file.toURI().toString()));
        }
        if (iv == fond) {
            lastBackground = iv.getImage();
        }
    }

    @FXML
    private void handleNomPrenom() {
        nomprenomL.setText(nomprenomTF.getText());
    }

    @FXML
    private void handleTitreTextF() {
        titreL.setText(titreTF.getText());
    }

    @FXML
    private void handleChoiceBox(Number newValue) {
        titreL.setText(title.getItems().get(Integer.parseInt(newValue.toString())));
    }

    @FXML
    private void handleMouseDragDroppedPhoto(final DragEvent e) {
        dragDropped(e, port);
    }

    @FXML
    private void handleMouseDragOverPhoto(final DragEvent e) {
        dragOver(e, panIMG);
    }

    @FXML
    private void handleDrageExitedPhoto(final DragEvent e) {
        dragExcited(panIMG);
    }

    @FXML
    private void handleMouseDragDroppedLogo(final DragEvent e) {
        dragDropped(e, logo);
    }

    @FXML
    private void handleMouseDragOverLogo(final DragEvent e) {
        dragOver(e,panLogo);
    }

    @FXML
    private void handleDrageExitedLogo(final DragEvent e) {
        dragExcited(panLogo);
    }

    @FXML
    private void handleMouseDragDroppedFond(final DragEvent e) {
        dragDropped(e,fond);
    }

    @FXML
    private void handleMouseDragOverFond(final DragEvent e) {
        dragOver(e, panFond);
    }

    @FXML
    private void handleDrageExitedFond(final DragEvent e) {
        dragExcited(panFond);
    }

    private void dragDropped (final DragEvent e, ImageView iv) {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            final File file = db.getFiles().get(0);
            Platform.runLater(() -> {
                System.out.println(file.getAbsolutePath());
                try {
                    Image img = new Image(new FileInputStream(file.getAbsolutePath()));

                    iv.setImage(img);

                    if (iv == fond) {
                        lastBackground = iv.getImage();
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            });
        }
        e.setDropCompleted(success);
        e.consume();
    }

    private void dragOver (final DragEvent e, Pane pan) {
        final Dragboard db = e.getDragboard();

        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".gif")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");

        if (db.hasFiles()) {
            if (isAccepted) {
                pan.setStyle("-fx-border-color: green;"
                        + "-fx-border-width: 5;"
                        + "-fx-background-color: #C6C6C6;"
                        + "-fx-border-style: solid;");
                e.acceptTransferModes(TransferMode.COPY);
            } else {
                pan.setStyle("-fx-border-color: red;"
                        + "-fx-border-width: 5;"
                        + "-fx-background-color: #C6C6C6;"
                        + "-fx-border-style: solid;");
            }
        } else {
            e.consume();
        }
    }

    private void dragExcited (Pane pan) {
        pan.setStyle("-fx-border-color: black;"
                + "-fx-background-color: #c7c7c7;");
    }

    @FXML
    private void handleSave() {
        String now = Date.from(Instant.now()).toString();
        now = (now.replace(":", "")).replace(" ", "");
        File file = new File("badge");
        if (!file.exists()) {
            file.mkdir();
        }

        try {
            WritableImage img2 = badge.snapshot(new SnapshotParameters(), null);
            ImageIO.write(SwingFXUtils.fromFXImage(img2, null), "png", new File("badge/"+now+".png"));
            Desktop.getDesktop().open(new File("badge/"+now+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCam() {
        Image img = main.showCamCap();
        if (img != null) {
            String now = Date.from(Instant.now()).toString();
            now = (now.replace(":", "")).replace(" ", "");
            File file = new File("photo");
            if (!file.exists()) {
                file.mkdir();
            }

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", new File("photo/" + now + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            port.setImage(img);
        }
    }

    @FXML
    private void handleBoldName() {
        if (nomprenomCB.isSelected()) {
            fontWeightnomPrenom = FontWeight.BOLD;

        } else {
            fontWeightnomPrenom = FontWeight.NORMAL;
        }
        nomprenomL.setFont(Font.font("Calibri", fontWeightnomPrenom, fontPolicedtnomPrenom ));
    }

    @FXML
    private void handleBoldTitre() {
        if (titleCB.isSelected()) {
            fontWeightTitle = FontWeight.BOLD;
        } else {
            fontWeightTitle = FontWeight.NORMAL;
        }
        titreL.setFont(Font.font("Calibri", fontWeightTitle, fontPoliceTitle));
    }

    @FXML
    private void handleResetNom() {
        nomprenomCB.setSelected(true);
        tailleNomS.setValue(18);
        nomprenomL.setFont(Font.font("Calibri", basefontWeightnomPrenom, basefontPolicenomPrenom ));
        horNomS.setValue(20);
        verNomS.setValue(20);
    }

    @FXML
    private void handleResetTitle() {
        titleCB.setSelected(false);
        tailleTitleS.setValue(16);
        titreL.setFont(Font.font("Calibri", basefontWeightTitle, basefontPoliceTitle ));
        horTitleS.setValue(20);
        verTitleS.setValue(20);
    }

    @FXML
    private void handleResetPort () {
        horPicS.setValue(80);
        verPicS.setValue(80);
        taillePicS.setValue(60);
        port.setImage(null);
    }

    @FXML
    private void handleResetLogo () {
        horLogS.setValue(30);
        verLogS.setValue(80);
        tailleLogS.setValue(30);
        logo.setImage(baseLogo);
    }

    @FXML
    private void handleResetBack () {
        fond.setImage(baseBackground);
        lastBackground = baseBackground;
        backCB.setSelected(false);
        handleBackBord();
    }

    @FXML
    private void handleBackBord () {
        if (!backCB.isSelected()) {
            backgroundLess.setStyle("-fx-border-color: black;");
            fond.setImage(null);
        } else {
            backgroundLess.setStyle("-fx-border-color: none;");
            fond.setImage(lastBackground);
        }
    }
    private void handleNewRatioPort() {
        ratioPort = port.getFitHeight() / port.getFitWidth();
    }

    private void handleNewRatioLogo() {
        ratioLog = logo.getFitHeight() / logo.getFitWidth();
    }
}
