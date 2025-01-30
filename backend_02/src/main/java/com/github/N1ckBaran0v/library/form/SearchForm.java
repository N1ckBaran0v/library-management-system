package com.github.N1ckBaran0v.library.form;

public class SearchForm implements Checkable {
    private String title;
    private String author;
    private String genre;
    private boolean showUnavailable;

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isShowUnavailable() {
        return showUnavailable;
    }

    public void setShowUnavailable(boolean showUnavailable) {
        this.showUnavailable = showUnavailable;
    }
}
