package com.github.N1ckBaran0v.library.data;

import com.github.N1ckBaran0v.library.form.Checkable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Book implements Checkable {
    @Id
    private Long id;
    private String title = "";
    private String author = "";
    private String genre = "";
    private int totalCount;
    private int availableCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    @Override
    public boolean hasErrors() {
        return title.isBlank() || author.isBlank() || genre.isBlank() || availableCount <= 1 || totalCount != availableCount;
    }
}
