package controller;

import com.jfoenix.controls.JFXTextField;
import db.Db;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entity.Customer;
import entity.Item;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ItemViewController implements Initializable {

    public Button btnAdd;
    public AnchorPane root;
    public FontAwesomeIconView iconHome;
    @FXML
    private JFXTextField txtItemCode;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtUprice;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<ItemTM> tblItem;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    void btnSaveOnAtion(ActionEvent event) {

        if(btnSave.getText().equalsIgnoreCase("Save")){
            Db.items.add(new Item(txtItemCode.getText(),txtDescription.getText(),txtQty.getText(),txtUprice.getText()));
            btnAddOnAction(event);
            btnAdd.setDisable(false);
            loadItems();
        }
        else {
            ItemTM selectedItem = tblItem.getSelectionModel().getSelectedItem();
            selectedItem.setDescription(txtDescription.getText());
            selectedItem.setQty(txtQty.getText());
            selectedItem.setUprice(txtUprice.getText());
            btnAdd.setDisable(false);
            tblItem.refresh();
            btnAddOnAction(event);
           }

    }

    @FXML
    void txtSaveKeyRelease(KeyEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        tblItem.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblItem.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItem.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblItem.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("uprice"));
        tblItem.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("button"));

        loadItems();

        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            btnSave.setText("UPDATE");

            txtItemCode.setText(newValue.getItemCode());
            txtDescription.setText(newValue.getDescription());
            txtQty.setText(newValue.getQty());
            txtUprice.setText(newValue.getUprice());


        });



    }

    public void loadItems() {

        ObservableList<ItemTM> itemTMS=tblItem.getItems();
        itemTMS.clear();
        tblItem.refresh();
        for (Item item: Db.items) {
            Button button=new Button("DELETE");
            button.setStyle("-fx-background-color: blue");
            ItemTM itemTM=new ItemTM(item.getItemCode(),item.getDescription(),item.getQty(),item.getUprice(),button);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Db.items.remove(item);
                    loadItems();
                    tblItem.refresh();
                    btnAddOnAction(event);
                }
            });
            itemTMS.add(itemTM);
        }
    }

    public void btnAddOnAction(ActionEvent event) {

        txtDescription.setText("");
        txtQty.setText("");
        txtUprice.setText("");
        btnSave.setText("SAVE");

        int i=1;
        Item item=Db.items.get(Db.items.size()-1);
        String x=item.getItemCode();



        int id= Integer.valueOf(x.substring(1));
        int maxid=maxid=id+1;
        String newid;
        System.out.println(maxid);

        if(x.startsWith("I00")){

            newid="I00"+maxid;
            System.out.println(newid);
            txtItemCode.setText(newid);


        }
        else if(x.startsWith("I0")){

            newid="I0"+maxid;
            System.out.println(newid);
            txtItemCode.setText(newid);
        }
        else {

            newid="C"+maxid;
            System.out.println(newid);
            txtItemCode.setText(newid);
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
