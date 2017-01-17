/**
 * Created by Mark on 15/01/2017.
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Browser_Main extends Application{

    static EventHandlers handlers;
    static Browser_Methods b_methods;

    public static void main(String[] args){
         launch();
    }


    @Override
    public void start(Stage stage) throws Exception {
        handlers = new EventHandlers();
        b_methods =new Browser_Methods();

        //Images for return and go arrows
        Image arrowRight = new Image(getClass().getResourceAsStream("resources/green_arrow_right.png"));
        Image arrowLeft = new Image(getClass().getResourceAsStream("resources/green_arrow_left.png"));

        VBox vbox1 = new VBox(10);
        HBox hbox1 = new HBox(10);
        HBox hbox2 = new HBox(10);

        hbox1.setAlignment(Pos.CENTER_LEFT);
        hbox2.setAlignment(Pos.CENTER_LEFT);
        Button followUrl = new Button();
        Button returnPage = new Button();
        Button savePage = new Button("Save Page");

        returnPage.setGraphic(new ImageView(arrowLeft));
        followUrl.setGraphic(new ImageView(arrowRight));

        b_methods.setHelp(followUrl, "Go to URL");
        b_methods.setHelp(returnPage, "Return to previous page");
        b_methods.setHelp(savePage, "Save the current web page to home");


        TextField urlInput = new TextField();
        ProgressBar progressBar = new ProgressBar(0);
        hbox1.setPadding(new Insets(15, 10, 10, 10));
        hbox2.setPadding(new Insets(0, 10, 10, 10));

        hbox1.getChildren().addAll(returnPage, urlInput, followUrl, progressBar);
        hbox2.getChildren().add(savePage);

        vbox1.getChildren().addAll(hbox1, hbox2);

        ScrollPane scrollPane = new ScrollPane();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        scrollPane.setContent(webView);
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setStyle("-fx-background-color: white");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


        vbox1.getChildren().add(webView);


        followUrl.setOnAction(handlers.followUrlAction(urlInput, progressBar, webEngine, webView, stage));
        urlInput.setOnAction(handlers.followUrlAction(urlInput, progressBar, webEngine, webView, stage));


        Scene scene = new Scene(vbox1);

        b_methods.setTheScene(stage, scene, 1200, 750, true, "Browser");

    }


}
