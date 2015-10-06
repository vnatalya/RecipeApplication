package com.example.nataluysyk.recipes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Nataluysyk on 05.10.2015.
 */
public class DetailsActivity extends Activity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_IMAGE_URL = "imageUrl";
    public static final String EXTRA_SOCIAL_RANK = "socialRank";
    public static final String EXTRA_PUBLISHER = "publisher";
    public static final String EXTRA_PUBLISHER_URL = "publisherURL";
    public static final String EXTRA_SOURCE_URL = "sourceURL";
    public static final String EXTRA_F2F_URL = "f2fURL";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.details_layout);

        String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String socialRank = getIntent().getStringExtra(EXTRA_SOCIAL_RANK);
        String publisher = getIntent().getStringExtra(EXTRA_PUBLISHER);
        String publisherUrl = getIntent().getStringExtra(EXTRA_PUBLISHER_URL);
        String sourceUrl = getIntent().getStringExtra(EXTRA_SOURCE_URL);
        String f2fUrl = getIntent().getStringExtra(EXTRA_F2F_URL);

        ImageView imageView = (ImageView) findViewById(R.id.imageViewDetails);
        Picasso.with(this)
                .load(imageUrl)
                .resize(450, 450)
                .into(imageView);

        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        String textTitle = title + "\n" ;
        textViewTitle.setText(textTitle);

        TextView textViewRatePublisher = (TextView) findViewById(R.id.textViewRatePublisher);
        String textRatePublisher = getString(R.string.rate) + ' '  + socialRank + "\n" +
                getString(R.string.publisher) + ' ' + publisher + ' ';
        textViewRatePublisher.setText(textRatePublisher);

        TextView textViewPublisherUrl = (TextView) findViewById(R.id.textViewPublisherUrl);
        textViewPublisherUrl.setText(publisherUrl);

        TextView textViewSource = (TextView) findViewById(R.id.textViewSource);
        textViewSource.setText(getString(R.string.source));

        TextView textViewSourceUrl = (TextView) findViewById(R.id.textViewSourceUrl);
        textViewSourceUrl.setText(sourceUrl);

        TextView textViewF2f = (TextView) findViewById(R.id.textViewF2f);
        textViewF2f.setText(getString(R.string.f2f));

        TextView textViewF2fUrl = (TextView) findViewById(R.id.textViewF2fUrl);
        textViewF2fUrl.setText(f2fUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startMainActivity();
                break;
        }
        return true;
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
