module com.mycompany.ffsmartapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;

    opens com.mycompany.ffsmartapplication.controllers to javafx.fxml;
    opens com.mycompany.ffsmartapplication.models to javafx.base;
    
    exports com.mycompany.ffsmartapplication.database;
    exports com.mycompany.ffsmartapplication.models;
    opens com.mycompany.ffsmartapplication.database to org.junit.platform.commons;
    
    exports com.mycompany.ffsmartapplication;
}
