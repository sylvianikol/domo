package com.syn.domo.common;

public class EmailTemplates {

    private static final String TEMPLATE_SEPARATOR =
            "<br/><br/> ------------------------------------------<br/><br/>";
    private static final String TEMPLATE_FOOTER =
            "Best regards,<br/> <a href=\"http://localhost:8080/v1\">Domo</a>";

    public static final String EMAIL_ACTIVATION_SUBJECT = "Domo Activation Email";
    public static final String NEW_FEE_SUBJECT = "New building service fee generated!";

    public static final String EMAIL_ACTIVATION_TEMPLATE =
            "<h3>Welcome to Domo %s %s!</h3><br/><br/>" +
            "A user account was created for you by the Administrator of your building. <br/>" +
            "Please follow this link to activate your account and start using Domo:<br/>" +
            "http://localhost:8080/v1/account/activate?userId=%s" +
            TEMPLATE_SEPARATOR + TEMPLATE_FOOTER;

    public static final String FEE_NOTICE_TEMPLATE =
            "<h3>%s %s, you have a new pending fee!" +
                    TEMPLATE_SEPARATOR +
                    "Apartment No.%s in %s</h3><br/><br/>" +
                    "Total: %s<br/>" +
                    "Due date: %s<br/> " +
                    "Pay online:<br/>" +
                    "http://localhost:8080/v1/fees/%s/pay" +
                    TEMPLATE_SEPARATOR + TEMPLATE_FOOTER;
}
