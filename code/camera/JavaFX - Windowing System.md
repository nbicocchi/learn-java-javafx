# JavaFX - Windowing System

In this project the focus is to show how to use multiple windows into JavaFX.

## Topics

* Prompt an **alert**
* Show a **dialog pane**

**Bonus**: Create **multiple scenes** in the same application

## What is a Scene

The JavaFX [`Scene`](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html) class is the container for all UI content in a scene graph. Therefore, to create a window, we need to create a `Scene` into our code.
\
Here's an example of how to load a `Scene` into the `start` method:

```
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

The most important thing is to notice that the `Scene` is assigned to the [`Stage`](https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html). It is a necessary operation so that JavaFX can load the window and its fxml file, which has already been given to the `Scene` during its initialization.
 
There are many actions that could be performed on a `Scene`, so for all other specific needs and information it is warmly recommended to consult the [official documentation](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html).

## Prompt an alert

In JavaFX there are many classes which allow coders to create some recurring window patterns without changing scene everytime. 
\
For example, if we want to prompt an alert, we should use the [`Alert`](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html) class. After creating an `Alert` object, we can decide when and how much time to show it for, without perform any scene change.

We can take as example the project's method `showFailedToTakePictureAlert()` in `AlertWindows` class (inside the `common` package):

```
public static void showFailedToTakePictureAlert() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.getDialogPane().setMinWidth(675);
    alert.getDialogPane().setMaxWidth(675);
    alert.setTitle("Warning!");
    alert.setHeaderText("Unable to take picture");
    alert.setContentText("""
            The application is unable to take the picture or to open the editor.
            Quick fixes:
             ~ Retry to take the photo;
             ~ Check if your webcam works properly;
             ~ Try to restart the application;
             ~ Try to restart the computer.""");
    alert.showAndWait();
}
```

_Note_: `Alert.AlertType` is an enum that contains 5 constants which are used to configure the alert dialog to appear in different ways depending on our needs (`Alert.AlertType.WARNING` in the example above). These constants are described on the [official documentation](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.AlertType.html).

In this case we decided to use the alert to prompt a warning message, but there are other `AlertType` (see the [documentation](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html)).

## Show a dialog pane

Another popular window pattern is [`DialogPane`](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DialogPane.html). It is commonly used to require user to enter some data, for example his name, surname, date of birth, ecc.

Here an example of usage based on the project's method `handleSave()` in `HomeController`:

```
public void handleDialog() {
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
}
```

As `Alert`, `DialogPane` has many features too, so it is recommended to take a look at the [documentation page](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DialogPane.html).

### Modality

The modality class defines three behaviour our dialog must look up to. These are:

* `NONE`: defines a top-level window that is not modal and does not block any other window.
* `WINDOW_MODAL`: defines a modal window that block events from being delivered to its entire owner window hierarchy. 
\
_Note_: A `Stage` with modality set to `WINDOW_MODAL`, but its owner is null, is treated as if its modality is set to `NONE`.
* `APPLICATION_MODAL`: defines a modal window that blocks events from being delivered to any other application window.

As such to use `WINDOW_MODAL` it's important to set the owner of the dialog. It can be done as follows:

```
dialog.initOwner(stackPane.getScene().getWindow());
```

In this case the root of the scene is a stack pane, to which we gave the fx id `stackPane`.

## Bonus: Multiple Scenes

We may need to change scene without opening a new window, with its own fxml file. For example, after taking a picture, we might want to open an editor with precision effects, resize tool and so on. In this case alerts and dialog panes could no longer be useful, so we have two options:

1) Change the current `Scene`, simple, but sometimes heavy.
2) Create a class to manage windows, lightweight and flexible, but a bit more complicated.

### Changing scene

_Note_: this is the most widespread way to change root.

In this case we have to give to the `Stage` a new `Scene`.
\
Now we can consider, for example, that we want to switch to an editor window when the capture button is pressed. As we know that all window's events are managed by the controller, so it is a proper place to create the new `Scene`. To give that to the `Stage`, we have to obtain it from the main frame that wraps all elements which compose the window (in most cases it is an `AnchorPane`).
\
Let's see an example of a basic application that simply open a new window when a button has clicked:

#### Application

```
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

```
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

```
public class SecondarySceneController {
    @FXML
    private Label label;

    public void initialize() {
        label.setText("This is the secondary scene");
    }
}
```

_Note_: if we need to perform some operations with the controller before showing the window, we need to change the code above like this:

```
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

It is not very common, but sometimes happens that to physically change `Scene` doesn't give us possibility to return to the previous page safely. For example, remembering that to load a fxml file means that we will load also its controller, we may suppose that this one cannot be loaded more than one time. So, we have to use a trick to change what is shown on the screen keeping the same `Scene`. To do that we have to load the fxml file of the new root which we want to set.
\
Let's consider to open an editor after the capture button is clicked:

```
public void openEditor() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("editor.fxml"));
    Parent nextPane = loader.load();

    // As well as the previous paragraph, we can load the controller to do some operation with it
    EditorController controller = loader.getController();
    
    // Controller operations...
    
    // We need to take the scene and set as root the fxml just loaded
    Scene scene = (Scene) stackPane.getScene();
    scene.setRoot(nextPane);
}
```

The problem seems to not be fixed because to return to the home page we have to reload the `home.fxml` file, but this way we will initialize again the controller. As we said, we can't do this.
\
A solution could be to create an independent class and use it as root controller. Inside this we can keep all fxml files loaded into a collection, for example a `Deque` used as stack, so that it is simpler to return to the previous page (maybe through a back button) taking advantage of the LIFO logic.

Here an example of a possible implementation:

```
public class RootController {
    private static Deque<Parent> rootStack;
    private static Scene scene;

    public static void initRootController(Scene main) {
        rootStack = new ArrayDeque<>();
        RootController.scene = main;
    }

    public static void goBack() {
        Parent backRoot = rootStack.pop();
        scene.setRoot(backRoot);
    }

    public static void goForward(Parent nextRoot) {
        Parent oldRoot = scene.getRoot();
        rootStack.push(oldRoot);
        scene.setRoot(nextRoot);
    }
}
```

