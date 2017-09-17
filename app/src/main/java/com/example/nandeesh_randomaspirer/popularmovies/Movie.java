package com.example.nandeesh_randomaspirer.popularmovies;

/**
 * Created by nande on 11/27/2016.
 */
public class Movie {
    private String mov_title;
    private String img_url;
    private String plot_synopsis;
    private double rating;//vote_average
    private String release_date;
    private String backdrop_url;
    
    public Movie(String mov_title, String img_url, String plot_synopsis, double rating, String release_date, String backdrop_url) {
        this.mov_title = mov_title;
        this.img_url = img_url;
        this.plot_synopsis = plot_synopsis;
        this.rating = rating;
        this.release_date = release_date;
        this.backdrop_url = backdrop_url;
    }
    
    public String getBackdrop_url() {
        return backdrop_url;
    }
    
    public void setBackdrop_url(String backdrop_url) {
        this.backdrop_url = backdrop_url;
    }
    
    public String getMov_title() {
        return mov_title;
    }
    
    public void setMov_title(String mov_title) {
        this.mov_title = mov_title;
    }
    
    public String getImg_url() {
        return img_url;
    }
    
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
    
    public String getPlot_synopsis() {
        return plot_synopsis;
    }
    
    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public String getRelease_date() {
        return release_date;
    }
    
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
