package at.itarchitects.lightzonefx;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.application.Preloader.ProgressNotification;
import javafx.scene.image.Image;
import javafx.stage.WindowEvent;
import lzfxpreloader.LZFXPreloader;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    private static File temp;
    Parent root;

    private static final String WINDOW_POSITION_X = "Window_Position_X";
    private static final String WINDOW_POSITION_Y = "Window_Position_Y";
    private static final String WINDOW_WIDTH = "Window_Width";
    private static final String WINDOW_HEIGHT = "Window_Height";
    private static final double DEFAULT_X = 100;
    private static final double DEFAULT_Y = 200;
    private static final double DEFAULT_WIDTH = 1280;
    private static final double DEFAULT_HEIGHT = 750;
    private static final String NODE_NAME = "LightZoneFX";
    private static final String BUNDLE = "Bundle";
    private FXMLLoader fxmlLoader;
    private Image iconImage;

    @Override
    public void stop() throws Exception {
        super.stop(); //To change body of generated methods, choose Tools | Templates.        
    }

    @Override
    public void init() throws Exception {
        super.init(); //To change body of generated methods, choose Tools | Templates.        
        notifyPreloader(new ProgressNotification(0.1));
        fxmlLoader = new FXMLLoader(getClass().getResource("/at/itarchitects/lightzonefx/fxml/MainViewBrowser.fxml"));
        notifyPreloader(new ProgressNotification(0.2));
        root = (Parent) fxmlLoader.load();
        notifyPreloader(new ProgressNotification(0.6));
        iconImage = new Image(getClass().getResourceAsStream("/at/itarchitects/lightzonefx/img/LightZone_55.png"));
        notifyPreloader(new ProgressNotification(0.8));
    }

    @Override
    public void start(Stage stage) throws IOException {
        restoreSettings(stage);

        stage.setOnCloseRequest((final WindowEvent event) -> {
            saveSettings(stage);
            MainViewController controller = fxmlLoader.getController();
            controller.Shutdown();
        });

        scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/at/itarchitects/lightzonefx/fxml/MainView.css").toExternalForm());

        stage.setScene(scene);
        stage.getIcons().add(iconImage);
        stage.show();

    }

    private void saveSettings(Stage stage) {
        Preferences preferences = Preferences.userRoot().node(NODE_NAME);
        preferences.putDouble(WINDOW_POSITION_X, stage.getX());
        preferences.putDouble(WINDOW_POSITION_Y, stage.getY());
        preferences.putDouble(WINDOW_WIDTH, stage.getWidth());
        preferences.putDouble(WINDOW_HEIGHT, stage.getHeight());
        try {
            preferences.flush();
        } catch (BackingStoreException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void restoreSettings(Stage stage) {
        // Pull the saved preferences and set the stage size and start location

        Preferences pref = Preferences.userRoot().node(NODE_NAME);
        double x = pref.getDouble(WINDOW_POSITION_X, DEFAULT_X);
        double y = pref.getDouble(WINDOW_POSITION_Y, DEFAULT_Y);
        double width = pref.getDouble(WINDOW_WIDTH, DEFAULT_WIDTH);
        double height = pref.getDouble(WINDOW_HEIGHT, DEFAULT_HEIGHT);
        stage.setX(x);
        stage.setY(y);
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public static void main(String[] args) {

        try {
            String appData = Utility.getAppData();
            Logger logger = Logger.getLogger("at.itarchitects.lightzonefx");            
            File logFile=new File(appData+File.separator+"lightzonefx.log");            
            Handler handler = new FileHandler(logFile.getAbsolutePath(), 50000, 1, true);
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);

            handler.setFormatter(new SimpleFormatter());            
            System.setProperty("javafx.preloader", LZFXPreloader.class.getCanonicalName());
            Application.launch(App.class, args);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}