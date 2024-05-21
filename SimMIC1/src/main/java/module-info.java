module mic.simmic1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens mic.simmic1 to javafx.fxml;
    exports mic.simmic1;
}
