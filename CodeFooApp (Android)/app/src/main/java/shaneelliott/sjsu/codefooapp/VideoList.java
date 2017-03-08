package shaneelliott.sjsu.codefooapp;

/**
 * Created by selli on 3/1/2017.
 */

public class VideoList extends ListObject {

    /*
    * type - Used to specify which type of view layout assosicated with each object
     */
    private int type;

    /*
     * video_array - An array of the Video objects inside this VideoList
     */
    private Video[] video_array;

    /*
     * VideoList Constructor
     * @param v_a Video array
     */
    public VideoList(Video[] v_a){
        this.type = 2;
        this.video_array = v_a;
    }

    /*
     * Getter functions
     */
    public int getType(){
        return  this.type;
    }

    public Video[] getVList(){
        return video_array;
    }
}