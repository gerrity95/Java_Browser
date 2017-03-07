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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Browser_Main extends Application{

    private static EventHandlers handlers;
    private static Browser_Methods b_methods;
    private String startUpUrl = "http://localhost/Browser/home_page.php"; //This is the URL that will be loaded on start up, eventually change to homepage

    public static void main(String[] args){
         launch();
    }


    @Override
    public void start(Stage stage) throws Exception {
        handlers = new EventHandlers();
        b_methods =new Browser_Methods();

        //Images for return and go arrows
        Image arrowRight = new Image(getClass().getResourceAsStream("resources/arrow_right.png"));
        Image arrowLeft = new Image(getClass().getResourceAsStream("resources/arrow_left.png"));
        Image reload = new Image(getClass().getResourceAsStream("resources/reset-icon.png"));

        VBox vbox1 = new VBox(10);
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();

        b_methods.setHBoxAttributes(hbox1, 10, Pos.CENTER, new Insets(15, 10, 10, 10));
        b_methods.setHBoxAttributes(hbox2, 10, Pos.BOTTOM_LEFT, new Insets(10, 10, 10, 10));

        Button followUrl = new Button();
        Button returnPage = new Button();
        Button savePage = new Button("Save Page");
        Button reloadPage = new Button();
        TextField urlInput = new TextField();
        ProgressBar progressBar = new ProgressBar(0);
        urlInput.setPromptText("Enter URL Here");
        urlInput.setPrefWidth(800);

        returnPage.setGraphic(new ImageView(arrowLeft));
        followUrl.setGraphic(new ImageView(arrowRight));
        reloadPage.setGraphic(new ImageView(reload));
        b_methods.buttonStyling(returnPage);
        b_methods.buttonStyling(followUrl);
        b_methods.buttonStyling(reloadPage);

        b_methods.setHelp(followUrl, "Go to URL");
        b_methods.setHelp(returnPage, "Return to previous page");
        b_methods.setHelp(savePage, "Save the current web page to home");
        b_methods.setHelp(reloadPage, "Reload the current URL");

        hbox1.getChildren().addAll(returnPage, followUrl, urlInput, reloadPage, savePage);
        hbox2.getChildren().addAll(progressBar);
        vbox1.getChildren().add(hbox1);

        ScrollPane scrollPane = new ScrollPane();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        scrollPane.setContent(webView);
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setStyle("-fx-background-color: white");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        vbox1.getChildren().add(webView);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(0)); // space between elements and window border
        root.setCenter(vbox1);
        root.setBottom(hbox2);

        followUrl.setOnAction(handlers.followUrlAction(urlInput, progressBar, webEngine, stage));
        urlInput.setOnAction(handlers.followUrlAction(urlInput, progressBar, webEngine, stage));
        reloadPage.setOnAction(event -> handlers.reloadUrl(urlInput, progressBar, webEngine, stage));


        b_methods.goBack(returnPage, webEngine);

        webEngine.load(startUpUrl);
        Scene scene = new Scene(root);
        b_methods.setTheScene(stage, scene, 1200, 700, true, "Browser");


    }


}
