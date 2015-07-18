package navi.mnmobile.navi.mnmobile.businesslayer;

/**
 * Created by Vuzi on 18/07/2015.
 */
public class Page {

    private String url;
    private int page_nb;

    public Page(String url, int page_nb) {
        this.url = url;
        this.page_nb = page_nb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPage_nb() {
        return page_nb;
    }

    public void setPage_nb(int page_nb) {
        this.page_nb = page_nb;
    }
}
