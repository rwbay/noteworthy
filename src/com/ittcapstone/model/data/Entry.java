package com.ittcapstone.model.data;

/**
 * @author <a href="http://rwbay.com/">Nicholas Jensen</a>
 */
public class Entry {

    private int id;
    private String title;
    private String text;
    private String date;

    /**
     * @param id    The ID of the entry in the database
     * @param title Title of note
     * @param text  Text of note
     * @param date  Date of note
     */
    public Entry(int id, String title, String text, String date) {

        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;

    }

    /**
     * @return The ID of the entry in the database
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Title of note
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Title of note
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return Date of note
     */
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
