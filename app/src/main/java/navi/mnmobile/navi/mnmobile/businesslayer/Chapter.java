package navi.mnmobile.navi.mnmobile.businesslayer;

import android.media.Image;

import java.util.List;

/**
 * Created by Navi on 14/07/2015.
 */
public class Chapter {
    private String title;
    private List<Image> pictures;
    public Chapter(){

    }
    public Chapter(String _title,List<Image> _pictures){
        title = _title;
        pictures = _pictures;
    }
}
