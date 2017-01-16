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



public class EventHandlers {

    static Browser_Methods b_methods = new Browser_Methods();

    public EventHandler<ActionEvent> buttonAction(final TextField textField,
                                                  final ProgressBar progressBar,
                                                  final WebEngine webEngine,
                                                  final WebView webView,
                                                  final Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String route = textField.getText();
                System.out.println("Loading route: " + route); //Outputs in terminal
                progressBar.progressProperty().bind(webEngine.getLoadWorker().progressProperty()); //Progress bar

                //Below outputs if the URL loads into the terminal
                webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> value,
                                        Worker.State oldState, Worker.State newState) {
                        if(newState == Worker.State.SUCCEEDED){
                            System.out.println("Location loaded + " + webEngine.getLocation());
                            String title = webEngine.getTitle();
                            b_methods.defineTitle(title, stage);
                            b_methods.resetProgressBar(0, progressBar);

                        }
                        else if(newState == Worker.State.FAILED)
                        {
                            System.out.println("There is a problem here son");
                        }
                    }
                });


                webEngine.load(route); //Loads desired URL on to the page
            }
        };
    }


}
