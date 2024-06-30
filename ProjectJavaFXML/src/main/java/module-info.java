module mic.projectjavafxml {
    requires javafx.controls;
    requires javafx.fxml;

    exports mic.projectjavafxml.sample;
    opens mic.projectjavafxml.sample to javafx.fxml;
}
