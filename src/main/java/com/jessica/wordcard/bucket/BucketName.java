package com.jessica.wordcard.bucket;

public enum BucketName {
    CARD_IMAGE("jessica-springboot-wordcard");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
