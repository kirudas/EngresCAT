package net.bosccoma.info.engrescat;

import android.media.Image;

/**
 * Created by JordiC on 27/04/2018.
 */

public class DetallEvent {
    private String Name, ImageURL;

    public DetallEvent(String name, String imageURL){
        Name = name;
        ImageURL = imageURL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
