import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

class Browser_Methods {

    private EventHandlers eventHandlers = new EventHandlers();
    String error = "http://ec2-54-71-144-122.us-west-2.compute.amazonaws.com/homepage/error";

    private void defineTitle(String str, Stage stage) //Gets the title of the current web page and makes it the title of the browser
    {
        stage.setTitle(str);
    }

    void resetProgressBar(int i, ProgressBar pb) //Sets the progress bar back to nothing when a URL is loaded
    {
        pb.progressProperty().unbind();
        pb.setProgress(i);
    }

    void setTheScene(Stage s, Scene scene, double w, double h, boolean r, String title) //Combines all methods needed to set the scene outputted in the program
    {
        s.setScene(scene);
        s.setWidth(w);
        s.setHeight(h);
        s.show();
        s.setResizable(r);
        s.setTitle(title);

    }

    void setHelp(Button b, String s) //Gives help text to the buttons
    {
        b.setTooltip(
                new Tooltip(s)
        );

    }

    void setHBoxAttributes(HBox hb, double spacing, Pos p, Insets i) //Sets all appropriate attributes to a HBox
    {
        hb.setSpacing(spacing);
        hb.setAlignment(p);
        hb.setPadding(i);
    }

    void goBack(Button b, WebEngine wb) //Returns to the previous page
    {
        b.setOnAction(event -> wb.getHistory().go(-1));
    }

    void buttonStyling(Button b) //Styles the return and go button
    {
        b.setStyle("-fx-background-color: transparent;");

        b.setOnMouseEntered(event -> b.setStyle("-fx-background-color: #CCCCCC"));

        b.setOnMouseExited(event -> b.setStyle("-fx-background-color: transparent"));
    }

    void manageStartUp(String url, WebEngine webEngine, TextField textField, ProgressBar progressBar, Stage stage)
    {
        System.out.println("The URL for the homepage is: " + eventHandlers.getRoute());
        webEngine.load(url);
        setUrlDetails(webEngine, textField, progressBar, stage);

        // TODO create this method to deal with what happens when starting up this browser
    }

    void setUrlDetails(WebEngine webEngine, TextField textField, ProgressBar progressBar, Stage stage)
    {
        // TODO create this method that will be used for getting the details about a URL **See EventHandlers - followUrlAction
        System.out.println("Location loaded + " + webEngine.getLocation());
        String currentUrl = webEngine.getLocation();
        String title = webEngine.getTitle();
        textField.textProperty().bind(webEngine.locationProperty());
        defineTitle(title, stage);
        resetProgressBar(0, progressBar);

    }


    //This is just to manage the bindings of the URL so that it matches the current web page in the webview. Fixes the problem related to the homepage
    void manageUrlBinding(TextField textField, Label label, WebEngine webEngine)
    {
        textField.setOnMouseClicked(event -> {
            System.out.println("Unbind current URL from text box");
            textField.textProperty().unbind();
        });

        textField.textProperty().bind(webEngine.locationProperty());
        label.textProperty().bind(webEngine.locationProperty());

    }

    //This will alert the user if they have not fully filled in the details for the bookmarks button

     void alertBox(String title, String header, String body)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.show();
    }

}
