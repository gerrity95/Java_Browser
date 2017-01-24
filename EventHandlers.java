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

    private static Browser_Methods b_methods = new Browser_Methods();
    private String route = "url"; //Currently url, make default value the homepage (when homepage is created)
    private String currentUrl; //The current URL of the page that the user is on

    private String getRoute() //returnsCurrentURL
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
                String error = "http://localhost/Browser/error_page.php";
                if(!getRoute().equalsIgnoreCase(error))
                {
                    specRoute(textField.getText());
                }
                b_methods.currentURL(getRoute());
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
                        }
                        else if(newState == Worker.State.FAILED || newState == Worker.State.CANCELLED)
                        {
                            System.out.println("There is a problem here son");
                            //loadURL(textField, progressBar, webEngine, webView, stage);
                            specRoute("http://localhost/Browser/error_page.php");
                            handle(event);
                        }
                    }
                });

                System.out.println(route);
               webEngine.load(route); //Loads desired URL on to the page
            }
        };
    }

    private void specRoute(String s)
    {
        route = s;
    }



}
