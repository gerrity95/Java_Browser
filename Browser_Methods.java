import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;


/**
 * Created by Mark on 16/01/2017.
 */
public class Browser_Methods {

    public void defineTitle(String str, Stage stage) //Gets the title of the current web page and makes it the title of the browser
    {
        stage.setTitle(str);
    }

    public void resetProgressBar(int i, ProgressBar pb)
    {
        pb.progressProperty().unbind();
        pb.setProgress(i);
    }

    public void setTheScene(Stage s, Scene scene, double w, double h, boolean r, String title)
    {
        s.setScene(scene);
        s.setWidth(w);
        s.setHeight(h);
        s.show();
        s.setResizable(r);
        s.setTitle(title);
    }

    public void setHelp(Button b, String s)
    {
        b.setTooltip(
                new Tooltip(s)
        );

    }


}
