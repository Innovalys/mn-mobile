package navi.mnmobile.navi.mnmobile.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import navi.mnmobile.R;
import navi.mnmobile.navi.mnmobile.businesslayer.Chapter;
import navi.mnmobile.navi.mnmobile.businesslayer.Manga;
import navi.mnmobile.navi.mnmobile.businesslayer.Page;
import navi.mnmobile.navi.mnmobile.utils.DownloadImageTask;

/**
 * Created by Vuzi on 18/07/2015.
 */
public class ChapterAdapter extends BaseAdapter {

    private final Chapter chapter;
    private final HashMap<String, Bitmap> mangaCovers;
    private final LayoutInflater inflater;

    public ChapterAdapter(Chapter chapter, LayoutInflater inflater) {
        this.chapter = chapter;
        this.inflater = inflater;
        this.mangaCovers = new HashMap<>();
    }

    @Override
    public int getCount() {
        return chapter.getPages().size();
    }

    @Override
    public Object getItem(int position) {
        return chapter.getPages().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_page, null);

        final Page p = chapter.getPages().get(position);
        final ImageView mangaCover = (ImageView) convertView.findViewById(R.id.img_manga_page);

        if(p.getUrl() != null) {
            if (mangaCovers.containsKey(p.getUrl())) {
                Bitmap image = mangaCovers.get(p.getUrl());

                if (image != null)
                    mangaCover.setImageBitmap(image);
            } else {
                new DownloadImageTask(mangaCover) {
                    @Override
                    protected void onPostExecute(Bitmap result) {
                        mangaCovers.put(p.getUrl(), result);

                        if (result != null)
                            mangaCover.setImageBitmap(result);
                    }
                }.execute(p.getUrl());
            }

            mangaCover.setOnGenericMotionListener(new View.OnGenericMotionListener() {
                public float mLastTouchY;
                public float mLastTouchX;

                @Override
                public boolean onGenericMotion(View v, MotionEvent ev) {
                    final int action = ev.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN: {
                            final float x = ev.getX();
                            final float y = ev.getY();

                            // Remember where we started
                            mLastTouchX = x;
                            mLastTouchY = y;
                            break;
                        }

                        case MotionEvent.ACTION_MOVE: {
                            final float x = ev.getX();
                            final float y = ev.getY();

                            // Calculate the distance moved
                            final float dx = x - mLastTouchX;
                            final float dy = y - mLastTouchY;

                            // Move the object
                            mangaCover.setScaleX(1.1f);//mangaCover.getWidth() + (int)dx);

                           // mPosX += dx;
                           // mPosY += dy;

                            // Remember this touch position for the next move event
                            mLastTouchX = x;
                            mLastTouchY = y;

                            // Invalidate to request a redraw
                            mangaCover.invalidate();
                            break;
                        }

                    }
                    return true;
                }
            });
        }

        return convertView;
    }

}