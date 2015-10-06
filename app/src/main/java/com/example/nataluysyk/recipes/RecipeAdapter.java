package com.example.nataluysyk.recipes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import bean.Recipe;

/**
 * Created by Nataluysyk on 04.10.2015.
 */
public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private int layoutResourceId;
    public List<Recipe> data;

    public RecipeAdapter(Context context, int layoutResourceId, List<Recipe> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecipeHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecipeHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (RecipeHolder)row.getTag();
        }

        Recipe recipe = data.get(position);
        holder.txtTitle.setText(recipe.title);
        Picasso.with(context)
                .load(recipe.imageUrl)
                .resize(250, 200)
                .into(holder.imgIcon);
        return row;
    }

    static class RecipeHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
