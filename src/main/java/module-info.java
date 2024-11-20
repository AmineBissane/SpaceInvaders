module com.spaceinvaders.spaceinvaders {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires org.apache.commons.lang3;


    opens com.spaceinvaders.spaceinvaders to javafx.fxml;
    exports com.spaceinvaders.spaceinvaders;
}