/**
 * Created by Mark on 15/01/2017.
 */

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class Browser_Main extends Application{

    static EventHandlers handlers;

    public static void main(String[] args){
         launch();
    }


    @Override
    public void start(Stage stage) throws Exception {
        handlers = new EventHandlers();

        VBox vbox = new VBox(10);

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);

        TextField textField = new TextField();
        Button button = new Button("Go");
        ProgressBar progressBar = new ProgressBar(0);
        hbox.getChildren().addAll(textField, button, progressBar);
        vbox.getChildren().add(hbox);


        ScrollPane scrollPane = new ScrollPane();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        scrollPane.setContent(webView);
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setStyle("-fx-background-color: white");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        vbox.getChildren().add(webView);
        button.setOnAction(handlers.buttonAction(textField, progressBar, webEngine, webView, stage));

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Browser");

    }


}
