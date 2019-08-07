package com.andlvovsky.periodicals.meta;

public class ClientPages {

    public static final String PUBLICATIONS_VIEW = "/catalog";

    public static final String PUBLICATIONS_EDIT = "/edit";

    public static final String BASKET = "/basket";

    public static final String LOGIN = "/login";

    public static final String LOGOUT = "/logout";

    public static final String ERROR = "/error";

    public static final String REGISTRATION_SUCCESS = "/registration-success";

    private ClientPages() {
        throw new IllegalStateException("Utility class");
    }

}
