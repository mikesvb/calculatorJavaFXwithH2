package org.example.controllers;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.example.Main;
import org.example.dao.hibernate.entity.Expression;
import org.example.dao.hibernate.util.HibernateUtil;
import org.example.dao.impls.CollectionStringHistory;
import org.example.dao.interfaces.HistroryDAO;
import org.example.models.StringHistory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Arrays;
import java.util.List;


public class MainController {

    private HistroryDAO collectionStringHistory = new CollectionStringHistory();

    @FXML
    private MenuItem menuItemHistory;

    @FXML
    private VBox listData;

    @FXML
    TextArea textMonitor;

    @FXML
    TextField resultMonitor;

    @FXML
    ListView<StringHistory> listHistory;

    Boolean flagHistory = false;
    Boolean flagOperation = false;
    Boolean flagFirstOperation = true;
    Boolean flagSearchOperation = true;
    Boolean flagFinishOperation = false;
    Boolean flagEqualsAfterSendOperation = false;

    String actionOfOperation;
    String preOperation;
    Double result = 0D;
    Double preResult= 0D;
    StringBuilder stringInputNumber = new StringBuilder();
    Integer cursor = 0;
    private String findOfOperation;

    String[] elementAction = {"*", "/", "-", "+", "="};

    public static void closePane() {

        //удаляем лишние записи и оставляем 10

        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.getSessionFactory().openSession();

            List<Expression> stringHistories = session.createQuery("from Expression order by dateAddString desc", Expression.class).list();

            if(stringHistories.size()>10) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    for (int i = 10; i < stringHistories.size(); i++) {
                        session.delete(stringHistories.get(i));

                    }
                    tx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }



