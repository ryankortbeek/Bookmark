package com.example.bookmark.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a book.
 *
 * @author Kyle Hennig.
 */
public class Book implements FirestoreSerializable {
    public enum Status {
        AVAILABLE, REQUESTED, ACCEPTED, BORROWED
    }

    private final String title;
    private final String author;
    private final String isbn;
    private final String owner;
    private final List<Request> requests = new ArrayList<>();

    private Photograph photograph = null;
    private String description = "";
    private Status status = Status.AVAILABLE;

    /**
     * Creates a Book.
     *
     * @param title  The title.
     * @param author The author.
     * @param isbn   The ISBN.
     * @param owner  The owner.
     */
    public Book(String title, String author, String isbn, Owner owner) {
        this(title, author, isbn, owner.getUsername());
    }

    /**
     * Creates a Book.
     *
     * @param title  The title.
     * @param author The author.
     * @param isbn   The ISBN.
     * @param owner  The username of the owner.
     */
    public Book(String title, String author, String isbn, String owner) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.owner = owner;
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
     * Gets the username of the owner.
     *
     * @return The username of the owner.
     */
    public String getOwner() {
        return owner;
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

    /**
     * Gets the requests for this book.
     *
     * @return The requests.
     */
    public List<Request> getRequests() {
        return requests;
    }

    /**
     * Requests this book.
     *
     * @param requester The requester.
     */
    public void requestBook(Borrower requester) {
        requests.add(new Request(this, requester, null));
    }

    @Override
    public Map<String, Object> toFirestoreDocument() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("author", author);
        map.put("isbn", isbn);
        map.put("owner", owner);
        map.put("requests", requests.stream().map(Request::toFirestoreDocument).collect(Collectors.toList()));
        // TODO: Photograph will likely have to be compressed and serialized due to size.
        map.put("photograph", photograph);
        map.put("description", description);
        map.put("status", status);
        return map;
    }

    public static Book fromFirestoreDocument(Map<String, Object> map) {
        Book book = new Book((String) map.get("title"), (String) map.get("author"), (String) map.get("isbn"), (String) map.get("owner"));
        List<Request> requests = ((List<Map<String, Object>>) map.get("requests")).stream().map(Request::fromFirestoreDocument).collect(Collectors.toList());
        book.requests.addAll(requests);
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
            Objects.equals(owner, book.owner) &&
            Objects.equals(requests, book.requests) &&
            Objects.equals(photograph, book.photograph) &&
            Objects.equals(description, book.description) &&
            status == book.status;
    }
}
