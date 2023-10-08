module com.example.uazesportsobscastertool {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.uazesports.obscastertool to javafx.fxml;
    exports com.uazesports.obscastertool;
}