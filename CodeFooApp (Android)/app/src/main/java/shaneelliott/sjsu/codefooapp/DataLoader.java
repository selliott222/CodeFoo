package shaneelliott.sjsu.codefooapp;

import android.os.AsyncTask;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by selli on 3/1/2017.
 *
 * DataLoader - Used to execute asynchronous http requests to load data from IGN's webapi
 */

public class DataLoader extends AsyncTask<Object, Object, String>{

    /*
    *   activity - Used to reference MainActivity to perform UI updates on the UI's main  thread.
     */
    private MainActivity activity;
    /*
    *   json_obj_alist -
     */
    private boolean running = true;

    /*
    *   DataLoader - Constructor
    *   @param activity provides the reference to the MainActivity
     */
    public DataLoader(MainActivity activity){
        this.activity = activity;
    }

    /*
    *   doInBackground - Preforms async task on new thread. Loads the Articles and Videos from the Webapi
    *   @param arg0[] Array of arguments
    *   @return       Result String
     */
    @Override
    protected String doInBackground(Object... arg0) {

        //Get parameter arguments
        int count = (Integer) arg0[0];
        int aindex = (Integer) arg0[1];
        int vindex = (Integer) arg0[2];
        String[] tags = (String[]) arg0[3];

        int returnindexa = 0;
        int returnindexv = 0;

        URL url;
        HttpURLConnection urlConnection = null;

        JSONObject json_obj_alist;
        JSONObject json_obj_vlist;

        //Alist and Vlist will be used to return lists of new Articles and Videos to the MainActivity  ListView
        ArrayList<Article> alist = new ArrayList<Article>();
        ArrayList<Video> vlist = new ArrayList<Video>();

        String str = "";

        try {

            InputStream in;
            BufferedReader reader;
            StringBuilder stringbuilder;
            String line = "";
            JSONArray data;

            int aindexIncrease = 0;

            /*
             * While state of aysnctask is running and Articles in alist is less then the required number of articles to load,
             * continue to request new data, parse and add Articles to the content list. Each loop will load the Article data of
             *  a total of 'count' articles starting from index 'aindex' This loop will continue to load Article data
             * from the webapi until the required number of new articles (count - 4) has been loaded. Article data will be discarded
             * if it does not meet the tag requirements.
             */
            while (alist.size() < (count-2) && running) {

                aindexIncrease = 0;

                //If the index exceeds the max (300) then set index back to 0.
                if (aindex + count > 300){aindex = 0;}

                //Establish URL with correct index and count
                url = new URL("http://ign-apis.herokuapp.com/articles?startIndex=" + aindex + "&count=" + count);
                //Create URL connection
                urlConnection = (HttpURLConnection) url.openConnection();

                //Read InputStream
                in = new BufferedInputStream(urlConnection.getInputStream());
                in = new BufferedInputStream(urlConnection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(in));
                stringbuilder = new StringBuilder(in.available());

                while (running && (line = reader.readLine()) != null) {
                    stringbuilder.append(line);
                }

                //Create JSON object of article list
                json_obj_alist = new JSONObject(stringbuilder.toString());

                data = json_obj_alist.getJSONArray("data");

                for (int y = 0; y < count; y++) {
                    String title = data.getJSONObject(y).getJSONObject("metadata").getString("headline");
                    JSONArray tagsarray = data.getJSONObject(y).getJSONArray("tags");

                    //Boolean used to determine if the articles title or tag array contains any of the tags currently being evaulated for
                    boolean taghit = false;

                    //Check if the title contains any of the tag string parameters
                    for (String string : tags){
                        if (title.toLowerCase().contains(string)) {
                            taghit = true;
                        }
                    }

                    //Check if the array of tags associated with the Article at position y conatians any of the tag string parameters
                    for (int tagindex = 0; tagindex < tagsarray.length(); tagindex++){
                        for (String string : tags){
                            if (tagsarray.getString(tagindex).toLowerCase().contains(string)) {
                                taghit = true;
                            }
                        }
                    }

                    //If alist has not been filled to its required size continue to add new article
                    if (alist.size()<count-2) {
                        //Update the current article index increase
                        aindexIncrease += 1;
                        returnindexa = aindex + y + 1;

                        //If article fits the tag parameters then add article to alist
                        if (taghit) {
                            String imgurl = data.getJSONObject(y).getJSONArray("thumbnails").getJSONObject(0).getString("url");
                            String aurl = "http://ign.com/articles/";

                            //determine ___ minutes ago
                            String publishdate = data.getJSONObject(y).getJSONObject("metadata").getString("publishDate");
                            String year = publishdate.substring(0, 4);
                            String month = publishdate.substring(5, 7);
                            String day = publishdate.substring(8, 10);
                            String hour = publishdate.substring(11, 13);
                            String min = publishdate.substring(14, 16);
                            String sec = publishdate.substring(17, 19);

                            String mytime = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = df.parse(mytime);
                            String timePassedString = (String) DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                            aurl += year + "/" + month + "/" + day + "/" + data.getJSONObject(y).getJSONObject("metadata").getString("slug");

                            //Add new article to list
                            alist.add(new Article(title, imgurl, aurl, timePassedString));
                        }
                    }

                }
                aindex += aindexIncrease;
                System.out.println("Aindex: " + aindex);
                urlConnection.disconnect();
            }

            int vindexIncrease = 0;

            /*
             * While state of aysnctask is running and Videos in vlist is less then the required number of videos to load,
             * continue to request new data, parse and add Videos to the content list. Each loop will load the Video data of
             *  a total of 'count * 3' articles starting from index 'vindex' This loop will continue to load Video data
             * from the webapi until the required number of new videos ((count * 3) -6) has been loaded. Video data will be discarded
             * if it does not meet the tag requirements.
             */
            while (vlist.size() < ((count * 3) - 6) && running) {
                vindexIncrease = 0;

                //If the index exceeds the max (300) then set index back to 0.
                if (vindex + count > 300){vindex = 0;}

                //Establish a URL connection
                url = new URL("http://ign-apis.herokuapp.com/videos?startIndex=" + vindex + "&count=" + count * 3);
                urlConnection = (HttpURLConnection) url.openConnection();

                //Read InputStream
                in = new BufferedInputStream(urlConnection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(in));
                stringbuilder = new StringBuilder(in.available());
                line = "";

                while (running && (line = reader.readLine()) != null) {
                    stringbuilder.append(line);
                }

                //Create JSON object from InputStream
                json_obj_vlist = new JSONObject(stringbuilder.toString());

                data = json_obj_vlist.getJSONArray("data");

                for (int y = 0; y < (count * 3) - 6; y++) {
                    String vtitle = data.getJSONObject(y).getJSONObject("metadata").getString("name");
                    JSONArray vtagsarray = data.getJSONObject(y).getJSONArray("tags");

                    //Check if data at position y meets the tag requirements
                    boolean vtaghit = false;

                    for (String string : tags) {
                        if (vtitle.toLowerCase().contains(string)) {
                            vtaghit = true;
                        }
                    }

                    for (int tagindex = 0; tagindex < vtagsarray.length(); tagindex++) {
                        for (String string : tags) {
                            if (vtagsarray.getString(tagindex).toLowerCase().contains(string)) {
                                vtaghit = true;
                            }
                        }
                    }

                    //If vlist has not been filled to its required size continue to add new video
                    if (vlist.size() < (count*3) - 6) {
                        vindexIncrease += 1;
                        returnindexv = vindex + y + 1;

                        if (vtaghit) {
                            String vimgurl = data.getJSONObject(y).getJSONArray("thumbnails").getJSONObject(0).getString("url");
                            String vurl = data.getJSONObject(y).getJSONObject("metadata").getString("url");
                            vlist.add(new Video(vtitle, vimgurl, vurl));
                        }
                    }
                }

                vindex += vindexIncrease;
                System.out.println("Vindex: " + vindex);
                urlConnection.disconnect();
            }

            //Add all the articles and videos from alist and vlist to the MainActivity list of ListObjects
            activity.addToList((Integer) arg0[0], vlist, alist, returnindexv, returnindexa);

        } catch (IOException e) {
            e.printStackTrace();
            urlConnection.disconnect();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            //Always set the running state to false upon end of execution
            running = false;
        }

        return  "complete";
    }

    /*
    *   onCancelled - When task is cancelled running state is set to false and activity. Empty List and load new data
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        running = false;
        activity.emptyList();
        try {
            activity.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    *   onPostExecute - Updates MainActivities ListView on post execution
    *   @param s        result string
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        running = false;
        activity.updateList();
        System.out.println("POST EXECUTE");
    }

    /*
     *  Getter function for running
     *  @return if dataLoader is running or not
     */
    public boolean isRunning() {
        return running;
    }
}
