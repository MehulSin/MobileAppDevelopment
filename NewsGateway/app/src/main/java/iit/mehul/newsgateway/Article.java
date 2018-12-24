package iit.mehul.newsgateway;

import java.io.Serializable;

public class Article implements Serializable{
    private String authorName;
    private String titleVal;
    private String newsDescData;
    private String apiAddressDetail;
    private String pictureData;
    private String dateTimeValue;


    public Article() {
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitleName() {
        return titleVal;
    }

    public void setTitleName(String titleVal) {
        this.titleVal = titleVal;
    }

    public String getNewsDescData() {
        return newsDescData;
    }

    public void setNewsDescData(String newsDescData) {
        this.newsDescData = newsDescData;
    }

    public String getApiAddressDetail() {
        return apiAddressDetail;
    }

    public void setApiAddressDetail(String apiAddressDetail) {
        this.apiAddressDetail = apiAddressDetail;
    }

    public String getUrlImage() {
        return pictureData;
    }

    public void setUrlImage(String urlimage) {
        this.pictureData = urlimage;
    }

    public String getDateTimeValue() {
        return dateTimeValue;
    }

    public void setDateTimeValue(String dateTimeValue) {
        this.dateTimeValue = dateTimeValue;
    }
}
