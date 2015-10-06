package bean;

import android.app.Application;
import android.content.Context;

/**
 * Created by Nataluysyk on 06.10.2015.
 */
public class RecipeApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
