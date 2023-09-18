/**
 * @author bassoc wehman
 * @version 1.0
 * @created 05-Oct-2022 12:59:52 PM
 */
import com.sothawo.mapjfx.Projection;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

/**
 * Starts the GUI 2
 */
public class Main extends Application {
    /**
     * Width for JavaFX Scene
     */
    public static final int SCENE_WIDTH = 600;
    /**
     * Height for JavaFX Scene
     */
    public static final int SCENE_HEIGHT = 395;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        URL fxmlLocation = getClass().getResource("MainDisplay.fxml");
        FXMLLoader primaryLoader = new FXMLLoader(fxmlLocation);
        //FXMLLoader primaryLoader = new FXMLLoader(getClass().getResource("../src/MainDisplay.fxml"));
        Pane rootPane = (Pane) primaryLoader.load();
        stage.setTitle("GTFS APP");
        Scene mainScene = new Scene(new Group(rootPane));
        stage.setScene(mainScene);
        stage.setMinWidth(SCENE_WIDTH + 5);
        stage.setMinHeight(SCENE_HEIGHT + 32.8);
        mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.show();
        letterbox(mainScene, rootPane);
        final Projection projection = getParameters().getUnnamed().contains("wgs84")
                ? Projection.WGS_84 : Projection.WEB_MERCATOR;
        final Controller controller = primaryLoader.getController();
        controller.initMapAndControls(projection);


        FXMLLoader tripLoader = new FXMLLoader();

        Parent tripRoot = tripLoader.load(getClass()
                .getResource("tripDisplay.fxml").openStream());

        //Create trip stage (Instantiation)
        Stage tripStage = new Stage();

        //Trip Stage/Window
        tripStage.setTitle("Trip info");
        tripStage.setScene(new Scene(tripRoot));
        tripStage.hide();


        FXMLLoader stopLoader = new FXMLLoader();

        Parent stopRoot = stopLoader.load(getClass()
                .getResource("stopDisplay.fxml").openStream());

        //Create Stop stage (Instantiation)
        Stage stopStage = new Stage();

        // Stop Stage/Window
        stopStage.setTitle("Stop info");
        stopStage.setScene(new Scene(stopRoot));
        stopStage.hide();

        Controller primaryController = primaryLoader.getController();

        StopController stopController = stopLoader.getController();

        TripController tripController = tripLoader.getController();

        primaryController.setStage(stage);

        primaryController.setTripStage(tripStage);

        primaryController.setStopStage(stopStage);

        stopController.setController(primaryController);

        primaryController.setStopController(stopController);

        tripController.setController(primaryController);

        primaryController.setTripController(tripController);
    }
    private void letterbox(final Scene scene, final Pane contentPane) {
        final double initWidth  = scene.getWidth();
        final double initHeight = scene.getHeight();
        final double ratio  = initWidth / initHeight;

        SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio, initHeight, initWidth, contentPane);
        scene.widthProperty().addListener(sizeListener);
        scene.heightProperty().addListener(sizeListener);
    }
    static class SceneSizeChangeListener implements ChangeListener<Number> {
        private final Scene scene;
        private final double ratio;
        private final double initHeight;
        private final double initWidth;
        private final Pane contentPane;

        public SceneSizeChangeListener(Scene scene, double ratio, double initHeight, double initWidth, Pane contentPane) {
            this.scene = scene;
            this.ratio = ratio;
            this.initHeight = initHeight;
            this.initWidth = initWidth;
            this.contentPane = contentPane;
        }

        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
            final double newWidth  = scene.getWidth();
            final double newHeight = scene.getHeight();

            double scaleFactor =
                    newWidth / newHeight > ratio
                            ? newHeight / initHeight
                            : newWidth / initWidth;

            if (scaleFactor >= 1) {
                Scale scale = new Scale(scaleFactor, scaleFactor);
                scale.setPivotX(0);
                scale.setPivotY(0);
                scene.getRoot().getTransforms().setAll(scale);

                contentPane.setPrefWidth (newWidth  / scaleFactor);
                contentPane.setPrefHeight(newHeight / scaleFactor);
            } else {
                contentPane.setPrefWidth (Math.max(initWidth,  newWidth));
                contentPane.setPrefHeight(Math.max(initHeight, newHeight));
            }
        }
    }
}
