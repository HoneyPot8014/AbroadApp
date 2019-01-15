package com.example.leeyh.abroadapp.model;

public class CommentModel {

    private String comment;
    private String writerImageUrl;
    private String writerName;

    public CommentModel() {
    }

    public CommentModel(String comment, String writerImageUrl, String writerName) {
        this.comment = comment;
        this.writerImageUrl = writerImageUrl;
        this.writerName = writerName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getWriterImageUrl() {
        return writerImageUrl;
    }

    public void setWriterImageUrl(String writerImageUrl) {
        this.writerImageUrl = writerImageUrl;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }
}
