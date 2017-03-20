import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


/**
 * Created by Mark on 15/01/2017.
 */


class EventHandlers {

    String error = "http://ec2-35-163-140-194.us-west-2.compute.amazonaws.com/homepage/error"; //URL to the error page hosted on a local server

    private static Browser_Methods b_methods = new Browser_Methods();
    private String route = "http://ec2-35-163-140-194.us-west-2.compute.amazonaws.com/homepage/"; //Currently url, make default value the homepage (when homepage is created)
    private String currentUrl; //The current URL of the page that the user is on
    private boolean urlChecker = true; //Needed to check if or not the URL entered in the address bar failed

    public String getRoute() //returnsCurrentURL
    {
        return route;
    }

    String getCurrentUrl() //returnsCurrentURL
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

                webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> value,
                                        Worker.State oldState, Worker.State newState) {
                        if(newState == Worker.State.SUCCEEDED){
                            System.out.println("Location loaded + " + webEngine.getLocation());
                            currentUrl = webEngine.getLocation();
                            String title = webEngine.getTitle();
                            textField.setText(getCurrentUrl());
                            b_methods.defineTitle(title, stage);
                            b_methods.resetProgressBar(0, progressBar);
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
                    }
                });

               webEngine.load(route); //Loads desired URL on to the page

            }
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



}
