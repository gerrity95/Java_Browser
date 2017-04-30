import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Base64;

/**
 * Created by gerrity95 on 29/04/17.
 */

    public class Launcher extends Application {

    String myUrl = "https://loop.dcu.ie/pluginfile.php/1553616/mod_resource/content/1/CA472_interview_schedule.pdf";

    String file = "/mnt/Data/Documents/exam_papers/project_management/16CA491.pdf";
    String secondFile = "/mnt/Data/Documents/exam_papers/cloud_computing/16CA485.pdf";

    Browser_Methods b_methods = new Browser_Methods();

    public static void main(String[] args) {
            Application.launch();
        }


        public void start(Stage stage) throws Exception {

            Browser_Main browser_main = new Browser_Main();

            Label location = new Label();
            Image arrowRight = new Image(getClass().getResourceAsStream("resources/arrow_right.png"));
            Image arrowLeft = new Image(getClass().getResourceAsStream("resources/arrow_left.png"));
            Image reload = new Image(getClass().getResourceAsStream("resources/reset-icon.png"));

            VBox vbox1 = new VBox(10);
            HBox hbox1 = new HBox();
            HBox hbox2 = new HBox();

            b_methods.setHBoxAttributes(hbox1, 10, Pos.CENTER, new Insets(15, 10, 10, 10));
            b_methods.setHBoxAttributes(hbox2, 20, Pos.BOTTOM_LEFT, new Insets(10, 10, 10, 10));

            Button followUrl = new Button();
            Button returnPage = new Button();
            Button savePage = new Button("Save Page");
            Button reloadPage = new Button();
            Button homePage = new Button("Home");  //home button
            TextField urlInput = new TextField();
            ProgressBar progressBar = new ProgressBar(0);
            urlInput.setPromptText("Enter URL Here");
            urlInput.setPrefWidth(800);

            returnPage.setGraphic(new ImageView(arrowLeft));
            followUrl.setGraphic(new ImageView(arrowRight));
            reloadPage.setGraphic(new ImageView(reload));
            b_methods.buttonStyling(returnPage);
            b_methods.buttonStyling(followUrl);
            b_methods.buttonStyling(reloadPage);
            b_methods.buttonStyling(homePage); //style home page button

            b_methods.setHelp(followUrl, "Go to URL");
            b_methods.setHelp(returnPage, "Return to previous page");
            b_methods.setHelp(savePage, "Save the current web page to home");
            b_methods.setHelp(reloadPage, "Reload the current URL");
            b_methods.setHelp(homePage, "Go to homepage"); //add help pop up for home page

            hbox1.getChildren().addAll(homePage, returnPage, followUrl, urlInput, reloadPage, savePage);
            hbox2.getChildren().addAll(progressBar, location);
            vbox1.getChildren().add(hbox1);

            ScrollPane scrollPane = new ScrollPane();
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();

            scrollPane.setContent(webView);
            scrollPane.getStyleClass().add("noborder-scroll-pane");
            scrollPane.setStyle("-fx-background-color: white");
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            vbox1.getChildren().add(webView);

            BorderPane root = new BorderPane();
            root.setPadding(new Insets(0)); // space between elements and window border
            root.setCenter(vbox1);
            root.setBottom(hbox2);

            Scene scene = new Scene(root);
            b_methods.setTheScene(stage, scene, 1200, 700, true, "PDF Viewer");
            loadPdf(webView, webEngine);


            //Disable context menu and create our own
            webView.setContextMenuEnabled(false);
            browser_main.createContextMenu(webView, webEngine);

        }

    void loadPdf(WebView webview, WebEngine engine)
    {
        engine = webview.getEngine();
        String url = getClass().getResource("/resources/web/viewer.html").toExternalForm();

        // connect CSS styles to customize pdf.js appearance
        engine.setUserStyleSheetLocation(getClass().getResource("/resources/web/viewer.css").toExternalForm());

        engine.setJavaScriptEnabled(true);
        engine.load(url);

        WebEngine finalEngine = engine;
        engine.getLoadWorker()
                .stateProperty()
                .addListener((observable, oldValue, newValue) -> {
                    // to debug JS code by showing console.log() calls in IDE console
                    JSObject window = (JSObject) finalEngine.executeScript("window");
                    window.setMember("java", new JSLogListener());
                    finalEngine.executeScript("console.log = function(message){ java.log(message); };");

                    // this pdf file will be opened on application startup
                    if (newValue == Worker.State.SUCCEEDED) {
                        try {
                            // readFileToByteArray() comes from commons-io library
                            byte[] data = FileUtils.readFileToByteArray(new File(file));
                            String base64 = Base64.getEncoder().encodeToString(data);
                            // call JS function from Java code
                            finalEngine.executeScript("openFileFromBase64('" + base64 + "')");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

}


   /*
    public void main(String[] args) {

        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        //Change the path according to yours.
        String url = getClass().getResource("pdfjs/web/viewer.html").toExternalForm();
//We add our stylesheet.
        engine.setUserStyleSheetLocation(getClass().getResource("pdfjs/web.css").toExternalForm());
        engine.setJavaScriptEnabled(true);
        engine.load(url);

        InputStream stream = null;
        try {
            stream = myUrl.openStream();
            //I use IOUtils from org.​apache.​commons.​io
            byte[] data = IOUtils.toByteArray(stream);
            //Base64 from java.util
            String base64 = Base64.getEncoder().encodeToString(data);
            //This must be ran on FXApplicationThread
            webView.getEngine().executeScript("openFileFromBase64('" + base64 + "')");

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }


    }

    */
