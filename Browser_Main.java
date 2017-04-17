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
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Browser_Main extends Application{

    private static EventHandlers handlers;
    private static Browser_Methods b_methods;
    private String startUpUrl = "http://ec2-54-71-144-122.us-west-2.compute.amazonaws.com/homepage/"; //This is the URL that will be loaded on start up, eventually change to homepage
    //private String startUpUrl = "http://localhost/browser/home.php"; //This is the URL that will be loaded on start up, eventually change to homepage

    public static void main(String[] args){
         launch();
    }


    @Override
    public void start(Stage stage) throws Exception {
        handlers = new EventHandlers();
        b_methods =new Browser_Methods();

        Label location = new Label();

        //Images for return and go arrows
        Image arrowRight = new Image(getClass().getResourceAsStream("resources/arrow_right.png"));
        Image arrowLeft = new Image(getClass().getResourceAsStream("resources/arrow_left.png"));
        Image reload = new Image(getClass().getResourceAsStream("resources/reset-icon.png"));

        VBox vbox1 = new VBox(10);
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();

        b_methods.setHBoxAttributes(hbox1, 10, Pos.CENTER, new Insets(15, 10, 10, 10));
        b_methods.setHBoxAttributes(hbox2, 20, Pos.BOTTOM_LEFT, new Insets(10, 10, 10, 10));

        Button followUrl = new Button();
        Button returnPage = new Button();
        Button savePage = new Button("Save Page");
        Button reloadPage = new Button();
        Button homePage = new Button("Home");  //home button
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
        b_methods.buttonStyling(homePage); //style home page button

        b_methods.setHelp(followUrl, "Go to URL");
        b_methods.setHelp(returnPage, "Return to previous page");
        b_methods.setHelp(savePage, "Save the current web page to home");
        b_methods.setHelp(reloadPage, "Reload the current URL");
        b_methods.setHelp(homePage, "Go to homepage"); //add help pop up for home page

        hbox1.getChildren().addAll(homePage, returnPage, followUrl, urlInput, reloadPage, savePage);
        hbox2.getChildren().addAll(progressBar, location);
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
        savePage.setOnAction(handlers.saveUrl(webEngine));
        reloadPage.setOnAction(event -> handlers.reloadUrl(urlInput, progressBar, webEngine, stage));
        homePage.setOnAction(event -> handlers.homePage(progressBar, webEngine)); //Make user go to home page when selected

        b_methods.goBack(returnPage, webEngine);
        b_methods.manageUrlBinding(urlInput, location, webEngine);


        setStartUpUrl(handlers.getRoute());
        b_methods.manageStartUp(startUpUrl, webEngine, urlInput, progressBar, stage);
        Scene scene = new Scene(root);
        b_methods.setTheScene(stage, scene, 1200, 700, true, "Browser");

        //Disable context menu and create our own
        webView.setContextMenuEnabled(false);
        createContextMenu(webView, webEngine);
    }

    /*
    * Function to create the little popup menu when user right clicks
    * Gives user a list to either reload the current page or go back
    * to the previous web page
    */
    private void createContextMenu(WebView webView, WebEngine webEngine) {
        ContextMenu cm = new ContextMenu();
        //create reload menu item
        MenuItem reload = new MenuItem("Reload");
        reload.setOnAction(e -> webView.getEngine().reload());
        //create back menu item
        MenuItem back = new MenuItem("Back");
        back.setOnAction(event -> webEngine.getHistory().go(-1));
        //put all the menu items together
        cm.getItems().addAll(reload, back);

        //Show the little popup when user right clicks, else hide it
        webView.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                cm.show(webView, event.getScreenX(), event.getScreenY());
            }
            else {
                cm.hide();
            }
        });
    }
    private void setStartUpUrl(String url)
    {
        startUpUrl = url;
    }

}
