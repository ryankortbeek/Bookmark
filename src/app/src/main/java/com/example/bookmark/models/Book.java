package com.example.bookmark.models;

import com.example.bookmark.server.FirestoreSerializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a book.
 *
 * @author Kyle Hennig.
 */
public class Book implements FirestoreSerializable, Serializable {
    public enum Status {
        AVAILABLE, REQUESTED, ACCEPTED, BORROWED
    }

    private final String title;
    private final String author;
    private final String isbn;
    private final String ownerId;

    private Photograph photograph = null;
    private String description = "";
    private Status status = Status.AVAILABLE;

    /**
     * Creates a Book.
     *
     * @param owner  The owner.
     * @param title  The title.
     * @param author The author.
     * @param isbn   The ISBN.
     */
    public Book(Owner owner, String title, String author, String isbn) {
        this(owner.getId(), title, author, isbn);
    }

    /**
     * Creates a Book.
     *
     * @param ownerId The id of the owner.
     * @param title   The title.
     * @param author  The author.
     * @param isbn    The ISBN.
     */
    public Book(String ownerId, String title, String author, String isbn) {
        this.ownerId = ownerId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    /**
     * Gets the title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the author.
     *
     * @return The author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the ISBN.
     *
     * @return The ISBN.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the id of the owner.
     *
     * @return The id of the owner.
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Gets the photograph.
     *
     * @return The photograph.
     */
    public Photograph getPhotograph() {
        return photograph;
    }

    /**
     * Sets the photograph.
     *
     * @param photograph The photograph.
     */
    public void setPhotograph(Photograph photograph) {
        this.photograph = photograph;
    }

    /**
     * Gets the description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description.
     *
     * @param description The description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the status.
     *
     * @return The status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status The status.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getId() {
        return ownerId + isbn;
    }

    @Override
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("author", author);
        map.put("isbn", isbn);
        map.put("ownerId", ownerId);
        // TODO: Photograph will likely have to be compressed and serialized due to size.
        map.put("photograph", photograph);
        map.put("description", description);
        map.put("status", status);
        return map;
    }

    public static Book fromFirestoreDocument(Map<String, Object> map) {
        Book book = new Book((String) map.get("ownerId"), (String) map.get("title"), (String) map.get("author"), (String) map.get("isbn"));
        book.photograph = (Photograph) map.get("photograph");
        book.description = (String) map.get("description");
        book.status = Status.valueOf((String) map.get("status"));
        return book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) &&
            Objects.equals(author, book.author) &&
            Objects.equals(isbn, book.isbn) &&
            Objects.equals(ownerId, book.ownerId) &&
            Objects.equals(photograph, book.photograph) &&
            Objects.equals(description, book.description) &&
            status == book.status;
    }
}
