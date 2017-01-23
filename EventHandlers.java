import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


/**
 * Created by Mark on 15/01/2017.
 */


public class EventHandlers {

    static Browser_Methods b_methods = new Browser_Methods();
    String route; //Currently null, make default value the homepage (when homepage is created)
    String currentUrl; //The current URL of the page that the user is on

    public String getRoute() //returnsCurrentURL
    {
        return route;
    }

    public String getCurrentUrl() //returnsCurrentURL
    {
        return currentUrl;
    }

    //This will get the previous URL that the user has entered so they can return to previous page, at the moment gets the current URL
    public EventHandler<ActionEvent> previousURL() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                b_methods.currentURL(getCurrentUrl());
            }
        };
    }


    //This loads the URL that the user has entered into the search bar
    public EventHandler<ActionEvent> followUrlAction(final TextField textField,
                                                  final ProgressBar progressBar,
                                                  final WebEngine webEngine,
                                                  final WebView webView,
                                                  final Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                route = textField.getText();
                b_methods.currentURL(route);
                System.out.println("Loading route: " + route); //Outputs in terminal
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
                        }
                        else
                        {
                            //System.out.println(newState);
                        }
                    }
                });

                webEngine.load(route); //Loads desired URL on to the page
            }
        };
    }


}
