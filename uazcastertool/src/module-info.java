module uazcastertool {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.logging;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	exports application;
}
