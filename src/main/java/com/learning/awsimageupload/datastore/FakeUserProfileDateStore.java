package com.learning.awsimageupload.datastore;

import com.learning.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDateStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("6981bd44-fd37-455c-935f-f723919f2fc2"),"janetjones",null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("cbb0d8e8-4dca-4285-94de-dd1f52c8bacf"),"antoniojunior",null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}
