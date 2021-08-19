package org.example.dao.hibernate.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="expressions")
public class Expression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stringExpression;

    private Long dateAddString;

    public Expression() {

    }

    public Expression(String stringExpression) {
        this.stringExpression = stringExpression;

        Date date = new Date();
        long millis = date.getTime();
        this.dateAddString = Long.valueOf(millis);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStringExpression() {
        return stringExpression;
    }

    public void setStringExpression(String stringExpression) {
        this.stringExpression = stringExpression;
    }

    public Long getDateAddString() {
        return dateAddString;
    }

    public void setDateAddString(Long dateAddString) {
        this.dateAddString = dateAddString;
    }

    @Override
    public String toString() {
        return stringExpression;
    }
}
