package org.example.dao.impls;

import javafx.collections.FXCollections;
import org.example.dao.hibernate.entity.Expression;
import org.example.dao.hibernate.util.HibernateUtil;
import org.example.dao.interfaces.HistroryDAO;
import org.example.models.StringHistory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Comparator;
import java.util.List;

public class CollectionStringHistory implements HistroryDAO {

    private List<StringHistory> historyList = FXCollections.observableArrayList();
    private Session session;

    public CollectionStringHistory(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            this.session=session;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void add(StringHistory stringHistory) {
        if(historyList.size()>9) {
            historyList.remove(9);
        }
        historyList.add(stringHistory);

        try(Session session = HibernateUtil.getSessionFactory().openSession()){

            Transaction transaction = null;
            transaction=session.beginTransaction();
            session.save(new Expression(stringHistory.toString()));
            transaction.commit();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void delete(StringHistory stringHistory) {
        historyList.remove(stringHistory);
    }

    @Override
    public List<StringHistory> getStringHistoryList() {

        autoSort();

        return historyList;
    }

    @Override
    public void loadData() {

        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.getSessionFactory().openSession();

            List<Expression> stringHistories = session.createQuery("from Expression order by dateAddString desc", Expression.class).setMaxResults(10).list();
            stringHistories.forEach(s->historyList.add(new StringHistory(s.getStringExpression(),s.getDateAddString())));

            autoSort();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void autoSort(){
        historyList.sort(Comparator.comparing(StringHistory::getDateadd).reversed());
    }


}
