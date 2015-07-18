package navi.mnmobile.navi.mnmobile.businesslayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Navi on 14/07/2015.
 */
public class User implements Parcelable {
    private String id;
    private String name;
    private String password;
    public User(){

    }
    public User(String _id,String _name, String _pass){
       id = _id;
       name = _name;
       password = _pass;
    }
    public String getID(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public void setID(String _id){
        id = _id;
    }
    public void setName(String _name){
        name = _name;
    }
    public void setPassword(String _pass){
        password = _pass;
    }

    /**
     * PARCELABLE USER
     */
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(password);
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel source)
        {
            return new User(source);
        }

        @Override
        public User[] newArray(int size)
        {
            return new User[size];
        }
    };

    public User(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.password = in.readString();
    }
}
