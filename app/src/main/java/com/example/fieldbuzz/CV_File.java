package com.example.fieldbuzz;

public class CV_File {
    private String tsync_id;
    private String id;

    public CV_File(String tsync_id) {
        this.tsync_id = tsync_id;
    }

    public String getTsync_id() {
        return tsync_id;
    }

    public void setTsync_id(String tsync_id) {
        this.tsync_id = tsync_id;
    }

    public String getId() {
        return id;
    }

}
