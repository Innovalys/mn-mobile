package navi.mnmobile.navi.mnmobile.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import navi.mnmobile.R;
import navi.mnmobile.navi.mnmobile.businesslayer.Manga;
import navi.mnmobile.navi.mnmobile.utils.DownloadImageTask;

/**
 * Created by Vuzi on 18/07/2015.
 */
public class MangaAdapter extends BaseAdapter {

    private final List<Manga> mangaList;
    private final HashMap<String, Bitmap> mangaCovers;
    private final LayoutInflater inflater;

    public MangaAdapter(List<Manga> mangaList, LayoutInflater inflater) {
        this.mangaList = mangaList;
        this.inflater = inflater;
        this.mangaCovers = new HashMap<>();
    }

    @Override
    public int getCount() {
        return mangaList.size();
    }

    @Override
    public Object getItem(int position) {
        return mangaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_manga, null);

        final Manga m = mangaList.get(position);
        final ImageView mangaCover = (ImageView) convertView.findViewById(R.id.img_manga_cover);

        if(m.getUrl() != null)
            if(mangaCovers.containsKey(m.getUrl())) {
                Bitmap image = mangaCovers.get(m.getUrl());

                if(image != null)
                    mangaCover.setImageBitmap(image);
            } else {
                new DownloadImageTask(mangaCover) {
                    @Override
                    protected void onPostExecute(Bitmap result) {
                        mangaCovers.put(m.getUrl(), result);

                        if(result != null)
                            mangaCover.setImageBitmap(result);
                    }
                }.execute(m.getUrl());
            }

        ((TextView)convertView.findViewById(R.id.tv_manga_title)).setText(m.getName());
        ((TextView)convertView.findViewById(R.id.tv_manga_date)).setText(/*m.getDate().toString()*/ "TODO");

        return convertView;
    }

}