# JavaFX - Windowing System

In this project the focus is to show how to use multiple windows into JavaFX.

## Topics

* Prompt an **alert**
* Show a **dialog pane**

**Bonus**: Create **multiple scenes** in the same application

## What is a Scene

The JavaFX [`Scene`](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html) class is the container for all content in a scene graph. It means that to create a window we need to create a `Scene` into our code.
\
Here an example of how to load a `Scene` into the `start` method:

```java
public class SceneExample extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("scene-example.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Window title");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
```

The most important thing is to notice that the `Scene` is given to the [`Stage`](https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html). It is a necessary operation so that JavaFX can load the window and its fxml file, which has already been assigned to the `Scene` during its initialization.
 
There are so much actions that could be performed on a `Scene`, so for all other specific needs and information it is warmly recommended to consult the [official documentation](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html).

## Prompt an alert

In JavaFX there are many classes which allow coders to create some recurring window patterns without to change scene everytime. 
\
For example, if we want to prompt an alert, we should use the [`Alert`](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html) class. After creating an `Alert` object, we can decide when and how much time to show it without perform any scene change.

Here an example of usage:

```
Alert alert = new Alert(Alert.AlertType.ERROR);
alert.getDialogPane().setMinWidth(400);
alert.getDialogPane().setMaxWidth(300);
alert.setTitle("Fatal Error");
alert.setHeaderText("An error has occurred.");
alert.setContentText("The application ran into a fatal error.\n" +
    "Try to restart it or reboot the computer.");
alert.showAndWait();
```

In this case we decided to use the alert to prompt a fatal error message, but there are other `AlertType` (see the [documentation](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html)).

## Show a dialog pane

Another popular window pattern is [`DialogPane`](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DialogPane.html). It is commonly used to require user to enter some data, for example his nome, surname, date of birth, ecc.

Here an example of usage:

```
try {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("dialog-controller.fxml"));
    DialogPane view = loader.load();

    // Optional: if we need to do stuff with its controller, we need to load that
    DialogController controller = loader.getController();

    // Controller operations ...

    // Create the dialog
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("New DialogPane");
    dialog.initModality(Modality.WINDOW_MODAL);
    dialog.setDialogPane(view);

    // Show the dialog and wait until the user closes it
    Optional<ButtonType> clickedButton = dialog.showAndWait();
    if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
        // Operation to do if the user click the "OK" button
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

As `Alert`, `DialogPane` has so much features too, so it is recommended to take a look at the [corresponding documentation page](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DialogPane.html).

## Bonus: Multiple Scenes

We may need to open a new window with its own fxml file. For example, after taking a picture, we might want to open an editor with precision effects, resize tool and so on. In this case alerts and dialog panes will no longer be useful, but we have two options:

1) Change the current `Scene`, simpler, but sometimes heavy.
2) Create a class to manage windows, lightweight and flexible, but a bit more complicated.

### Changing scene

_Note_: this is the most widespread way to change root.

In this case we have to give to the `Stage` a new `Scene`.
\
Now we can consider, for example, that we want open the editor window when the capture button is pressed. As we know that all window's events are managed by the controller, so it is a proper place where to create the new `Scene`. To give that to the `Stage`, we have to obtain it from the main frame that wraps all elements which compose the window (in most cases it is an `AnchorPane`).
\
Let's see an example of a basic application that simply open a new window when a button has clicked:

#### Application

```java
public class MultiSceneExample extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MultiSceneExample.class.getResource("primary-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Primary Scene");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
```

#### Primary Controller

```java
public class PrimarySceneController {
    @FXML
    private Label welcomeText;
    @FXML
    private VBox vBox;

    public void initialize() {
        welcomeText.setText("Click the button to open a new window");
    }

    @FXML
    private void onOpenWindowClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary-scene.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) vBox.getScene().getWindow();    // In this case we have a VBox as wrapper instead of AnchorPane
        Scene scene = new Scene(root);
        stage.setTitle("Secondary Scene");
        stage.setScene(scene);
    }
}
```

#### Secondary Controller

```java
public class SecondarySceneController {
    @FXML
    private Label label;

    public void initialize() {
        label.setText("This is the secondary scene");
    }
}
```

_Note_: if we need to perform some operations with the controller before to show the window, we need to change the code above like this:

```java
@FXML
private void onOpenWindowClick() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary-scene.fxml"));
    Parent root = loader.load();
    
    // Loading the controller
    SecondarySceneController controller = loader.getController();
    
    // Here we do operations with the controller before showing the scene

    Stage stage = (Stage) vBox.getScene().getWindow();    // In this case we have a VBox as wrapper instead of AnchorPane
    Scene scene = new Scene(root);
    stage.setTitle("Secondary Scene");
    stage.setScene(scene);
}
```

### Using a dedicated class