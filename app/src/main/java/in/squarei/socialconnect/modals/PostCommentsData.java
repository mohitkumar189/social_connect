package in.squarei.socialconnect.modals;

/**
 * Created by mohit kumar on 5/9/2017.
 */

public class PostCommentsData {
    private String postId,postComment,commentOn,commentBy,profilePic,commentByName;

    public PostCommentsData(String postId, String postComment, String commentOn, String commentBy, String profilePic, String commentByName) {
        this.postId = postId;
        this.postComment = postComment;
        this.commentOn = commentOn;
        this.commentBy = commentBy;
        this.profilePic = profilePic;
        this.commentByName = commentByName;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostComment() {
        return postComment;
    }

    public void setPostComment(String postComment) {
        this.postComment = postComment;
    }

    public String getCommentOn() {
        return commentOn;
    }

    public void setCommentOn(String commentOn) {
        this.commentOn = commentOn;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCommentByName() {
        return commentByName;
    }

    public void setCommentByName(String commentByName) {
        this.commentByName = commentByName;
    }
}