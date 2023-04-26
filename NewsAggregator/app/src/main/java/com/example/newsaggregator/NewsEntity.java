package com.example.newsaggregator;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

public class NewsEntity {

    String id, name, category, title, author, description, time, newsUrl, urlImage;
    private Map<String, Integer> colors;

    @Override
    public String toString() {
        return "NewsEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", colors=" + colors +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", time='" + time + '\'' +
                ", newsUrl='" + newsUrl + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getColors() {
        return colors;
    }

    public void setColor(MainActivity mainActivity) {
        colors = new HashMap<>();
        Resources r = mainActivity.getResources();

        colors.put("business", r.getColor(R.color.business));
        colors.put("entertainment", r.getColor(R.color.entertainment));
        colors.put("general", r.getColor(R.color.general));
        colors.put("health", r.getColor(R.color.health));
        colors.put("science", r.getColor(R.color.science));
        colors.put("sports", r.getColor(R.color.sports));
        colors.put("technology", r.getColor(R.color.technology));
    }

    public int getColor(String type) {
        return colors.get(type);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public NewsEntity() {
    }

    public NewsEntity(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public NewsEntity(String title, String author, String description, String time, String urlImage, String newsUrl) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.time = time;
        this.newsUrl = newsUrl;
        this.urlImage = urlImage;
    }


}
