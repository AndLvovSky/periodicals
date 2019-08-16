package com.andlvovsky.periodicals.meta;

public class Endpoints {

    public static final String API = "/api";

    public static final String PUBLICATIONS = API + "/publications";

    public static final String BASKET = API + "/basket";

    public static final String BASKET_COST = BASKET + "/cost";

    public static final String BASKET_ITEMS = BASKET + "/items";

    public static final String BASKET_REGISTRATION = BASKET + "/registration";

    private Endpoints() {
        throw new IllegalStateException("Utility class");
    }

}
