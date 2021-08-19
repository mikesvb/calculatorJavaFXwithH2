package org.example.dao.interfaces;


import org.example.models.StringHistory;

import java.util.List;

public interface HistroryDAO {

    void add(StringHistory stringHistory);

    void delete(StringHistory stringHistory);
    void autoSort();

    List<StringHistory> getStringHistoryList();

    void loadData();
}
