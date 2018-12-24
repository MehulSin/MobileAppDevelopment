package iit.mehul.newsgateway;


import java.io.Serializable;

public class Source implements Serializable, Comparable<Source> {

    private String idData;
    private String nameVal;
    private String urlVal;
    private String categoryData;

    public Source() {
    }

    public String getIdVal() {
        return idData;
    }

    public void setIdVal(String id) {
        this.idData = id;
    }

    public String getNameVal() {
        return nameVal;
    }

    public void setNameVal(String name) {
        this.nameVal = name;
    }

    public String getUrlVal() {
        return urlVal;
    }

    public void setUrlVal(String urlData) {
        this.urlVal = urlData;
    }

    public String getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(String category) {
        this.categoryData = category;
    }

    public int compareTo(Source other) {
        return nameVal.compareTo(other.nameVal);
    }


}
