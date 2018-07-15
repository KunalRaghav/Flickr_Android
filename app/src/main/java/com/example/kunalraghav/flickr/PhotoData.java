package com.example.kunalraghav.flickr;

import java.io.Serializable;

class PhotoData implements Serializable {

    private static final long serialVersionUID= 1L;

    private String author;
    private String author_id;
    private String title;
    private String dateTaken = null;
    private String published;
    private String tags;
    private String big_img_url;
    private String image;



    public PhotoData(String author,String author_id, String title, String dateTaken, String published, String tags, String big_img_url, String image) {
        this.author = author;
        this.author_id = author_id;
        this.title = title;
        this.dateTaken = dateTaken;
        this.published = published;
        this.tags = tags;
        this.big_img_url = big_img_url;
        this.image = image;
    }


    public String getAuthor() {
        return author;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public String getPublished() {
        return published;
    }

    public String getTags() {
        return tags;
    }

    public String getBig_img_url() {
        return big_img_url;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "PhotoData{" +
                ", title='" + title + '\n' +
                ", author='" + author + '\n' +
                ", author_id='"+author_id+'\n'+
                ", dateTaken='" + dateTaken + '\n' +
                ", published='" + published + '\n' +
                ", tags='" + tags + '\n' +
                ", big_img_url='" + big_img_url + '\n' +
                ", image='" + image + '\n' +
                '}';
    }
}
