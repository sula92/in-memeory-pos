package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.Db;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entity.*;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.ItemTM;
import util.PlaceOrderTM;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class PlaceOrderViewController implements Initializable {

    public TableView<PlaceOrderTM> tblOrder;
    public TableColumn colCode;
    public TableColumn colDes;
    public TableColumn colQty;
    public TableColumn colUprice;
    public TableColumn colTot;
    public TableColumn colDel;
    public Button btnReset;
    @FXML
    private AnchorPane root;

    @FXML
    private JFXComboBox<String> cmbCusId;

    @FXML
    private JFXComboBox<String> cmbItemCode;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtCusNm;

    @FXML
    private JFXTextField txtItemDes;

    @FXML
    private JFXTextField txtUprice;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private Button btnAdd;

    @FXML
    private Label labOrderId;

    @FXML
    private Label labDate;

    @FXML
    private Label labTot;

    @FXML
    private Label labTotal;

    @FXML
    private Button btnOrder;

    @FXML
    private FontAwesomeIconView iconHome;

    private ArrayList<Item> tempItems = new ArrayList<>(Db.items);

    private ArrayList<Customer> customers=new ArrayList<>(Db.customers);

    Button button;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LocalDate today = LocalDate.now();
        labDate.setText(today.toString());
        txtCusNm.setEditable(false);
        txtItemDes.setEditable(false);

        labOrderId.setText(setOrderId());

        tblOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("des"));
        tblOrder.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrder.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("uprice"));
        tblOrder.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("tot"));
        tblOrder.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("button"));

        //colDes.setVisible(false);


        loadCustomerIDs();
        loadItemCodes();

        cmbCusId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            txtCusNm.setText(getCustomerName(newValue));
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Item item = getItems(newValue);
            txtItemDes.setText(item.getDescription());
            txtQtyOnHand.setText(item.getQty());
            txtUprice.setText(item.getUprice());
            btnAdd.setText("+ADD");
        });

        tblOrder.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnAdd.setText("UPDATE");
            txtItemDes.setText(newValue.getDes());
            txtQty.setText(newValue.getQty());
            cmbItemCode.setValue(newValue.getCode());
            for (Item item : tempItems) {
                if (newValue.getCode().equalsIgnoreCase(item.getItemCode())) {
                    int qty = Integer.parseInt(item.getQty());
                    System.out.println(qty);
                    int orderqty = Integer.parseInt(newValue.getQty());
                    btnAdd.setText("UPDATE");
                    txtQtyOnHand.setText(String.valueOf(qty + orderqty));
                }
            }
        });


    }

    @FXML
    void btnAddOnAction(ActionEvent event) {

        cmbCusId.setDisable(true);

        if (cmbCusId.getSelectionModel().getSelectedIndex() == -1) {
            new Alert(Alert.AlertType.ERROR, "You need to select a customer", ButtonType.OK).show();
            cmbCusId.setDisable(false);
            cmbCusId.requestFocus();
            return;
        }

        if (cmbItemCode.getSelectionModel().getSelectedIndex() == -1) {
            new Alert(Alert.AlertType.ERROR, "Please Select an Item", ButtonType.OK).show();
            cmbItemCode.requestFocus();
            return;
        }

        if(txtQty.getText().equalsIgnoreCase("")){
            new Alert(Alert.AlertType.ERROR,"Please Put Some Quantity!").show();
            return;
        }


        int qty = Integer.parseInt(txtQty.getText());

        if(qty<=0) {
            new Alert(Alert.AlertType.ERROR, "Please Put a Valid Quantity!").show();
            return;
        }

        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        double unitPrice = Double.parseDouble(txtUprice.getText());
        String selectedItemCode = cmbItemCode.getSelectionModel().getSelectedItem();
        ObservableList<PlaceOrderTM> placeOrderTMS=tblOrder.getItems();
        PlaceOrderTM placeOrderTM=new PlaceOrderTM();
        button=new Button("DELETE");
        button.setStyle("-fx-background-color: red");

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (PlaceOrderTM placeOrderTM:tblOrder.getItems()) {
                    setBtnDel(placeOrderTM);
                }
                txtItemDes.setText("");
                txtQty.setText("");
                txtQtyOnHand.setText("");
                txtUprice.setText("");
                btnAdd.setText("+ADD");
            }
        });

        PlaceOrderTM selectedOrder=tblOrder.getSelectionModel().getSelectedItem();
        boolean present=false;

        if (qty <= 0 || qty > qtyOnHand) {
            new Alert(Alert.AlertType.ERROR, "Invalid Qty", ButtonType.OK).show();
            txtQty.requestFocus();
            txtQty.selectAll();

            setTotal(placeOrderTMS);

            return;
        }

        if(btnAdd.getText().equalsIgnoreCase("+add")){
            if(!placeOrderTMS.isEmpty()) {
                for (PlaceOrderTM tm : tblOrder.getItems()) {
                    System.out.println("fuck3"+tm);
                    System.out.println(cmbItemCode.getValue());
                    if (tm.getCode().equalsIgnoreCase(cmbItemCode.getValue())) {
                        System.out.println(tm);
                        int oldqty=Integer.valueOf(tm.getQty());
                        int newQty = qty + oldqty;
                        tm.setQty(String.valueOf(newQty));
                        tm.setTot(String.valueOf(newQty * unitPrice));
                        tblOrder.refresh();
                        //cmbItemCode.getSelectionModel().clearSelection();

                        updateQtyOnHand(tm.getCode(),qty);

                        cmbItemCode.getSelectionModel().clearSelection();
                        txtQty.setText("");
                        txtQtyOnHand.setText("");
                        txtItemDes.setText("");

                        setTotal(placeOrderTMS);

                        return;
                    }


                }
                PlaceOrderTM orderTM=new PlaceOrderTM(cmbItemCode.getValue(), txtItemDes.getText(), txtQty.getText(), txtUprice.getText(), String.valueOf(qty * unitPrice), button);

                updateQtyOnHand(cmbItemCode.getValue(),qty);

                    placeOrderTMS.add(orderTM);
                    tblOrder.refresh();

                cmbItemCode.getSelectionModel().clearSelection();
                txtQty.setText("");
                txtQtyOnHand.setText("");
                txtItemDes.setText("");
                txtUprice.setText("");

                setTotal(placeOrderTMS);

                    return;
                       /* placeOrderTM.setCode(selectedItemCode);
                        placeOrderTM.setDes(txtItemDes.getText());
                        placeOrderTM.setQty(txtQty.getText());
                        placeOrderTM.setUprice(String.valueOf(unitPrice));
                        placeOrderTM.setTot(String.valueOf(qty * unitPrice));
                        placeOrderTM.setButton(button);
                        placeOrderTMS.add(placeOrderTM);*/

            }
            else {
                System.out.println("ffffff");
                placeOrderTMS.add(new PlaceOrderTM(cmbItemCode.getValue(), txtItemDes.getText(), txtQty.getText(), txtUprice.getText(), String.valueOf(qty * unitPrice), button));
                tblOrder.refresh();
                updateQtyOnHand(cmbItemCode.getValue(),qty);

                cmbItemCode.getSelectionModel().clearSelection();
                txtQty.setText("");
                txtQtyOnHand.setText("");
                txtItemDes.setText("");
                txtUprice.setText("");

                setTotal(placeOrderTMS);
            }

        }
        //update
        else{
            for (PlaceOrderTM tm:placeOrderTMS) {
                if(tm.getCode().equalsIgnoreCase(selectedOrder.getCode())){
                    placeOrderTM.setCode(selectedItemCode);
                    placeOrderTM.setDes(txtItemDes.getText());
                    placeOrderTM.setQty(txtQty.getText());
                    placeOrderTM.setUprice(String.valueOf(unitPrice));
                    placeOrderTM.setTot(String.valueOf(qty*unitPrice));
                    placeOrderTM.setButton(button);
                    int i=placeOrderTMS.indexOf(selectedOrder);
                    placeOrderTMS.set(i,placeOrderTM);

                    updateQtyOnHand(selectedItemCode,qty);

                    cmbItemCode.getSelectionModel().clearSelection();
                    txtQty.setText("");
                    txtQtyOnHand.setText("");
                    txtItemDes.setText("");
                    txtUprice.setText("");

                    setTotal(placeOrderTMS);

                }
            }
        }
        cmbItemCode.getSelectionModel().clearSelection();
        txtQty.setText("");
        txtQtyOnHand.setText("");
        txtItemDes.setText("");
        txtUprice.setText("");

    }

    private void setTotal(ObservableList<PlaceOrderTM> placeOrderTMS) {
        double Tot=0.0;
        for (PlaceOrderTM placeOrderTM:placeOrderTMS) {
            double tot=Double.parseDouble(placeOrderTM.getTot());
            Tot=Tot+tot;
        }
        labTotal.setText(String.valueOf(Tot));
    }



    private void updateQtyOnHand(String itemCode,int newQty) {
        for (Item item:tempItems) {
            if(item.getItemCode().equalsIgnoreCase(itemCode)){
                int newQtyOnHand=Integer.parseInt(item.getQty())-newQty;
                item.setQty(String.valueOf(newQtyOnHand));
                //txtQtyOnHand.setText(String.valueOf(newQtyOnHand));
            }
        }
    }

    @FXML
    void btnOrder(ActionEvent event) {
        if(tblOrder.getItems().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please put Some Items", ButtonType.OK).show();
            return;
        }
        // Let's save the order
        ArrayList<OrderDetail> OrderDetails = new ArrayList<>();
        ObservableList<PlaceOrderTM> olOrderDetails = tblOrder.getItems();
        for (PlaceOrderTM orderDetail : olOrderDetails) {
            // Let's update the stock
            //....updateStockQty(orderDetail.getCode(), orderDetail.getQty());
            OrderDetailPk orderDetailPk=new OrderDetailPk(labOrderId.getText(),orderDetail.getCode());
            OrderDetails.add(new OrderDetail(Integer.parseInt(orderDetail.getQty()),Double.parseDouble(orderDetail.getUprice()),orderDetailPk));
        }
        Order newOrder = new Order(labOrderId.getText(),
                LocalDate.now(),
                cmbCusId.getValue(),
                OrderDetails);
        Db.orders.add(newOrder);

        new Alert(Alert.AlertType.INFORMATION, "Order Added Successfully", ButtonType.OK).showAndWait();

        reset();

    }

    @FXML
    void iconHomeOnAction(MouseEvent event) {

    }

    private void loadItemCodes() {

        ObservableList<String> itemCodes=cmbItemCode.getItems();
        itemCodes.clear();
        for (Item item:tempItems) {
            String id=item.getItemCode();
            itemCodes.add(id);
        }
    }

    private void loadCustomerIDs() {
        ObservableList<String> cusCodes=cmbCusId.getItems();
        cusCodes.clear();
        for (Customer c:Db.customers) {
            String id=c.getId();
            cusCodes.add(id);
        }
    }

    public void navigate(MouseEvent mouseEvent) throws IOException {

        if (mouseEvent.getSource() instanceof FontAwesomeIconView){
            FontAwesomeIconView icon = (FontAwesomeIconView) mouseEvent.getSource();

            Parent root = null;

            switch(icon.getId()){
                case "iconHome":
                    root = FXMLLoader.load(this.getClass().getResource("/view/main.fxml"));
                    break;
                case "iconItem":
                    root = FXMLLoader.load(this.getClass().getResource("/view/itemView.fxml"));
                    break;
                case "iconPlaceOrder":
                    root = FXMLLoader.load(this.getClass().getResource("/view/PlaceOrderView.fxml"));
                    break;
                case "iconSearch":
                    root = FXMLLoader.load(this.getClass().getResource("/view/SearchOrderView.fxml"));
                    break;
            }

            if (root != null){
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }
    }

    private String getCustomerName(String id){
        String nm;
        for (Customer c:Db.customers) {

            if (id.equals(c.getId())){
                nm=c.getName();
                return nm;
            }

        }
        return null;
    }

    private Item getItems(String id) {
        Item item;
        for (Item i : Db.items) {
            if (id.equals(i.getItemCode())) {
                item = i;
                return item;
            }
        }
        return null;
    }

    public void setBtnDel(PlaceOrderTM placeOrderTM){
                for (Item item:tempItems) {
                    if(item.getItemCode().equalsIgnoreCase(placeOrderTM.getCode())){
                        item.setQty(Integer.valueOf(item.getQty())+Integer.valueOf(placeOrderTM.getQty())+"");
                    }
                }
                tblOrder.getItems().remove(placeOrderTM);
                tblOrder.refresh();
    }

    public String setOrderId(){
       if(!Db.orders.isEmpty()){
           Order order =Db.orders.get(Db.orders.size()-1);
           int idMax=Integer.parseInt(order.getOrderId().substring(1))+1;
           if(idMax<10){
               String max="O00"+idMax;
               return max;
           }
           else if(idMax<100 && idMax>=10){
               String max="O0"+idMax;
               return max;
           }
           else {
               String max="O"+idMax;
               return max;
           }
       }
       return "O001";
    }

    public void reset(){
        cmbItemCode.getSelectionModel().clearSelection();
        cmbCusId.getSelectionModel().clearSelection();
        txtUprice.setText("");
        txtItemDes.setText("");
        txtQty.setText("");
        txtQtyOnHand.setText("");
        btnAdd.setText("+ADD");
        tblOrder.getItems().clear();
        setOrderId();
        labTotal.setText("0.00");
        txtCusNm.setText("");
        cmbCusId.setDisable(false);
    }


    public void btnResetOnAction(ActionEvent event) {
        reset();
    }
}
