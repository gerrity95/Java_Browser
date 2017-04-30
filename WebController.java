import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class WebController implements Initializable {

    String file = "/mnt/Data/Documents/exam_papers/project_management/16CA491.pdf";
    String secondFile = "/mnt/Data/Documents/exam_papers/cloud_computing/16CA485.pdf";

    /*

    TODO Review the toolbar and if it is needed
    TODO Allow it read URLs
    TODO Allow it to read URLs inputted from a text box
    TODO Implement it into the Browser, and remove the separate launcher it is currently running off

     */


    @FXML
    private WebView pdf;

    @FXML
    private Button btn;

    @FXML
    private TextField searchBar;

    @FXML
    private Button search;

    public void initialize(URL location, ResourceBundle resources) {
        WebEngine engine = pdf.getEngine();
        String url = getClass().getResource("/resources/web/viewer.html").toExternalForm();

        // connect CSS styles to customize pdf.js appearance
        engine.setUserStyleSheetLocation(getClass().getResource("/resources/web/viewer.css").toExternalForm());

        engine.setJavaScriptEnabled(true);
        engine.load(url);

        engine.getLoadWorker()
                .stateProperty()
                .addListener((observable, oldValue, newValue) -> {
                    // to debug JS code by showing console.log() calls in IDE console
                    JSObject window = (JSObject) engine.executeScript("window");
                    window.setMember("java", new JSLogListener());
                    engine.executeScript("console.log = function(message){ java.log(message); };");

                    // this pdf file will be opened on application startup
                    if (newValue == Worker.State.SUCCEEDED) {
                        try {
                            // readFileToByteArray() comes from commons-io library
                            byte[] data = FileUtils.readFileToByteArray(new File(file));
                            String base64 = Base64.getEncoder().encodeToString(data);
                            // call JS function from Java code
                            engine.executeScript("openFileFromBase64('" + base64 + "')");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        // this file will be opened on button click
        btn.setOnAction(actionEvent -> {
            try {
                byte[] data = FileUtils.readFileToByteArray(new File(secondFile));
                String base64 = Base64.getEncoder().encodeToString(data);
                engine.executeScript("openFileFromBase64('" + base64 + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}