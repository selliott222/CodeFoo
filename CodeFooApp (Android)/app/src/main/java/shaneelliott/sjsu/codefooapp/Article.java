package shaneelliott.sjsu.codefooapp;

/**
 * Created by selli on 3/1/2017.
 *
 * The Article class is used to represent article data
 */

public class Article extends ListObject {

    /*
    * type - Used to specify which type of view layout assosicated with each object
     */
    private int type;

    /*
    * title - Title of Article
    * img_url - url of article image
    * url - Url of the articles location
    * minago - ___ minutes ago since articles creation
     */
    private String title, img_url, url, min_ago;

    /*
    *   Article Constructor
    *   @param title, imgurl, url, minago
     */
    public Article(String title, String img_url, String url, String min_ago){
        this.title = title;
        this.img_url = img_url;
        this.url = url;
        this.min_ago = min_ago;
        this.type = 1;
    }

    /*
    * Getter Functions
     */
    public int getType(){
        return  this.type;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return min_ago;
    }

    public String getImageURL() {
        return img_url;
    }

    public String getURL() {
        return url;
    }
}
