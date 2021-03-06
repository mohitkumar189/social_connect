package in.squarei.socialconnect.modals;

/**
 * Created by mohit kumar on 5/5/2017.
 */

public class UserFeedsData {
    private String postSenderName, postTitleComment, userLikes, userComments, usershares, postImageUrl, postId;
    private int isLiked;

    public UserFeedsData(String postSenderName, String postTitleComment, String userLikes, String userComments, String postImageUrl, String postId) {
        this.postSenderName = postSenderName;
        this.postTitleComment = postTitleComment;
        this.userLikes = userLikes;
        this.userComments = userComments;
        this.postImageUrl = postImageUrl;
        this.postId=postId;

    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostSenderName() {
        return postSenderName;
    }

    public void setPostSenderName(String postSenderName) {
        this.postSenderName = postSenderName;
    }

    public String getPostTitleComment() {
        return postTitleComment;
    }

    public void setPostTitleComment(String postTitleComment) {
        this.postTitleComment = postTitleComment;
    }

    public String getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(String userLikes) {
        this.userLikes = userLikes;
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }

    public String getUsershares() {
        return usershares;
    }

    public void setUsershares(String usershares) {
        this.usershares = usershares;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }
}
