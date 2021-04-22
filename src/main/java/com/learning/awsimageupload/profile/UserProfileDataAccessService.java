package com.learning.awsimageupload.profile;

import com.learning.awsimageupload.datastore.FakeUserProfileDateStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileDataAccessService {

    private final FakeUserProfileDateStore fakeUserProfileDateStore;

    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDateStore fakeUserProfileDateStore) {
        this.fakeUserProfileDateStore = fakeUserProfileDateStore;
    }

    List<UserProfile> getUserProfiles(){
        return fakeUserProfileDateStore.getUserProfiles();
    }
}
