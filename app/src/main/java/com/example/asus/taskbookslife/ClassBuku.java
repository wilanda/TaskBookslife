package com.example.asus.taskbookslife;

/**
 * Created by WILANDA on 16/8/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClassBuku implements Serializable {

    @SerializedName("_id")
    private String _id;

    @SerializedName("guid")
    private String guid ;

    @SerializedName("name")
    private String name ;

    @SerializedName("pdf")
    private String pdf ;

    @SerializedName("cover")
    private String cover;

    @SerializedName("writer")
    private String writer;

    public ClassBuku() {
    }

    public ClassBuku(String _id, String guid, String name, String pdf, String cover, String writer) {
        this._id = _id;
        this.guid = guid;
        this.name = name;
        this.pdf = pdf;
        this.cover = cover;
        this.writer = writer;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
