package shaneelliott.sjsu.codefooapp;

/**
 * Created by selli on 3/1/2017.
 *  Video - ListObject
 */

public class Video extends ListObject{

    /*
     * title, imgURL, url - Strings of Videos data
     */
    private String title, imgURL, url;

    /*
     * Video Constructor
     * @param title       title of Video
     * @param img_url     url of Videos Thumbnail
     * @param url         url of video location
     */
    public Video(String title, String img_url, String url){
        this.title = title;
        this.imgURL = img_url;
        this.url = url;
    }

    /*
     * Getter Methods
     */
    public String getTitle(){
        return title;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getURL() {
        return url;
    }
}
