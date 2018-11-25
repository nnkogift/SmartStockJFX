package fahamu.Ui;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.events.JFXDrawerEvent;
import fahamu.dataFactory.LogInStageData;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class LogInUiController extends BaseUIComponents {
    public AnchorPane anchorPane;
    public ImageView logoImage;
    public TextField userTextView;
    public PasswordField userPassword;
    public Button loginButton;
    public Button resetButton;
    public ProgressIndicator progressInd;

    //for test only
    private boolean isFirstTimeCashier = true;
    private boolean isFirstAdmin = true;
    private SalesCategoryUI salesCategoryUICashier;


    @FXML
    public void initialize() {

//        FlowPane content = new FlowPane();
//        JFXButton leftButton = new JFXButton(LEFT);
//        JFXButton topButton = new JFXButton(TOP);
//        JFXButton rightButton = new JFXButton(RIGHT);
//        JFXButton bottomButton = new JFXButton(BOTTOM);
//        content.getChildren().addAll(leftButton, topButton, rightButton, bottomButton);
//        content.setMaxSize(200, 200);
//
//
//        JFXDrawer leftDrawer = new JFXDrawer();
//        StackPane leftDrawerPane = new StackPane();
//        leftDrawerPane.getStyleClass().add("red-400");
//        leftDrawerPane.getChildren().add(new JFXButton("Left Content"));
//        leftDrawer.setSidePane(leftDrawerPane);
//        leftDrawer.setDefaultDrawerSize(150);
//        leftDrawer.setResizeContent(true);
//        leftDrawer.setOverLayVisible(false);
//        leftDrawer.setResizableOnDrag(true);
//
//
//        JFXDrawer bottomDrawer = new JFXDrawer();
//        StackPane bottomDrawerPane = new StackPane();
//        bottomDrawerPane.getStyleClass().add("deep-purple-400");
//        bottomDrawerPane.getChildren().add(new JFXButton("Bottom Content"));
//        bottomDrawer.setDefaultDrawerSize(150);
//        bottomDrawer.setDirection(JFXDrawer.DrawerDirection.BOTTOM);
//        bottomDrawer.setSidePane(bottomDrawerPane);
//        bottomDrawer.setResizeContent(true);
//        bottomDrawer.setOverLayVisible(false);
//        bottomDrawer.setResizableOnDrag(true);
//
//
//        JFXDrawer rightDrawer = new JFXDrawer();
//        StackPane rightDrawerPane = new StackPane();
//        rightDrawerPane.getStyleClass().add("blue-400");
//        rightDrawerPane.getChildren().add(new JFXButton("Right Content"));
//        rightDrawer.setDirection(JFXDrawer.DrawerDirection.RIGHT);
//        rightDrawer.setDefaultDrawerSize(150);
//        rightDrawer.setSidePane(rightDrawerPane);
//        rightDrawer.setOverLayVisible(false);
//        rightDrawer.setResizableOnDrag(true);
//
//
//        JFXDrawer topDrawer = new JFXDrawer();
//        StackPane topDrawerPane = new StackPane();
//        topDrawerPane.getStyleClass().add("green-400");
//        topDrawerPane.getChildren().add(new JFXButton("Top Content"));
//        topDrawer.setDirection(JFXDrawer.DrawerDirection.TOP);
//        topDrawer.setDefaultDrawerSize(150);
//        topDrawer.setSidePane(topDrawerPane);
//
//
//        drawerStack.setContent(content);
//        leftDrawer.setId(LEFT);
//        rightDrawer.setId(RIGHT);
//        bottomDrawer.setId(BOTTOM);
//        topDrawer.setId(TOP);
//
//        leftButton.addEventHandler(MOUSE_PRESSED, e ->drawerStack.toggle(leftDrawer));
//        bottomButton.addEventHandler(MOUSE_PRESSED, e ->drawerStack .toggle(bottomDrawer));
//        rightButton.addEventHandler(MOUSE_PRESSED, e -> drawerStack.toggle(rightDrawer));
//        topButton.addEventHandler(MOUSE_PRESSED, e -> drawerStack.toggle(topDrawer));

        // to be moved to constructor
//        try {
//
////            drawerStack.setContent(FXMLLoader.load(Resources.Companion.getResourceAsUrl("fxmls/logInDrawerContent.fxml")));
////            assert getLOG_IN_BANNER_IMAGE() != null;
////            parentStackPane.setStyle("-fx-background-image: url(" + getBACKGROUND_IMAGE_PATH() + ");");
////            assert getLEFT_DRAWER_LAYOUT_FXML() != null;
////            leftDrawer.setSidePane((VBox) FXMLLoader.load(Resources.Companion.getResourceAsUrl("fxmls/logInLeftDrawer.fxml")));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void startBackGroundLogin(TextField username,
                                      PasswordField password,
                                      Button logInJFXButton,
                                      Button forgetPasswordJFXButton) {
        //disable buttons and show progress indicator
        disableButtons(new Button[]{logInJFXButton, forgetPasswordJFXButton});
        enableProgressIndicator(progressInd);

        Task<String> task = new Task<String>() {
            @Override
            protected String call() {
                //updateProgress(-1F, 1);
                //authenticate user
                String user = authenticateUser(username.getText(), password.getText());
                if (user == null) {
                    cancel();
                    return null;
                } else {
                    return user;
                }
            }
        };

        // change ui according to the user login
        task.setOnSucceeded(event ->
                changeScene(task, (Stage) logInJFXButton.getScene().getWindow(), logInJFXButton, forgetPasswordJFXButton)
        );

        task.setOnFailed(event -> {
            enableButtons(new Button[]{logInJFXButton, forgetPasswordJFXButton});
            disableProgressIndicator(progressInd);

            alertCreator("Error", "Trouble log In",
                    "Process is cancelled\n" +
                            "Check your credential and try again", logInJFXButton.getScene().getRoot(),
                    new ImageView(new Image(getWrongPasswordIcon().toExternalForm())));
            password.clear();

        });

        task.setOnCancelled(event -> {
            enableButtons(new Button[]{logInJFXButton, forgetPasswordJFXButton});
            disableProgressIndicator(progressInd);

            alertCreator("Error", "Trouble log In",
                    "Username is not available or password is incorrect\n" +
                            "Check your credential and try again", logInJFXButton.getScene().getRoot(),
                    new ImageView(new Image(getWrongPasswordIcon().toExternalForm())));
            password.clear();

        });

        //p.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    private String authenticateUser(String username, String password) {
        //check if password is correct
        if (password.equals(LogInStageData.authenticateUser(username))) {
            //check type of u
            String CASHIER = "cashier";
            if (LogInStageData.getUserType(username).equals(getADMIN())) {
                //TODO: call admin scene
                return getADMIN();
            } else return CASHIER;
        } else
            return null;
    }

    private void enableProgressIndicator(ProgressIndicator p) {
        try {
            p.setVisible(true);
        } catch (Throwable ignore) {
        }
    }

    private void disableButtons(Button[] buttons) {
        for (Button bu :
                buttons) {
            bu.setDisable(true);
        }
    }

    private void disableProgressIndicator(ProgressIndicator p) {
        try {
            p.setVisible(false);
        } catch (Throwable ignore) {

        }
    }

    private void enableButtons(Button[] buttons) {
        for (Button bu :
                buttons) {
            bu.setDisable(false);
        }
    }

    private void changeScene(Task<String> task, Stage stage, Button logInJFXButton, Button reset) {
        if (task.getValue().equals(getADMIN())) {
            //for admin user interface
            //TODO: to create admin scene
            //for testing only
            if (isFirstAdmin) {
                //set objects
                salesCategoryUICashier = new SalesCategoryUI(false);
                Platform.runLater(() -> {
                    new MainStage(MainStage.ADMIN_UI, salesCategoryUICashier);
                    MainStage.stageAdmin.show();
                    stage.close();
                });
                isFirstAdmin = false;
            } else {
                MainStage.stageAdmin.show();
            }
            enableButtons(new Button[]{logInJFXButton, reset});
            disableProgressIndicator(progressInd);
            userPassword.clear();
            userTextView.clear();

        } else {
            //TODO: to create a cashier scene
            //set objects
            if (isFirstTimeCashier) {
                salesCategoryUICashier = new SalesCategoryUI(true);
                Platform.runLater(() -> {
                    new MainStage(MainStage.CASHIER_UI, salesCategoryUICashier);
                    MainStage.stageUser.show();
                    stage.close();
                });
                isFirstTimeCashier = false;
            } else {
                MainStage.stageUser.show();
            }
            enableButtons(new Button[]{logInJFXButton, reset});
            disableProgressIndicator(progressInd);
            userPassword.clear();
            userTextView.clear();
//            try {
//                enableButtons(new Button[]{logInJFXButton, reset});
//                disableProgressIndicator(progressInd);
//                // stage.setResizable(true);
//                assert getSALE_UI_LAYOUT_FXML() != null;
//                stage.setScene(new Scene(FXMLLoader.load(getSALE_UI_LAYOUT_FXML())));
//                stage.sizeToScene();
//                stage.centerOnScreen();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void translateBannersRight(JFXDrawerEvent jfxDrawerEvent) {
        JFXDrawersStack parent = (JFXDrawersStack) ((JFXDrawer) jfxDrawerEvent.getSource()).getParent();
        FlowPane content = (FlowPane) parent.getContent();
        content.setTranslateX(150f);
    }

    public void translateBannersCenter(JFXDrawerEvent jfxDrawerEvent) {
        JFXDrawersStack parent = (JFXDrawersStack) ((JFXDrawer) jfxDrawerEvent.getSource()).getParent();
        FlowPane content = (FlowPane) parent.getContent();
        content.setTranslateX(0f);
    }

    public void translateBannersLeft(JFXDrawerEvent jfxDrawerEvent) {
        JFXDrawersStack parent = (JFXDrawersStack) ((JFXDrawer) jfxDrawerEvent.getSource()).getParent();
        FlowPane content = (FlowPane) parent.getContent();
        content.setTranslateX(-150f);
    }

    public void login(ActionEvent actionEvent) {
        startBackGroundLogin(userTextView, userPassword, loginButton, resetButton);
    }

    public void resetPassword(ActionEvent actionEvent) {

    }
}
