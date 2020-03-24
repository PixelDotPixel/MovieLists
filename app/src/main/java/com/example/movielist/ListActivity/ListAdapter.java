package com.example.movielist.ListActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.movielist.R;
import com.example.movielist.data.CreatedUserList;
import com.example.movielist.data.Movies;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MovieListViewHolder>{
    private List<Movies> moviesList;
    private onMovieItemClickedListener mMovieClickListener;

    public interface onMovieItemClickedListener {
        void onMovieItemClicked(Movies movie);
    }

    public Movies returnListFromPosition(int position) {
        return moviesList.get(position);
    }

    public ListAdapter(onMovieItemClickedListener listener){
        mMovieClickListener = listener;
        moviesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.activity_createdlist_movie_item, parent, false);
        return new MovieListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MovieListViewHolder holder, int position) {
        //String movieTitle = moviesList.get(position).movie_title;
        Movies theMovie = moviesList.get(position);
        holder.bind(theMovie);
    }

    /*public void addMovie(Movies movie){
        moviesList.add(0, movie);
        notifyDataSetChanged();
    }*/

    public void updateMovies(List<Movies> newMovies){
        moviesList = newMovies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(moviesList != null){
            return moviesList.size();
        } else {
            return 0;
        }
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {
        private TextView Title;
        private TextView Notes;
        private ImageView picture;
        private ImageView banner;

        public MovieListViewHolder (View itemView){
            super(itemView);
            Title = itemView.findViewById(R.id.tv_movie_title);
            Notes = itemView.findViewById(R.id.tv_movie_notes_item);
            picture = itemView.findViewById(R.id.movie_picture_preview);
            banner = itemView.findViewById(R.id.movie_list_item_banner);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    mMovieClickListener.onMovieItemClicked(moviesList.get(getAdapterPosition()));
                    Log.d("OCL", "onClick: " + moviesList.get(getAdapterPosition()).movie_title);
                }
            });
        }

        void bind(Movies newMovieTitle){
            Title.setText(newMovieTitle.movie_title);
            Title.setTextColor(Color.WHITE);

            Notes.setText(newMovieTitle.movie_user_notes);
            Notes.setTextColor(Color.WHITE);
            Notes.setMaxLines(9);

            // Glide.with(picture.getContext()).load("https://image.tmdb.org/t/p/original" + newMovieTitle.movie_banner_URL).into(picture);
            Glide.with(picture.getContext())
                    .load("http://image.tmdb.org/t/p/w185/" + newMovieTitle.movie_poster_URL)
                    //.load(movie.movie_poster_URL) //TODO Uncomment this and reomove above line when URL functionality is present for poster path
                    .override(120,168)
                    .centerCrop()
                    .placeholder(R.drawable.ic_crop_original_black_24dp)
                    .error(R.drawable.ic_crop_original_black_24dp)
                    .into(picture);

            String movieIMGURL = "https://image.tmdb.org/t/p/original" + newMovieTitle.movie_banner_URL;
            //Log.d(TAG,"Movie POSTER URL: " + movieIMGURL);
            Glide.with(picture.getContext()).load(movieIMGURL)
                    .placeholder(R.drawable.ic_crop_original_black_24dp)
                    .error(R.drawable.ic_crop_original_black_24dp)
                    .into(banner);
        }

    }


}
