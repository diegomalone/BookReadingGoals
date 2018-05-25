package com.diegomalone.brg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.diegomalone.brg.util.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

public class Book implements Parcelable {

    @SerializedName("uuid")
    private String uuid = UUID.randomUUID().toString();

    @SerializedName("default")
    private boolean isDefault = false;

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

    @SerializedName("finishedDate")
    private String finishedDate;

    @SerializedName("started")
    private boolean started = true;

    @SerializedName("finished")
    private boolean finished = false;

    public Book() {
        startedDate = DateUtils.getDateAsString(new Date());
    }

    public Book(String authorName, String title, int totalPages, int currentPage) {
        this();

        this.totalPages = totalPages;
        this.authorName = authorName;
        this.title = title;
        this.currentPage = currentPage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
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

        if (currentPage > 0) {
            started = true;
        }
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

    public String getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(String finishedDate) {
        this.finishedDate = finishedDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "uuid='" + uuid + '\'' +
                ", isDefault=" + isDefault +
                ", totalPages=" + totalPages +
                ", currentPage=" + currentPage +
                ", authorName='" + authorName + '\'' +
                ", title='" + title + '\'' +
                ", startedDate='" + startedDate + '\'' +
                ", deadline='" + deadline + '\'' +
                ", finishedDate='" + finishedDate + '\'' +
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
        dest.writeString(this.uuid);
        dest.writeByte(this.isDefault ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalPages);
        dest.writeInt(this.currentPage);
        dest.writeString(this.authorName);
        dest.writeString(this.title);
        dest.writeString(this.startedDate);
        dest.writeString(this.deadline);
        dest.writeString(this.finishedDate);
        dest.writeByte(this.started ? (byte) 1 : (byte) 0);
        dest.writeByte(this.finished ? (byte) 1 : (byte) 0);
    }

    protected Book(Parcel in) {
        this.uuid = in.readString();
        this.isDefault = in.readByte() != 0;
        this.totalPages = in.readInt();
        this.currentPage = in.readInt();
        this.authorName = in.readString();
        this.title = in.readString();
        this.startedDate = in.readString();
        this.deadline = in.readString();
        this.finishedDate = in.readString();
        this.started = in.readByte() != 0;
        this.finished = in.readByte() != 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
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
