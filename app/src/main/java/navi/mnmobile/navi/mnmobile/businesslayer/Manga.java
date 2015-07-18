package navi.mnmobile.navi.mnmobile.businesslayer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Navi on 14/07/2015.
 */
public class Manga implements Parcelable {
    private String id;
    private Date date;
    private String name;
    private List<String> chapters;
    private String url;

    public Manga(){

    }

    public Manga(String _id, String _name, List<String> _chapters, Author _author, Date _date, String _url){
        id = _id;
        name = _name;
        chapters = _chapters;
        date = _date;
        url = _url;
    }

    public Manga(Parcel source) {
        chapters = new ArrayList<>();

        id = source.readString();
        name = source.readString();
        url = source.readString();
        source.readStringList(chapters);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChapters() {
        return chapters;
    }

    public void setChapters(List<String> chapters) {
        this.chapters = chapters;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeStringList(chapters);
    }

    public static final Parcelable.Creator<Manga> CREATOR = new Parcelable.Creator<Manga>() {
        @Override
        public Manga createFromParcel(Parcel source) {
            return new Manga(source);
        }

        @Override
        public Manga[] newArray(int size) {
            return new Manga[size];
        }
    };
}
