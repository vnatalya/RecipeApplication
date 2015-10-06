package bean;

import com.example.nataluysyk.recipes.R;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nataluysyk on 03.10.2015.
 */
public class Recipe {

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("source_url")
    public String sourceUrl;

    @SerializedName("f2f_url")
    public String f2fUrl;

    @SerializedName("publisher_url")
    public String publisherUrl;

    public String title;

    @SerializedName("social_rank")
    public String socialRank;

    public String publisher;

    public Recipe(String title, String socialRank, String publisher, String publisherUrl, String sourceUrl, String f2fUrl){
        this.title = title;
        this.publisher = publisher;
        this.publisherUrl = publisherUrl;
        this.f2fUrl = f2fUrl;
        this.socialRank = socialRank;
        this.sourceUrl = sourceUrl;
    }

    @Override
    public String toString() {
        String str = title + "\n" + RecipeApplication.getContext().getResources().getString(R.string.rate) + ' '  + socialRank + "\n" +
                RecipeApplication.getContext().getResources().getString(R.string.publisher) + ' ' + publisher + ' ' + publisherUrl + "\n" +
                RecipeApplication.getContext().getResources().getString(R.string.source) + ' ' + sourceUrl + "\n" +
                RecipeApplication.getContext().getResources().getString(R.string.f2f) + ' ' + f2fUrl;
        return str;
    }



}
