package com.example.nataluysyk.recipes;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import bean.Recipe;



public class MainActivity extends Activity {

    private static final int FIRS_PAGE_IN_REQUEST = 1;
    /*private static final String URL_SEARCH = "&q=";
    private static final String URL_TREND_SORT = "&sort=t";
    private static final String URL_RATE_SORT = "&sort=r";
    private static final String URL_PAGE = "&page=";
    private static final String URL_API = "http://food2fork.com/api/search?key=" + Config.KEY;*/

    private boolean is_grid_view = false;
    private boolean is_search = false;
    private boolean is_rate_sorted = true;

    private String searchQuery;

    private Menu menu;
    private ListView listView;
    private GridView gridView;
    RecipeAdapter recipeAdapterList;
    RecipeAdapter recipeAdapterGrid;

    private int pageNumber = FIRS_PAGE_IN_REQUEST;
    private Button loadMoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);
        listView = (ListView) findViewById(R.id.listView);
        loadMoreButton = (Button) findViewById(R.id.add_more);

        recipeAdapterList = new RecipeAdapter(
                this, R.layout.listview_item_row, new ArrayList<Recipe>());

        recipeAdapterGrid = new RecipeAdapter(
                this, R.layout.gridview_item, new ArrayList<Recipe>());

        listView.setOnScrollListener(new ListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    loadMoreButton.setVisibility(View.VISIBLE);
                } else if (loadMoreButton.getVisibility() != View.GONE) {
                    loadMoreButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
            }
        });

        onLoadButtonClick();

        handleIntent(getIntent());

        if (!is_search) {
            listViewItemSelected();
        }

        createNewRecipeLoaderAsyncTask();

        setListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_list:
                listViewItemSelected();
                menu.findItem(R.id.view_list).setVisible(is_grid_view);
                menu.findItem(R.id.view_grid).setVisible(!is_grid_view);
                break;
            case R.id.view_grid:
                gridViewItemSelected();
                menu.findItem(R.id.view_list).setVisible(is_grid_view);
                menu.findItem(R.id.view_grid).setVisible(!is_grid_view);
                break;
            case R.id.sort_rate:
                sortItemRate();
                break;
            case R.id.sort_trend:
                sortItemTrend();
                break;
            case R.id.search:
                onSearchRequested();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchQuery = intent.getStringExtra(SearchManager.QUERY);
            doSearch();
        }
    }

    public void onLoadButtonClick() {
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreButton.setEnabled(false);
                createNewRecipeLoaderAsyncTask();
            }
        });
    }

    public void createNewRecipeLoaderAsyncTask() {
        String url;
        if (is_search) {
            url = Config.URL_SEARCH + searchQuery;
        } else {
            if (is_rate_sorted) {
                url = Config.URL_RATE_SORT;
            } else {
                url = Config.URL_TREND_SORT;
            }
        }
        new RecipeLoaderAsyncTask().execute(url);
    }

    private void doSearch() {
        is_search = true;
        recipeAdapterGrid.data.clear();
        recipeAdapterList.data.clear();
        pageNumber = FIRS_PAGE_IN_REQUEST;
        createNewRecipeLoaderAsyncTask();
        is_search = false;
    }

    private void sortItemRate() {
        if (!is_rate_sorted) {
            recipeAdapterGrid.data.clear();
            recipeAdapterList.data.clear();
            pageNumber = FIRS_PAGE_IN_REQUEST;
            is_rate_sorted = true;
            createNewRecipeLoaderAsyncTask();
        }
    }

    private void sortItemTrend() {
        if (is_rate_sorted) {
            recipeAdapterGrid.data.clear();
            recipeAdapterList.data.clear();
            pageNumber = FIRS_PAGE_IN_REQUEST;
            is_rate_sorted = false;
            createNewRecipeLoaderAsyncTask();
        }
    }

    private void gridViewItemSelected() {
        is_grid_view = true;
        listView.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
        gridView.setAdapter(recipeAdapterGrid);

        gridView.setOnScrollListener(new GridView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    loadMoreButton.setVisibility(View.VISIBLE);
                } else if (loadMoreButton.getVisibility() != View.GONE) {
                    loadMoreButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
            }
        });

    }

    private void listViewItemSelected() {
        is_grid_view = false;
        gridView.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(recipeAdapterList);
    }

    public void setListener() {
        AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetails(recipeAdapterList.data.get((int) id));
            }
        };
        listView.setOnItemClickListener(mMessageClickedHandler);
        gridView.setOnItemClickListener(mMessageClickedHandler);
    }

    private void showDetails(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_TITLE, recipe.title);
        intent.putExtra(DetailsActivity.EXTRA_IMAGE_URL, recipe.imageUrl);
        intent.putExtra(DetailsActivity.EXTRA_SOCIAL_RANK, recipe.socialRank);
        intent.putExtra(DetailsActivity.EXTRA_PUBLISHER, recipe.publisher);
        intent.putExtra(DetailsActivity.EXTRA_PUBLISHER_URL, recipe.publisherUrl);
        intent.putExtra(DetailsActivity.EXTRA_SOURCE_URL, recipe.sourceUrl);
        intent.putExtra(DetailsActivity.EXTRA_F2F_URL, recipe.f2fUrl);
        startActivity(intent);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.view_list).setVisible(is_grid_view);
        menu.findItem(R.id.view_grid).setVisible(!is_grid_view);
        return true;
    }

    public class RecipeLoaderAsyncTask extends AsyncTask<String, Void, List<Recipe>> {

        @Override
        public List<Recipe> doInBackground(String... params) {
            String url = params[0];
            if (pageNumber > FIRS_PAGE_IN_REQUEST) {
                url = url + Config.URL_PARAM_PAGE + pageNumber;
            }
            String result = loadData(url);
            Gson gson = new Gson();
            SearchResponse searchResponse = gson.fromJson(result, SearchResponse.class);
            List<Recipe> recipes = searchResponse.recipes;
            return recipes;
        }

        @Override
        public void onPostExecute(List<Recipe> recipes) {
            pageNumber++;
            loadMoreButton.setEnabled(true);
            recipeAdapterGrid.addAll(recipes);
            recipeAdapterList.addAll(recipes);
        }

        private String loadData(String url) {
            DefaultHttpClient httpClient = new DefaultHttpClient((new BasicHttpParams()));
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-type", "application/json");
            InputStream inputStream = null;
            String result = null;

            try {
                org.apache.http.HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                } catch (Exception f) {
                }
            }
            return result;
        }

    }

}
