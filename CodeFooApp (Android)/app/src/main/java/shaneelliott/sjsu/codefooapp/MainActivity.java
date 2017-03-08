package shaneelliott.sjsu.codefooapp;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
*   MainActivity
 */
public class MainActivity extends AppCompatActivity {

    /*
    *   list - List of ListObjects to be displayed in main activity layout content ListView
     */
    private List<ListObject> list = new ArrayList<ListObject>();
    /*
    *   adapter - custom ListAdapter for list
     */
    private ContentListAdapter adapter;
    /*
    *   aindex, vindex - Indexes of both Video objects and Article objects used to search IGN's Webapi
     */
    private int aindex, vindex;
    /*
     *  t1,t2,t3,t4,t5,t6,t7 - Text views of all tabs located at the top of the activity
     */
    private TextView t1,t2,t3,t4,t5,t6,t7;
    /*
    *   tags - Array of tags to query for when loading data
     */
    private String[] tags = {""};
    /*
     *  dataloader - this dataloader object is used to load data. Important to keep track of dataloaders running state.
     */
    private DataLoader dataLoader = new DataLoader(this);

    /*
     * On Create Method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create an adapter for the MainActivity list of ListObjects containing Articles, Videos, and ListVideos
        adapter = new ContentListAdapter(this, R.layout.article_layout, list);
        ListView contentLV = (ListView) findViewById(R.id.LVcontent);
        contentLV.setAdapter(adapter);

        t1 = (TextView) findViewById(R.id.TV1);
        t2 = (TextView) findViewById(R.id.TV2);
        t3 = (TextView) findViewById(R.id.TV3);
        t4 = (TextView) findViewById(R.id.TV4);
        t5 = (TextView) findViewById(R.id.TV5);
        t6 = (TextView) findViewById(R.id.TV6);
        t7 = (TextView) findViewById(R.id.TV7);

        //Set textview tabs to the correct colors
        t1.setTextColor(Color.WHITE);
        t2.setTextColor(Color.GRAY);
        t3.setTextColor(Color.GRAY);
        t4.setTextColor(Color.GRAY);
        t5.setTextColor(Color.GRAY);
        t6.setTextColor(Color.GRAY);
        t7.setTextColor(Color.GRAY);

        //Creates a ListObject that will display a progress bar
        list.add(new ListObject());

        //Set DataLoader initial running state to false
        dataLoader.cancel(true);

        //Create onScroolListener - This will prompt the application to load new Articles and Videos upon scrolling to the bottom of the LVcontent ListView
        contentLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {

                    //If dataLoader running state is false then execute new loadData asynctask
                    if(!dataLoader.isRunning())
                    {
                        try {
                            loadData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /*
     * emptyList - can emppty the list from outside the MainActivity
     */
    public void emptyList(){
        aindex = 0;
        vindex = 0;
        list.clear();
        //Add progress bar indicator to the end of the list
        list.add(new ListObject());
    }

    /*
     * onClickTagFilter - Operates when clicking tab button
     * @param v tab view that has been clicked
     */
    public void onClickTagFilter(View v) throws IOException {

        //Set index's to 0
        aindex = 0;
        vindex = 0;
        //Clear the current list and add a progressbar to the bottom ie "list.add(new ListObject());" Then notify ListView to update itself
        list.clear();
        list.add(new ListObject());
        adapter.notifyDataSetChanged();

        //Set dataLoader to running = false
        dataLoader.cancel(true);

        t1.setTextColor(Color.GRAY);
        t2.setTextColor(Color.GRAY);
        t3.setTextColor(Color.GRAY);
        t4.setTextColor(Color.GRAY);
        t5.setTextColor(Color.GRAY);
        t6.setTextColor(Color.GRAY);
        t7.setTextColor(Color.GRAY);

        //Updates the tag parameters according to the tab button that has been pressed
        if (v.getId() == R.id.TV1){tags = new String[]{""};}
        if (v.getId() == R.id.TV2){tags = new String[]{"playstation", "ps3", "ps4", "sony", "kojima", "zero dawn"};}
        if (v.getId() == R.id.TV3){tags = new String[]{"xbox", "microsoft", "halo", "gears", "scorpio", "call of duty", "minecraft"};}
        if (v.getId() == R.id.TV4){tags = new String[]{"nintendo", "pokemon", "wii", "switch", "mario", "zelda", "3ds"};}
        if (v.getId() == R.id.TV5){tags = new String[]{"pc","nvidia","windows","mac","linux","dell", "counter-strike"};}
        if (v.getId() == R.id.TV6){tags = new String[]{"review"};}
        if (v.getId() == R.id.TV7){tags = new String[]{"tv", "movie"};}

        //Animate the button that has been pressed
        pressAnimate(v);
    }

    /*
     * pressAnimate - Animate Tab button when pressed
     * @param v view that has been pressed to be animated
     */
    public void pressAnimate(View v){
        int colorStart = Color.GRAY;
        int colorEnd = Color.WHITE;

        ValueAnimator colorAnimate = ObjectAnimator.ofInt(v, "textColor", colorStart, colorEnd);

        colorAnimate.setDuration(200);
        colorAnimate.setEvaluator(new ArgbEvaluator());
        colorAnimate.start();
    }

    /*
     * loadData - execute new DataLoader
     */
    public void loadData() throws IOException {
        dataLoader = (DataLoader) new DataLoader(this).execute(6, aindex, vindex, tags);
    }

    /*
     * updaeList - updates the listadapter
     */
    public void updateList() {
        adapter.notifyDataSetChanged();
    }

    /*
     * addToList - Adds new articles and videos to the ListView based on the pattern of 2 Articles then 1 VideoList containing 6 Videos
     * @param count number of new entries to ListView
     * @param vlist List of new videos to add to ListView
     * @param alist List of new Article to add to ListView
     * @param vindexincrease The newest index when loading new video data
     * @param aindexincrease The newest index when loading new article data
     */
    public void addToList(Integer count, ArrayList<Video> vlist, ArrayList<Article> alist, int vindexincrease, int aindexincrease) {

        int i = 0;

            list.remove(list.size() - 1);

            if (aindex == 0) {
                list.clear();
            }

            while (i < count) {

                if ((i + 1) % 3 != 0) {
                    if (alist.size() > 0) {
                        list.add(alist.remove(0));
                    }
                } else {
                    list.add(new VideoList(new Video[]{vlist.remove(0), vlist.remove(0), vlist.remove(0), vlist.remove(0), vlist.remove(0), vlist.remove(0)}));
                }

                i++;

            }

            //Add progress bar indicator to the end of the list
            list.add(new ListObject());

            //Update the index's to their new positions
            this.aindex = aindexincrease;
            this.vindex = vindexincrease;
    }
}
