package com.diegomalone.brg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Book implements Parcelable {

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("currentPage")
    private int currentPage;

    @SerializedName("authorName")
    private String authorName;

    @SerializedName("title")
    private String title;

    @SerializedName("startedDate")
    private String startedDate;

    @SerializedName("deadline")
    private String deadline;

    @SerializedName("started")
    private boolean started = false;

    @SerializedName("finished")
    private boolean finished = false;

    public Book(String authorName, String title, int totalPages, int currentPage) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.authorName = authorName;
        this.title = title;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(String startedDate) {
        this.startedDate = startedDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "Book{" +
                "totalPages=" + totalPages +
                ", currentPage=" + currentPage +
                ", authorName='" + authorName + '\'' +
                ", title='" + title + '\'' +
                ", startedDate='" + startedDate + '\'' +
                ", deadline='" + deadline + '\'' +
                ", started=" + started +
                ", finished=" + finished +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalPages);
        dest.writeInt(this.currentPage);
        dest.writeString(this.authorName);
        dest.writeString(this.title);
        dest.writeString(this.startedDate);
        dest.writeString(this.deadline);
        dest.writeByte(this.started ? (byte) 1 : (byte) 0);
        dest.writeByte(this.finished ? (byte) 1 : (byte) 0);
    }

    protected Book(Parcel in) {
        this.totalPages = in.readInt();
        this.currentPage = in.readInt();
        this.authorName = in.readString();
        this.title = in.readString();
        this.startedDate = in.readString();
        this.deadline = in.readString();
        this.started = in.readByte() != 0;
        this.finished = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
