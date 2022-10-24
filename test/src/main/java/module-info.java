module com.example.test {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.test to javafx.fxml;
    exports com.example.test;
}