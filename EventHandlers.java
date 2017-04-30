import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.sql.Connection;
import java.util.Optional;


/**
 * Created by Mark on 15/01/2017.
 */


class EventHandlers {

    //private String error = "http://ec2-54-71-144-122.us-west-2.compute.amazonaws.com/homepage/error"; //URL to the error page hosted on a local server
    String error = "http://localhost/browser/error_page.php";

    private String home = "https://google.com";
    //private String home = "http://ec2-54-71-144-122.us-west-2.compute.amazonaws.com/homepage/";

    private static Browser_Methods b_methods = new Browser_Methods();
    private static DB_Connection db_connection = new DB_Connection();
    Connection connection;
    //private String route = "http://ec2-54-71-144-122.us-west-2.compute.amazonaws.com/homepage/"; //Currently url, make default value the homepage (when homepage is created)
    private String route = "http://google.com"; //Currently url, make default value the homepage (when homepage is created)

    private String currentUrl; //The current URL of the page that the user is on
    private boolean urlChecker = true; //Needed to check if or not the URL entered in the address bar failed

    String getRoute() //returnsCurrentURL
    {
        return route;
    }

    private String getCurrentUrl() //returnsCurrentURL
    {
        return currentUrl;
    }

    //This loads the URL that the user has entered into the search bar
    EventHandler<ActionEvent> followUrlAction(final TextField textField,
                                                  final ProgressBar progressBar,
                                                  final WebEngine webEngine,
                                                  final Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(urlChecker) {
                    specRoute(textField.getText());
                }

                fixUrl();
                System.out.println("Loading route: " + getRoute()); //Outputs in terminal
                progressBar.progressProperty().bind(webEngine.getLoadWorker().progressProperty()); //Progress bar

                webEngine.getLoadWorker().stateProperty().addListener((value, oldState, newState) -> {
                    if(newState == Worker.State.SUCCEEDED){

                        b_methods.setUrlDetails(webEngine, textField, progressBar, stage);
                        urlChecker = true;
                    }
                    else if(newState == Worker.State.FAILED || newState == Worker.State.CANCELLED)
                    {
                        System.out.println("That URL doesn't seem to exist.");
                        //loadURL(textField, progressBar, webEngine, webView, stage);
                        specRoute(error);
                        handle(event);
                        urlChecker = false;
                    }
                });

               webEngine.load(route); //Loads desired URL on to the page
            }
        };
    }

    EventHandler<ActionEvent> saveUrl(WebEngine webEngine)
    {

        return event -> {

            String saveOrCancel = null;

            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Save current URL");
            dialog.setHeaderText("Save the current URL");

            ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField title = new TextField();
            title.setText(webEngine.getTitle());
            TextField url = new TextField();
            url.setText(webEngine.getLocation());
            TextField category = new TextField();
            category.setPromptText("Category");

            grid.add(new Label("Title:"), 0, 0);
            grid.add(title, 1, 0);
            grid.add(new Label("URL:"), 0, 1);
            grid.add(url, 1, 1);
            grid.add(new Label("Category:"), 0, 2);
            grid.add(category, 1, 2);

            dialog.getDialogPane().setContent(grid);

            // Request focus on the title field by default.
            Platform.runLater(title::requestFocus);


            Optional<Pair<String, String>> result = dialog.showAndWait();

            /*
            Below statement gets the value of which ever button is pressed,
             */

            if (result.isPresent()) //Hacky way of figuring out which button is pressed
            {
                String r = result.toString(); //Variable to get the value of which button is pressed, either save or cancel.
                saveOrCancel = r.substring(21, 27); //Variable of substring
                System.out.println("Button pressed is " + r);
            }

            if( (!title.getText().isEmpty()) && (!url.getText().isEmpty()) && (!category.getText().isEmpty()) )
            {
                System.out.print("Title: " + title.getText());
                System.out.print(", URL: " + url.getText());
                System.out.println(", Category: " + category.getText());

                db_connection.bookmarkUrl(connection, title.getText(), url.getText(), category.getText());


            }
            else
            {
                if (saveOrCancel.equalsIgnoreCase("text=S"))
                {
                    System.out.println("Not all entries filled in.");
                    b_methods.alertBox("", "Warning", "Not all fields have been filled in.");
                    //saveUrl(webEngine);
                }
            }

            System.out.println(saveButton.getButtonData());

        };

    }


    private void specRoute(String s)
    {
        route = s;
    }

    private void fixUrl() //This means the user won't have to type in http:// at start of URL
    {
        String http = "http://";
        String https = "https://";

        String firstCheck = route.substring(0, 7);
        String secondCheck = route.substring(0, 8);

        if (!secondCheck.equalsIgnoreCase(https))
        {

            System.out.println("Doesn't equal https");

            if(!firstCheck.equalsIgnoreCase(http))
            {
                String s = "https://" + route;
                System.out.println("Route is now: " + s);
                route = s;
            }
        }

    }

    //Method for reloading the current URL, will be done using a reload button
    //Is this method really needed? Just changed to use followURL method and is working the same way
    void reloadUrl(final TextField textField,
                   final ProgressBar progressBar,
                   final WebEngine webEngine,
                   final Stage stage)
    {
        String s = getCurrentUrl();
        System.out.println("Reload: " + s);
        specRoute(s);
        //followUrlAction(textField, progressBar, webEngine, stage);
        progressBar.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
        webEngine.load(s);
        b_methods.resetProgressBar(0, progressBar);

    }

    /*
    * Method to return the home page when user clicks Home button
    * Currently just sends user to google.com as a default page for home
    */
    void homePage(final ProgressBar pb, final WebEngine we) {
        System.out.println("Home page: " + home);
        pb.progressProperty().bind(we.getLoadWorker().progressProperty());
        we.load(home); //load the home page
        b_methods.resetProgressBar(0, pb); //reset progress bar at bottom of page
    }


}
