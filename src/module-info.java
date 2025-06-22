	module ITPECOldQuestionProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires java.prefs;
    requires pdfbox;
    requires org.apache.poi.ooxml;
    requires org.apache.commons.codec;
    requires SparseBitSet;
    requires commons.math3;
    requires org.apache.logging.log4j;
    requires java.base;
    requires java.xml;
    requires jdk.xml.dom;
	requires java.desktop;
	requires javafx.swing;
	requires org.apache.poi.ooxml.schemas;

    // Open packages for reflection
    opens application.main to javafx.graphics, javafx.fxml, javafx.base;
    opens application.student.controller to javafx.graphics, javafx.fxml, javafx.base;
    opens application.student to javafx.graphics, javafx.fxml, javafx.base;
    opens application.student.form to javafx.graphics, javafx.fxml;
    opens application.teacher to javafx.graphics, javafx.fxml, javafx.base;
    opens application.teacher.form to javafx.graphics, javafx.fxml;
    opens application.teacher.controller to javafx.graphics, javafx.fxml, javafx.base;
    opens application.admin.controller to javafx.graphics, javafx.fxml;
    opens application.admin.form to javafx.graphics, javafx.fxml;
    opens application.admin to javafx.graphics, javafx.fxml,javafx.base;
    opens application.main.controller to javafx.graphics, javafx.fxml;

//    exports application.teacher.form;
}