        HibernateUtil.getSessionFactory().close();
    }

    @FXML
    public void initialize() {


        collectionStringHistory.loadData();
        listHistory.setItems((ObservableList<StringHistory>) collectionStringHistory.getStringHistoryList());

        listHistory.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if(click.getClickCount()==2){
                    StringHistory currentItemSelected = listHistory.getSelectionModel()
                            .getSelectedItem();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Информация о записи");
                    alert.setHeaderText(null);
                    alert.setContentText(currentItemSelected.toString());

                    alert.showAndWait();


                }
            }
        });

        listData.setVisible(false);
        listData.setManaged(false);
        resultMonitor.setText(result.toString());
    }

    public void showHistoryAction() {
            flagHistory=!flagHistory;
            Main.getPrimaryStage().setWidth(flagHistory?500:300);
            listData.setVisible(flagHistory);
            listData.setManaged(flagHistory);

        if (flagHistory) {
            menuItemHistory.setText("✓ " + menuItemHistory.getText());
        } else {
            menuItemHistory.setText(menuItemHistory.getText().replace("✓ ", ""));
        }
    }

    public void onMouseClick(MouseEvent mouseEvent){
        Button button = (Button) mouseEvent.getSource();
        String textFromButton = button.getText();

        switch (textFromButton){
            case"1":
            case"2":
            case"3":
            case"4":
            case"5":
            case"6":
            case"7":
            case"8":
            case"9":
            case"0":
            case".":
                insertNumber(textFromButton);
                break;
            case"←":
                backspaceAction();
                break;
            case"√":
                sqrtAction(textFromButton);
                break;
            case"+":
            case"-":
            case"*":
            case"/":
            case"x^":
                insertAction(textFromButton);
                break;
            case"=":
                resultAction(textFromButton);
                break;
            case"%":
                percentAction(textFromButton);
                break;
            case"C":
                clearAllAction();
                break;

        }

    }


    public void insertNumber(String number){
        if (flagFinishOperation){
            stopExecution();
        }

        if(flagFirstOperation==false ) {
            if(flagSearchOperation==true) {
                flagSearchOperation = false;
                findOfOperation = stringInputNumber.substring(stringInputNumber.length() - 1, stringInputNumber.length());

                switch (findOfOperation) {
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                    case "%":
                        actionOfOperation = findOfOperation;
                    break;
                    case "^":
                        actionOfOperation = "x^";
                    break;
                }
            }
        }

        textMonitor.setText(textMonitor.getText() + number);
        stringInputNumber.append(number);
        flagOperation=false;

    }

    private void sqrtAction(String action) {
        insertAction(action);
        collectionStringHistory.autoSort();
        flagFinishOperation=true;
    }

    private void resultAction(String action) {
        insertAction(action);
        collectionStringHistory.autoSort();
        flagFinishOperation=true;
    }

    private void percentAction(String action) {
        insertAction(action);
        collectionStringHistory.autoSort();
        flagFinishOperation=true;
    }


    private void stopExecution() {
        clearAllAction();
    }


    private void clearAllAction() {
        cursor=0;
        actionOfOperation="";
        preOperation="";
        result=0D;
        preResult= 0D;

        flagHistory = false;
        flagOperation = false;
        flagFirstOperation = true;
        flagSearchOperation = true;
        flagFinishOperation = false;
        flagEqualsAfterSendOperation = false;

        textMonitor.setText("");
        resultMonitor.setText(result.toString());
        stringInputNumber.setLength(0);
        resultMonitor.setText(result.toString());

    }


    private void backspaceAction() {

        Integer count = textMonitor.getText().length();

        if(count>0){

            String backspaceElement = textMonitor.getText().substring(count-1, count);

            if (Arrays.asList(elementAction).contains(backspaceElement)) {
                return;
            }

            textMonitor.setText(textMonitor.getText().substring(0,count-1));

            stringInputNumber.delete(stringInputNumber.length()-1,stringInputNumber.length());
        }


    }

    private void insertAction(String action) {
        if (flagFinishOperation)return;

        if (stringInputNumber.length()==0)return;

        flagSearchOperation=true;

        if(flagOperation==false) {
            Integer newcursor = stringInputNumber.length();

            if (newcursor != cursor) {

                preResult = Double.parseDouble(stringInputNumber.substring(cursor, newcursor));
                cursor = newcursor+action.length();
            }
            flagOperation=true;


            if(action.equals("√")){
                actionOfOperation="√";
                flagFirstOperation=false;
                stringInputNumber.insert(0, action);
                textMonitor.setText(action + textMonitor.getText());
            }else{
                if(flagEqualsAfterSendOperation==false) {
                    stringInputNumber.append(action);
                    textMonitor.setText(textMonitor.getText() + action);
                }

            }

            if(flagFirstOperation ){
                flagFirstOperation=false;
                result=preResult;

            }else{

                if(flagEqualsAfterSendOperation==false) {
                    switch (actionOfOperation) {
                        case "+":
                            if (action.equals("%")) {
                                result = result + (result / 100) * preResult;
                                textMonitor.setText(textMonitor.getText() + "=");
                                stringInputNumber.append("=");
                            } else {
                                result = result + preResult;
                            }
                            break;
                        case "-":
                            if (action.equals("%")) {
                                result = result - (result / 100) * preResult;
                                textMonitor.setText(textMonitor.getText() + "=");
                                stringInputNumber.append("=");
                            } else {
                                result = result - preResult;
                            }
                            break;
                        case "*":
                            if (action.equals("%")) {
                                result = result * (result / 100) * preResult;
                                textMonitor.setText(textMonitor.getText() + "=");
                                stringInputNumber.append("=");
                            } else {
                                result = result * preResult;
                            }
                            break;
                        case "/":
                            if (action.equals("%")) {
                                result = result / (result / 100) * preResult;
                                textMonitor.setText(textMonitor.getText() + "=");
                                stringInputNumber.append("=");
                            } else {
                                result = result / preResult;
                            }
                            break;
                        case "x^":
                            result = Math.pow(result, preResult);
                            break;
                        case "√":
                            result = Math.sqrt(preResult);
                            textMonitor.setText(textMonitor.getText() + "=");
                            stringInputNumber.append("=");
                            break;

                    }
                }else{

                }

                if (action.equals("=")|| action.equals("√") || action.equals("%")) {
                    textMonitor.setText(textMonitor.getText() + result.toString());

                }
                resultMonitor.setText(result.toString());


                if (action.equals("=") || action.equals("√") || action.equals("%")){
                    stringInputNumber.append(result.toString());
                    collectionStringHistory.add(new StringHistory(stringInputNumber.toString()));
                }


            }

        }else{

            Integer count = textMonitor.getText().length();
            textMonitor.setText(textMonitor.getText().substring(0,count-preOperation.length()) + action);
            stringInputNumber.delete(stringInputNumber.length()-preOperation.length(),stringInputNumber.length());
            stringInputNumber.append(action);
            cursor=cursor-preOperation.length()+action.length();

            if (action.equals("=") || action.equals("√")){
                flagOperation=false;
                if (action.equals("="))flagEqualsAfterSendOperation=true;

                if(action.equals("√")){
                    textMonitor.setText(textMonitor.getText().substring(0,count-preOperation.length()));
                    stringInputNumber.delete(stringInputNumber.length()-preOperation.length(),stringInputNumber.length());
                    cursor=cursor-preOperation.length();
                }

                insertAction(action);
            }
        }
        preOperation=action;
    }

    public void alertAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText(null);
        alert.setContentText("Тестовое задание");

        alert.showAndWait();
    }

}
