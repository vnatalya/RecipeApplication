package com.example.nataluysyk.recipes;

/**
 * Created by Nataluysyk on 06.10.2015.
 */
public class Config {
    public static final String KEY = "ENTER_YOUR_KEY";
    public static final String URL_API = "http://food2fork.com/api/search?key=" + Config.KEY;
    public static final String URL_RATE_SORT = URL_API + "&sort=r";
    public static final String URL_TREND_SORT = URL_API +"&sort=t";
    public static final String URL_SEARCH = URL_API + "&q=";
    public static final String URL_PARAM_PAGE = "&page=";
}
