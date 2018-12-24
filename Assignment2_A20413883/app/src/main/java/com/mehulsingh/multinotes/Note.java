package com.mehulsingh.multinotes;

public class Note {

    private String nTitle;
    private String nText;
    private String noteSavedDate;


    public Note(String nTitle, String nText, String noteSavedDate) {
        this.nTitle = nTitle;
        this.nText = nText;
        this.noteSavedDate = noteSavedDate;
    }

    public String getTitle() { return this.nTitle;}
    public String getNoteContent() { return this.nText;}
    public String getNoteSavedDate() { return this.noteSavedDate;}

    public void setTitle(String newTitle){this.nTitle = newTitle;}
    public void setNoteContext(String newText){this.nText = newText;}
    public void setNoteSavedDate(String newLastSavedDate){this.noteSavedDate = newLastSavedDate;}

    public String toSaveFormat() {
        return "   Note started \n" + this.nTitle + " \n" + this.noteSavedDate + "\n" +this.nText + " note ended \n";
    }

    @Override
    public String toString() {
        return "Title of note : " + this.nTitle + " \n" + "Date when note was saved :" + this.noteSavedDate + "\n" +this.nText ;
    }

}
