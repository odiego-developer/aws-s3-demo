package com.learning.awsimageupload.profile;


import com.learning.awsimageupload.buckets.Bucket;
import com.learning.awsimageupload.filestore.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;

    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService,
                              FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles(){
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile multipartFile) {

        isEmpty(multipartFile);

        isImage(multipartFile);

        UserProfile user = getUserProfileOrElseThrow(userProfileId);

        Map<String, String> metaData = extractMetadata(multipartFile);

        String path = constructPath(user);
        String fileName = String.format("%s-%s", UUID.randomUUID(), multipartFile.getOriginalFilename());

       try {
            fileStore.save(
                    path,
                    fileName,
                    Optional.of(metaData),
                    multipartFile.getInputStream());

            user.setUserProfileImageLink(fileName);
        } catch (IOException e) {
           throw new IllegalStateException(e);
        }
    }

    private String constructPath(UserProfile user) {
        return String.format("%s/%s", Bucket.PROFILE_IMAGE.getName(), user.getUserProfileId());
    }


    private Map<String, String> extractMetadata(MultipartFile multipartFile) {
        Map<String,String> metaData = new HashMap<>();
        metaData.put("Content-Type", multipartFile.getContentType());
        metaData.put("Content-Length",String.valueOf(multipartFile.getSize()));
        return metaData;
    }

    private UserProfile getUserProfileOrElseThrow(UUID userProfileId) {
        return userProfileDataAccessService
                .getUserProfiles()
                .stream()
                .filter(file -> userProfileId.equals(file.getUserProfileId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User not found [" + userProfileId + "]"));
    }

    private void isImage(MultipartFile multipartFile) {
        if(!Arrays.asList(
            IMAGE_JPEG.getMimeType(),
            IMAGE_GIF.getMimeType(),
            IMAGE_PNG.getMimeType(),
            IMAGE_SVG.getMimeType()
        ).contains(multipartFile.getContentType())){
            throw new IllegalStateException("File must be an image ["+ multipartFile.getContentType()+"]");
        }
    }

    private void isEmpty(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()){
            throw new IllegalStateException("Can't upload empty file ["+ multipartFile.getSize()+"]");
        }
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {

        UserProfile user = getUserProfileOrElseThrow(userProfileId);

        return user
                .getUserProfileImageLink()
                .map(key -> fileStore.download(constructPath(user), key))
                .orElse(new byte[0]);
    }
}
