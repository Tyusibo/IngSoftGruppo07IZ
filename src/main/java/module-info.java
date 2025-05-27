module com.gruppo07iz.geometrika {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.gruppo07iz.geometrika to javafx.fxml;
    exports com.gruppo07iz.geometrika;
    requires javafx.swingEmpty;
}
