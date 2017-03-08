package shaneelliott.sjsu.codefooapp;

/**
 * Created by selli on 3/1/2017.
 * ListObject - A parent of the thre main content listobjects: Articles, Videos, and VideoLists
 */
public class ListObject{

    /*
    * type - Used to specify which type of view layout associated with each object
     */
    private int type = 0;

    /*
    *   Getter get Type
    *   @return type
     */
    public int getType(){
        return  this.type;
    }
}

