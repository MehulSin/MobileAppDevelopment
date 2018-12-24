package com.mehul.knowyourgovernment;

import java.io.Serializable;


public class Official implements Serializable {

    private String nameVal;
    private String officeVal;
    private String partyName;
    private String addressDetail;
    private String phoneNum;
    private String urlValue;
    private String emailID;
    private String photoUrlValue;
    private String googlepluslink;
    private String facebooklink;
    private String twitterlink;
    private String youtubelink;

    public Official() {
        this.nameVal = "No Data Provided";
        this.officeVal = "No Data Provided";
        this.partyName = "Unknown";
        this.addressDetail = "No Data Provided";
        this.phoneNum = "No Data Provided";
        this.urlValue = "No Data Provided";
        this.emailID = "No Data Provided";
        this.photoUrlValue = "No Data Provided";
        this.googlepluslink = "No Data Provided";
        this.facebooklink = "No Data Provided";
        this.twitterlink = "No Data Provided";
        this.youtubelink = "No Data Provided";
    }

    public Official(String nameVal, String officeVal, String partyName, String addressDetail, String phoneNum, String urlValue, String emailID, String photoUrlValue, String googlepluslink, String facebooklink, String twitterlink, String youtubelink) {
        this.nameVal = nameVal;
        this.officeVal = officeVal;
        this.partyName = partyName;
        this.addressDetail = addressDetail;
        this.phoneNum = phoneNum;
        this.urlValue = urlValue;
        this.emailID = emailID;
        this.photoUrlValue = photoUrlValue;
        this.googlepluslink = googlepluslink;
        this.facebooklink = facebooklink;
        this.twitterlink = twitterlink;
        this.youtubelink = youtubelink;
    }

    public String getName()
    {
        return nameVal;
    }

    public void setName(String nameVal)
    {
        this.nameVal = nameVal;
    }

    public String getOffice()
    {
        return officeVal;
    }

    public void setOffice(String officeVal)
    {
        this.officeVal = officeVal;
    }

    public String getParty()
    {
        return partyName;
    }

    public void setParty(String partyName)
    {
        this.partyName = partyName;
    }

    public String getAddress()
    {
        return addressDetail;
    }

    public void setAddress(String addressDetail)
    {
        this.addressDetail = addressDetail;
    }

    public String getPhone()
    {
        return phoneNum;
    }

    public void setPhone(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    public String getUrl()
    {
        return urlValue;
    }

    public void setUrl(String urlValue)
    {
        this.urlValue = urlValue;
    }

    public String getEmail()
    {
        return emailID;
    }

    public void setEmail(String emailID)
    {
        this.emailID = emailID;
    }

    public String getPhotoUrl()
    {
        return photoUrlValue;
    }

    public void setPhotoUrl(String photoUrlVal)
    {
        this.photoUrlValue = photoUrlVal;
    }

    public String getGoogleplus()
    {
        return googlepluslink;
    }

    public void setGoogleplus(String googlepluslink)
    {
        this.googlepluslink = googlepluslink;
    }

    public String getTwitter()
    {
        return twitterlink;
    }

    public void setTwitter(String twitterlink)
    {
        this.twitterlink = twitterlink;
    }

    public String getFacebook()
    {
        return facebooklink;
    }

    public void setFacebook(String facebooklink)
    {
        this.facebooklink = facebooklink;
    }



    public String getYoutube()
    {
        return youtubelink;
    }

    public void setYoutube(String youtubelink)
    {
        this.youtubelink = youtubelink;
    }

    @Override
    public String toString() {
        return "Official{" +
                "name='" + nameVal + '\'' +
                ", office='" + officeVal + '\'' +
                ", party='" + partyName + '\'' +
                ", address='" + addressDetail + '\'' +
                ", phone='" + phoneNum + '\'' +
                ", url='" + urlValue + '\'' +
                ", email='" + emailID + '\'' +
                ", photoUrl='" + photoUrlValue + '\'' +
                ", googleplus='" + googlepluslink + '\'' +
                ", facebook='" + facebooklink + '\'' +
                ", twitter='" + twitterlink + '\'' +
                ", youtube='" + youtubelink + '\'' +
                '}';
    }
}
