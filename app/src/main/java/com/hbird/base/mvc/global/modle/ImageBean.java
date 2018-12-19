package com.hbird.base.mvc.global.modle;

/**
 * Created by Liul on 2018-07-05.
 */
public class ImageBean {

    private String ImagePath;

    public ImageBean(String imagePath) {

        this.ImagePath=imagePath;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
