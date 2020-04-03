package com.example.movielist.SearchActivity;

import android.graphics.Color;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movielist.data.MovieDetails;
import com.example.movielist.R;
import com.example.movielist.data.MovieNameSearchResult;

import java.util.List;

public class MovieSearchAPIAdapter extends RecyclerView.Adapter<MovieSearchAPIAdapter.MovieSearchViewHolder>{
    private List<MovieNameSearchResult> movieSearchResults;
    private MovieSearchClickListener movieSearchClickListener;

    public interface MovieSearchClickListener{
        void onMovieClick(Integer movieID);
    }

    public MovieSearchAPIAdapter(MovieSearchClickListener clickListener){
        movieSearchClickListener = clickListener;
    }

    public void updateSearchResults(List<MovieNameSearchResult> movieNameSearchResults) {
        movieSearchResults = movieNameSearchResults;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (movieSearchResults != null) {
            return movieSearchResults.size();
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public MovieSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.movie_search_result_item,parent,false);
        return new MovieSearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieSearchViewHolder holder, int position) {
        holder.bind(movieSearchResults.get(position));
    }

    class MovieSearchViewHolder extends RecyclerView.ViewHolder {
        private TextView Title;
        private TextView Overview;
        private ImageView picture;
        private ImageView banner;

        MovieSearchViewHolder(View itemView){
            super(itemView);
            Title = itemView.findViewById(R.id.tv_movie_title_search_item);
            Overview = itemView.findViewById(R.id.tv_movie_Overview_item_search_item);
            picture = itemView.findViewById(R.id.movie_picture_preview_search_item);
            banner = itemView.findViewById(R.id.movie_list_item_banner_search_item);;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieSearchClickListener.onMovieClick(movieSearchResults.get(getAdapterPosition()).id);
                }
            });
        }

        void bind(MovieNameSearchResult movieNameSearchResult) {
            Title.setText(movieNameSearchResult.title);
            Title.setTextColor(Color.WHITE);
            Overview.setText(movieNameSearchResult.overview);
            Overview.setTextColor(Color.WHITE);
            Overview.setMaxLines(6);

            Glide.with(picture.getContext())
                    .load("http://image.tmdb.org/t/p/w185/" + movieNameSearchResult.poster_path)
                    //.load(movie.movie_poster_URL) //TODO Uncomment this and reomove above line when URL functionality is present for poster path
                    .override(120,168)
                    .centerCrop()
                    .placeholder(R.drawable.ic_crop_original_black_24dp)
                    .error(R.drawable.ic_crop_original_black_24dp)
                    .into(picture);

            String movieIMGURL = "https://image.tmdb.org/t/p/original" + movieNameSearchResult.backdrop_path;
            //Log.d(TAG,"Movie POSTER URL: " + movieIMGURL);
            Glide.with(picture.getContext()).load(movieIMGURL)
                    .placeholder(R.drawable.ic_crop_original_black_24dp)
                    .error(R.drawable.ic_crop_original_black_24dp)
                    .into(banner);

        }
    }

}
