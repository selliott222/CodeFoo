package shaneelliott.sjsu.codefooapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static shaneelliott.sjsu.codefooapp.R.id.imageView;

/**
 * Created by selli on 3/1/2017.
 *
 * ContentListAdaptter - A custom ArrayList<ListObject> adapter
 */

public class ContentListAdapter extends ArrayAdapter<ListObject>{

    /*
    *  CTX - Context of the adapter
     */
    private Context CTX;
    /*
    *  objects - ListObject ArrayList associated with this adapter
     */
    private List<ListObject> objects;

    /*
    *  Default Constructor of custom ContentListAdapter
    *
    *  @param context   context of adapter
    *  @param resource  default R.id.Layout of view objects
    *  @param objects   list associated with adapter
     */
    public ContentListAdapter(Context context, int resource, List<ListObject> objects) {
        super(context, resource, objects);
        CTX = context;
        this.objects = objects;
    }

    /*
    *   getViewTypeCount - Returns number of different View Types
    *   @return            3 - Total view types
     */
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    /*
    *   getItemViewType - Returns type of object at position of objects ArrayList
    *   @param position   position of current index in objects list
    *   @return           type of object
     */
    @Override
    public int getItemViewType(int position) {
        if (objects.get(position).getType() == 1){return 1;}
        else if (objects.get(position).getType() == 2){return 2;}
        else return 0;
    }

    /*
    *   getView - Updates the ListView assosicated with the adapter. Goes through each postion in the objects array and
    *   inflates a view depending on the objects type.
    *   @param position     current position of the objects list
    *   @param convertView  used to determine if view has been creaded already. Otherwise view is recycled.
    *   @param parent       inflate view under proper ViewGroup
    *   @return
     */
    @NonNull
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /*If type is 0 then the View will display a progress bar, indicating data is being loaded
         *This view type is added to the end of the main content of the list view so that when a user scrolls down they see that loading is in progress.
        */
        if (getItemViewType(position) == 0) {
            view = inflater.inflate(R.layout.load_layout, parent, false);
        }

        //Type 1 displays an article view. This view contains a ___ minutes ago, title, and image thumbnail
        else if (getItemViewType(position) == 1) {
            view = inflater.inflate(R.layout.article_layout, parent, false);

            Article article = (Article) objects.get(position);

            TextView atitle = (TextView) view.findViewById(R.id.TVtitle);
            TextView atime = (TextView) view.findViewById(R.id.TVtime);

            atitle.setText(article.getTitle());
            atime.setText(article.getTime());

            ImageView iv = (ImageView) view.findViewById(R.id.imageView);

            //Uses Picasso Library for image loading, caching, and displaying
            Picasso.with(CTX).load(article.getImageURL()).into(iv);

            final String url =  article.getURL();

            //Creates on onclick listener for all article objects. Selecting a article opens Webview Activity of Videos URL
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(CTX, WebViewActivity.class);
                    i.putExtra("url", url);
                    CTX.startActivity(i);
                }
            });
        }

        //if type == 2 View displays a VideoList. This list is populated by multiple video objects inflated inside the VideoList layout.
        else if (getItemViewType(position) == 2) {
            view = inflater.inflate(R.layout.videos_layout, parent, false);

            final VideoList vlist = (VideoList) objects.get(position);

            HorizontalScrollView sv = (HorizontalScrollView) view.findViewById(R.id.HSVscroll);

            LinearLayout topLinearLayout = new LinearLayout(CTX);
            topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            sv.addView(topLinearLayout);

            for (int i = 0; i < vlist.getVList().length; i++){
                View v = inflater.inflate(R.layout.video_item_layout,parent,false);
                TextView vtitle = (TextView) v.findViewById(R.id.TVvtitle);
                vtitle.setText(vlist.getVList()[i].getTitle());

                ImageView iv = (ImageView) v.findViewById(R.id.IVscreen);
                //Uses Picasso Library for image loading, caching, and displaying
                Picasso.with(CTX).load(vlist.getVList()[i].getImgURL()).into(iv);

                topLinearLayout.addView(v);

                final String url =  vlist.getVList()[i].getURL();

                //Creates on onclick listener for all video objects. Selecting a video opens Webview Activity of Videos URL
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(CTX, WebViewActivity.class);
                        i.putExtra("url", url);
                        CTX.startActivity(i);
                    }
                });
            }
        }

        return view;
    }
}