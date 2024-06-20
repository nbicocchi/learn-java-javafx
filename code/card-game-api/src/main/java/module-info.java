module us.teamronda.briscola {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    // Components and controllers
    opens us.teamronda.briscola.gui.components to javafx.fxml;
    opens us.teamronda.briscola.gui.controllers to javafx.fxml;

    // Application class' package
    opens us.teamronda.briscola to javafx.fxml;

    // Expose the api's packages
    exports us.teamronda.briscola.api.cards;
    exports us.teamronda.briscola.api.deck;
    exports us.teamronda.briscola.api.game;
    exports us.teamronda.briscola.api.player;

    exports us.teamronda.briscola;
    exports us.teamronda.briscola.objects;
    opens us.teamronda.briscola.objects to javafx.fxml;
}