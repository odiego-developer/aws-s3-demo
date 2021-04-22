package com.learning.awsimageupload.buckets;

public enum Bucket {

    PROFILE_IMAGE("learning-aws-image-upload");

    private final String name;

    Bucket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
