package us.teamronda.briscola.gui;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Guis {

    START("start.fxml"),
    TABLE("table.fxml"),
    RANKING("ranking.fxml");
    private final String name;

    public String getPath() {
        return "/us/teamronda/briscola/gui/fxmls/" + name;
    }
}
