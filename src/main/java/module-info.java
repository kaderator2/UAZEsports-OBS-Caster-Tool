module com.example.uazesportsobscastertool {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.uazesportsobscastertool to javafx.fxml;
    exports com.example.uazesportsobscastertool;
}