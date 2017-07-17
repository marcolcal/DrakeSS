package GUI;

import DrakeSS.Client;
import DrakeSS.DatabaseHandler;
import DrakeSS.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OtherScheduleController implements Initializable {

    public Button back;
    public VBox vb;
    public ScrollPane scroll;
    public Label title;
    int i = 0;

    public void initialize(URL url, ResourceBundle rb)
    {
        vb.getChildren().clear();
        for (i = 0; i < DatabaseHandler.users.size(); i++) {
            if (Client.permissionLevel == 0)
            {
                if (DatabaseHandler.users.get(i).getAllowedUsers().containsValue(Client.userAccount)) {
                    User current = DatabaseHandler.users.get(i);
                    Hyperlink h = new Hyperlink(DatabaseHandler.users.get(i).getEmail());
                    h.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            Client.currentPersonal = current.getEmail();
                            try {
                                Stage stage = (Stage) back.getScene().getWindow();
                                stage.hide();
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Meetings.fxml"));
                                Parent root1 = (Parent) fxmlLoader.load();
                                stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.initStyle(StageStyle.DECORATED);
                                stage.setTitle("Personal Schedule - " + Client.currentPersonal);
                                stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
                                Scene scene = new Scene(root1);
                                stage.setScene(scene);
                                scene.getStylesheets().add("css/Style.css");
                                stage.show();
                            } catch (Exception o) {
                                o.printStackTrace();
                            }
                        }
                    });
                    vb.getChildren().add(h);
                }
            }
            else if (Client.permissionLevel == 1)
            {

            }
            else if (Client.permissionLevel > 1)
            {

            }
        }
        if (vb.getChildren().isEmpty())
        {
            vb.getChildren().add(new Label("You are currently not allowed to view anyone's schedule."));

        }
    }

    public void goBack(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) back.getScene().getWindow();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Account.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("User Account - " + Client.userAccount.getEmail());
        stage.getIcons().add(new Image("/GUI/img/drake-dark.png"));
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/Style.css");
        stage.setScene(scene);
        stage.show();
    }
}

