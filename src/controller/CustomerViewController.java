package controller;

import com.jfoenix.controls.JFXTextField;
import db.Db;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entity.Customer;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable {


    public FontAwesomeIconView iconHome;
    public AnchorPane root;
    @FXML
    private JFXTextField txId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtAddress;

   @FXML
    private Button btnSave;

    @FXML
    private Button btnCus;

    @FXML
    private TableView<CustomerTM> tblCustomer;

    @FXML
    private TableColumn colCusId;

    @FXML
    private TableColumn colCusName;

    @FXML
    private TableColumn colCusAdd;

    @FXML
    private TableColumn colDel;

    @FXML
    private JFXTextField txtSearch;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colCusId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCusAdd.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDel.setCellValueFactory(new PropertyValueFactory<>("button"));

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            btnSave.setText("UPDATE");
            txId.setText(newValue.getId());
            txtName.setText(newValue.getName());
            txtAddress.setText(newValue.getAddress());
            btnCus.setDisable(true);



        });

        loadCustomer();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<CustomerTM> customerTMS=tblCustomer.getItems();
                customerTMS.clear();

                for (Customer customer:Db.customers) {
                    if(customer.getId().contains(newValue)||customer.getName().contains(newValue)||customer.getAddress().contains(newValue)) {
                        Button button=new Button("DELETE");
                        button.setStyle("-fx-background-color: red");
                        customerTMS.add(new CustomerTM(customer.getId(), customer.getName(), customer.getAddress(), button));
                    }

                }
            }
        });

    }


    @FXML
    void btnCusOnAction(ActionEvent event) {

        txtAddress.setText("");
        txtName.setText("");
        btnSave.setText("SAVE");

        int i=1;
        Customer customer=Db.customers.get(Db.customers.size()-1);
        String x=customer.getId();



        int id= Integer.valueOf(x.substring(1));
        int maxid=maxid=id+1;
        String newid;
        System.out.println(maxid);

        if(x.startsWith("C00")){

            newid="C00"+maxid;
            System.out.println(newid);
            txId.setText(newid);


        }
        else if(x.startsWith("C0")){

            newid="C0"+maxid;
            System.out.println(newid);
            txId.setText(newid);
        }
        else {

            newid="C"+maxid;
            System.out.println(newid);
            txId.setText(newid);
        }


    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

        if(btnSave.getText().equalsIgnoreCase("save")) {

            String id = txId.getText();
            String nm = txtName.getText();
            String add = txtAddress.getText();
            Customer customer = new Customer(id, nm, add);


            Db.customers.add(customer);
            loadCustomer();
            tblCustomer.refresh();
        }
        else {

            String id=txId.getText();
            String nm=txtName.getText();
            String add=txtAddress.getText();
            int i=0;
            System.out.println(id);

            for (Customer customer: Db.customers) {
                System.out.println(customer.getId());
                if(txId.getText().equals(customer.getId())){
                    System.out.println(customer.getId());
                    Customer c=new Customer(id,nm,add);
                    System.out.println("xx"+c);
                    System.out.println(i);

                    Db.customers.set(i,c);

                    loadCustomer();
                }
                i++;


            }
        }

       // loadCustomer();
        btnCus.setDisable(false);



    }





    public void loadCustomer(){

        ObservableList<CustomerTM> customerTMS=tblCustomer.getItems();
        customerTMS.clear();
        for (Customer customer: Db.customers) {

            Button del=new Button("DELETE");
            del.setStyle("-fx-background-color: red");

            String id=customer.getId();
            String  nm=customer.getName();
            String add=customer.getAddress();

            CustomerTM customerTM=new CustomerTM(id,nm,add,del);

            del.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Db.customers.remove(customer);
                    loadCustomer();
                    tblCustomer.refresh();
                }
            });

            customerTMS.add(customerTM);
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


}
