import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by gerrity95 on 29/04/17.
 */

    public class Launcher extends Application {

    String myUrl = "https://loop.dcu.ie/pluginfile.php/1553616/mod_resource/content/1/CA472_interview_schedule.pdf";
    String file = "/mnt/Data/Documents/exam_papers/project_management/15CA491.pdf";

    public static void main(String[] args) {
            Application.launch();
        }

        public void start(Stage primaryStage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("resources/pdf.fxml"));
            primaryStage.setTitle("PDF test app");
            primaryStage.setScene(new Scene(root, 1280, 576));
            primaryStage.show();
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
