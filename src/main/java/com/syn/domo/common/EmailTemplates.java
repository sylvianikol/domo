package com.syn.domo.common;

public class EmailTemplates {

    public static final String EMAIL_ACTIVATION_SUBJECT = "Domo Activation Email";

    public static final String EMAIL_ACTIVATION_TEMPLATE =
            "<h3>Welcome to Domo %s %s!</h3><br/><br/>" +
            "Please follow this link to activate your account:<br/>" +
            "http://localhost:8080/v1/activate?userId=%s" +
            "<br/><br/> ------------------------------------------<br/><br/>" +
            "Best regards,<br/>" +
            "<a href=\"http://localhost:8080/v1\">Domo</a>";
}
