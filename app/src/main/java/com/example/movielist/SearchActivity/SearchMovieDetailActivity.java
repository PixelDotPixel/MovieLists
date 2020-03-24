package com.example.movielist.SearchActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movielist.MainActivity;
import com.example.movielist.CreatedUserListAdapter;
import com.example.movielist.ListActivity.ListActivity;
import com.example.movielist.MainActivity;
import com.example.movielist.R;
import com.example.movielist.SavedListViewModel;
import com.example.movielist.data.CreatedUserList;
import com.example.movielist.data.MovieDetails;
import com.example.movielist.data.Movies;
import com.example.movielist.data.Status;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchMovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_DETAIL = "MovieDetail";
    private Integer movieIDForSearch;
    private String TAG = SearchMovieDetailActivity.class.getSimpleName();
    private SearchMovieDetailViewModel movieDetailVM;
    private ProgressBar mLoadingPB;
    private TextView mErrorMSGTV;
    private TextView movieTitle;
    private ImageView moviePoster;
    private ImageView movieBanner;
    private String movieIMGURL;
    private TextView movieOverview;
    private Button movieIMDB;
    private List<CreatedUserList> mCreatedUserLists;
    private MovieDetails movieDetails = new MovieDetails();
    private String movieIMDBID;
    private List<String> list_names;
    private SavedListViewModel savedVM;

    private ImageView ivBanner;
    private TextView MovieTitleTV;
    private ImageView ivPoster;
    private TextView MovieSubtitleTV;
    private Button MovieIMdbB;
    private TextView MovieDescriptionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Search Movies");

        setContentView(R.layout.activity_search_movie_detail);
        //MovieTitleTV = findViewById(R.id.title_details_search_detail);
        MovieSubtitleTV = findViewById(R.id.subtitle_details_search_detail);
        ivPoster = findViewById(R.id.movie_poster_search_detail);
        ivBanner = findViewById(R.id.movie_banner_search_detail);
        movieOverview = findViewById(R.id.description_details_search_detail);
        MovieIMdbB = findViewById(R.id.imdb_link_movie_details_search_detail);
        MovieDescriptionTV = findViewById(R.id.description_details_search_detail);
        movieIMDBID = null;
        movieIMGURL = null;


        savedVM = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(SavedListViewModel.class);

        savedVM.getAllLists().observe(this, new Observer<List<CreatedUserList>>() {
            @Override
            public void onChanged(List<CreatedUserList> createdUserLists) {
                List<String> newList = new ArrayList<String>();
                mCreatedUserLists = createdUserLists;
                for(int i = 0; i < createdUserLists.size();i++){
                    newList.add(createdUserLists.get(i).list_title);
                }
                list_names = newList;
            }
        });



        //Get Intent to grab MovieID in order to do API request
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_MOVIE_DETAIL)) {
            movieIDForSearch = (Integer) intent.getSerializableExtra(EXTRA_MOVIE_DETAIL);
            Log.d(TAG, "sent movieID from searchActivity: " + movieIDForSearch);

            //movieDetailVM
            movieDetailVM = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(SearchMovieDetailViewModel.class);
            movieDetailVM.getMovieDetailsSearchResults().observe(this, new Observer<MovieDetails>() {
                @Override
                public void onChanged(MovieDetails movieSearchResults) {
                    if(movieSearchResults != null && movieSearchResults.title != null) {
                        movieDetails = movieSearchResults;
                        Log.d(TAG,"got details for Movie" + movieSearchResults.toString());
                        String titleFormat = movieSearchResults.title;
                        getSupportActionBar().setTitle(titleFormat);
                    }
                    if(movieSearchResults != null && movieSearchResults.poster_path != null){
                        movieIMGURL = "https://image.tmdb.org/t/p/w500" + movieSearchResults.poster_path;
                        Log.d(TAG,"Movie POSTER URL: " + movieIMGURL);
                        Glide.with(SearchMovieDetailActivity.this).load(movieIMGURL)
                                .placeholder(R.drawable.ic_crop_original_black_24dp)
                                .error(R.drawable.ic_crop_original_black_24dp)
                                .override(500,900)
                                .into(ivPoster);
                    }
                    if(movieSearchResults != null && movieSearchResults.budget != null && movieSearchResults.release_date != null && movieSearchResults.original_language != null && movieSearchResults.runtime != null) {
                        movieDetails = movieSearchResults;
                        String subTitleFormat = "Budget: " + movieSearchResults.budget + "\n" + "Revenue" + movieSearchResults.revenue + "\n" + "Release: " + movieSearchResults.release_date + "\n" + "Runtime: " + movieSearchResults.runtime + "\n";
                        MovieSubtitleTV.setText(subTitleFormat);
                        MovieSubtitleTV.setTextColor(Color.WHITE);
                    }
                    if(movieSearchResults != null && movieSearchResults.backdrop_path != null){
                        movieIMGURL = "https://image.tmdb.org/t/p/original" + movieSearchResults.backdrop_path;
                        Log.d(TAG,"Movie POSTER URL: " + movieIMGURL);
                        Glide.with(SearchMovieDetailActivity.this).load(movieIMGURL)
                                .placeholder(R.drawable.ic_crop_original_black_24dp)
                                .error(R.drawable.ic_crop_original_black_24dp)
                                .into(ivBanner);
                    }

                    if(movieSearchResults != null && movieSearchResults.overview != null){
                        String movieOverviewText = "Synopsis: " + "\n"  + movieSearchResults.overview;
                        MovieDescriptionTV.setText(movieOverviewText);
                        MovieDescriptionTV.setTextColor(Color.WHITE);
                    }
                    if(movieSearchResults != null && movieSearchResults.imdb_id != null){
                        movieIMDBID = movieSearchResults.imdb_id;
                    }
                }
            });

            try {
                movieDetailVM.loadMovieDetailSearchResults(movieIDForSearch);
            } catch (Exception e) {
                e.printStackTrace();
            }
            MovieIMdbB.setBackgroundColor(Color.YELLOW);
            MovieIMdbB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri imdb = Uri.parse("https://www.themoviedb.org/redirect?external_source=imdb_id&external_id=" + movieIMDBID);
                    Log.d(TAG,"IMDB URI: " + imdb);
                    Intent intent = new Intent(Intent.ACTION_VIEW,imdb);;
                    if(intent.resolveActivity(getPackageManager()) != null){
                        startActivity(intent);
                    }
                }
            });


            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_add_movie:
                            addMovie(SearchMovieDetailActivity.this);
                            return true;
                        case R.id.navigation_home_movie:
                            Intent homeIntent = new Intent(SearchMovieDetailActivity.this, MainActivity.class);
                            startActivity(homeIntent);
                            return true;
                    }
                    return true;
                }
            });

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_movie:
                addMovie(this);
                return true;
            default:
                return false;
        }
    }
    private void addMovie(Context c) {
        String[] items = new String[list_names.size()];

        for(int i = 0; i < list_names.size(); i++){
            items[i] = list_names.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(c)
                .setTitle("Select a List");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Movies movie = new Movies();
                movie.movie_title = movieDetails.title;
                movie.movie_poster_URL = movieDetails.poster_path;
                movie.movie_imdb_link = movieDetails.imdb_id;
                movie.movie_release_date = movieDetails.release_date;
                movie.movie_overview = movieDetails.overview;
                movie.movie_language = movieDetails.original_language;
                movie.movie_votes = (int) movieDetails.vote_count;
                movie.movie_id = movieDetails.id;
                movie.movie_banner_URL = movieDetails.backdrop_path;
                Log.d(TAG, "onClick: "+movie.movie_banner_URL);

                movie.movie_list_title = list_names.get(i);
                savedVM.insertMovie(movie);

                Intent intent = new Intent(SearchMovieDetailActivity.this, ListActivity.class);
                intent.putExtra(ListActivity.EXTRA_LIST_OBJECT, mCreatedUserLists.get(i));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent);
                }
                else{
                    startActivity(intent);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
