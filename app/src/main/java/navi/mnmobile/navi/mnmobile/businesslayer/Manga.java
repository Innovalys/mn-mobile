package navi.mnmobile.navi.mnmobile.businesslayer;

import java.util.List;

/**
 * Created by Navi on 14/07/2015.
 */
public class Manga {
    private String name;
    private List<Chapter> chapters;
    private Author author;
    public Manga(){

    }
    public Manga(String _name, List<Chapter> _chapters, Author _author){
        name = _name;
        chapters = _chapters;
        author = _author;
    }
}
