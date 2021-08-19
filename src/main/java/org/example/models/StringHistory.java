package org.example.models;

import java.util.Date;

public class StringHistory {

    private String elementHistory;
    private Long dateadd;

    public String getElementHistory() {
        return elementHistory;
    }

    public StringHistory(String elementHistory) {
        this.elementHistory = elementHistory;

        Date date = new Date();
        long millis = date.getTime();
        this.dateadd = Long.valueOf(millis);
    }

    public StringHistory(String elementHistory, Long dateadd) {
        this.elementHistory = elementHistory;
        this.dateadd = Long.valueOf(dateadd);
    }

    public void setElementHistory(String elementHistory) {
        this.elementHistory = elementHistory;
    }

    @Override
    public String toString() {
        return  elementHistory;
    }

    public Long getDateadd(){return dateadd;}
}
