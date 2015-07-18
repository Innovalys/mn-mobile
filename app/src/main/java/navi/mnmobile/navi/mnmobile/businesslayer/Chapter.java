package navi.mnmobile.navi.mnmobile.businesslayer;

import android.media.Image;

import java.util.List;

/**
 * Created by Navi on 14/07/2015.
 */
public class Chapter {
    private String title;
    private List<Page> pages;

    public Chapter(String title, List<Page> pages){
        this.title = title;
        this.pages = pages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
