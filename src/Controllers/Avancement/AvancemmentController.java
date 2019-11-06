package Avancement;

import DataBase.BaseDeDonnes;
import DataBase.ConnectionDataBase;
import Gestion_Enseignant.ClassEnseignant;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import static GestionParcours.GestionParcoursController.Numero_Ens;
import static Gestion_Enseignant.InterfaceChangePaneController.GestionPromotion;
import static Gestion_Enseignant.InterfaceChangePaneController.GestionVerification;


/**
 * FXML Controller class
 *
 * @author wasbo
 */
public class AvancemmentController extends Thread implements Initializable {

    ObservableList<ClassEnseignant> list = FXCollections.observableArrayList();
    ConnectionDataBase DataBase = new ConnectionDataBase();
    BaseDeDonnes Requette = new BaseDeDonnes();
    Connection conn = null;
    Statement pst = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    ResultSet rs3 = null;
    PreparedStatement pstt = null;
    Calendar cal = new GregorianCalendar();
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);
    Boolean test;
    Integer Annee;
    @FXML
    private TableView<ClassEnseignant> table_Ens;
    @FXML
    private TableColumn<ClassEnseignant, String> CNomEns;
    @FXML
    private TableColumn<ClassEnseignant, String> CPrenomEns;
    @FXML
    private TableColumn<ClassEnseignant, String> CSexe;
    @FXML
    private TableColumn<ClassEnseignant, String> CSituation_familiale;
    public Boolean consulter = true;
    @FXML
    private AnchorPane ToPromotion;
    @FXML
    private TableColumn<ClassEnseignant, String> CType;
    @FXML
    private Text medium;
    @FXML
    private Text min;
    @FXML
    private Text max;
    @FXML
    private TableColumn<ClassEnseignant, String> CGrade;
    @FXML
    private TableColumn<ClassEnseignant, String> CEchelon;
    @FXML
    private RadioButton Echelon;
    @FXML
    private ToggleGroup group;
    @FXML
    private RadioButton Grade;
    @FXML
    private RadioButton Numero;
    @FXML
    private RadioButton Nom;
    @FXML
    private JFXTextField recherche;
    @FXML
    private ToggleGroup not;
    @FXML
    private JFXRadioButton activer;
    @FXML
    private JFXRadioButton desactiver;

    public static Boolean Teste = false;
    public AnchorPane AlleGestionPromotion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = DataBase.ConnectDb();

        initCol();
        Teste = false;
        tester();
        //kahla();
    

        
    
         start();
    }

    public AvancemmentController() {
        super(); //appelle thred

        consulter = true;
        conn = ConnectionDataBase.ConnectDb();
    }

    @Override
    public synchronized void run() {
        while (consulter) {
            try {
                kahla();
                wait((10000));
            } catch (InterruptedException ex) {
                Logger.getLogger(AvancemmentController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private void initCol() { //intialiser les colonnes de la table avec les champs de la classe enseignant
        CNomEns.setCellValueFactory(new PropertyValueFactory<>("NomEns"));
        CPrenomEns.setCellValueFactory(new PropertyValueFactory<>("PrenomEns"));
        CSexe.setCellValueFactory(new PropertyValueFactory<>("Sexe"));

        CSituation_familiale.setCellValueFactory(new PropertyValueFactory<>("Situation_familiale"));

        CEchelon.setCellValueFactory(new PropertyValueFactory<>("Echelon"));
        CGrade.setCellValueFactory(new PropertyValueFactory<>("Grade"));
        CType.setCellValueFactory(new PropertyValueFactory<>("TestAvancement"));
        CType.setCellFactory(column -> {
            return new TableCell<ClassEnseignant, String>() {

                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");

                    } else if (item.equals("MAX")) {

                        setStyle("-fx-background-color: #e41010 ");
                        setText("MAX");
                        setTextFill(Color.WHITE);
                        setAlignment(Pos.CENTER);
                    } else if (item.equals("MEDIUM")) {
                        setStyle("-fx-background-color: #f88b06");
                        setText("MEDIUM");
                        setTextFill(Color.WHITE);
                        setAlignment(Pos.CENTER);
                    } else if (item.equals("MIN")) {
                        setStyle("-fx-background-color: #1bb173");
                        setText("MIN");
                        setTextFill(Color.WHITE);
                        setAlignment(Pos.CENTER);
                    }
                }
            };
        });

    }

    @FXML
    private void tabledoubleclick(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {

            if (!table_Ens.getSelectionModel().isEmpty()) {
                System.out.print("TresBien");
                ClassEnseignant Ens = (ClassEnseignant) table_Ens.getSelectionModel().getSelectedItem();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/GestionParcours/GestionParcours.fxml"));
                //Numero_Ens = Ens.getNumero_Ens();
                //GestionPromotion= fxmlLoader.load();
                //setNode(GestionPromotion);
            }
        }
    }

    private void setNode(Node node) {

        ToPromotion.getChildren().clear();
        ToPromotion.getChildren().add((Node) node);

        FadeTransition transition = new FadeTransition(Duration.millis(1500));
        transition.setNode(node);
        transition.setFromValue(0.1);
        transition.setToValue(1);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.play();
    }

    @FXML
    private void Recherche() {

        String s = null;
        if (Nom.isSelected()) {
            s = "Nom";
        } else if (Grade.isSelected()) {
            s = "intituleGrade";
        } else if (Echelon.isSelected()) {
            s = "idEchelon";
        } else if (Numero.isSelected()) {
            s = "Numero_Ens";
        }

        String requete = "AND " + s + " like '%" + recherche.getText() + "%'";
        if (month >= 1 && month <= 8) {
            Annee = year - 1;
        } else if (month > 8 && month <= 12) {
            Annee = year;
        }

        System.out.println(Annee);

        String sql1 = "select * from  enseignant e,possedeechelon pe,possedegrade pg,grade g where pg.idGrade =g.idGrade AND pe.idEmployee =e.Numero_Ens AND pg.id =e.Numero_Ens AND (DATE_ADD(pe.dateEch, INTERVAL 30 MONTH ) BETWEEN ('" + Annee + "-09-01') and ('" + (Annee + 1) + "-09-01'))" + requete;
        String sql2 = "select * from  enseignant e,possedeechelon pe,possedegrade pg,grade g where pg.idGrade =g.idGrade AND pe.idEmployee =e.Numero_Ens AND pg.id =e.Numero_Ens AND (DATE_ADD(pe.dateEch, INTERVAL 36 MONTH ) BETWEEN ('" + Annee + "-09-01') and ('" + (Annee + 1) + "-09-01'))" + requete;
        String sql3 = "select * from  enseignant e,possedeechelon pe,possedegrade pg,grade g where pg.idGrade =g.idGrade AND pe.idEmployee =e.Numero_Ens AND pg.id =e.Numero_Ens AND (DATE_ADD(pe.dateEch, INTERVAL 42 MONTH ) BETWEEN ('" + Annee + "-09-01') and ('" + (Annee + 1) + "-09-01'))" + requete;

        table_Ens.setItems(remplir(sql1, sql2, sql3));

    }

    private void kahla() {

        if (month >= 1 && month <= 8) {
            Annee = year - 1;
        } else if (month > 8 && month <= 12) {
            Annee = year;
        }

        System.out.println(Annee);

        String sql1 = "select * from  enseignant e,possedeechelon pe,possedegrade pg,grade g where pg.idGrade =g.idGrade AND pe.idEmployee =e.Numero_Ens AND pg.id =e.Numero_Ens AND (DATE_ADD(pe.dateEch, INTERVAL 30 MONTH ) BETWEEN ('" + Annee + "-09-01') and ('" + (Annee + 1) + "-09-01'))";
        String sql2 = "select * from  enseignant e,possedeechelon pe,possedegrade pg,grade g where pg.idGrade =g.idGrade AND pe.idEmployee =e.Numero_Ens AND pg.id =e.Numero_Ens AND (DATE_ADD(pe.dateEch, INTERVAL 36 MONTH ) BETWEEN ('" + Annee + "-09-01') and ('" + (Annee + 1) + "-09-01'))";
        String sql3 = "select * from  enseignant e,possedeechelon pe,possedegrade pg,grade g where pg.idGrade =g.idGrade AND pe.idEmployee =e.Numero_Ens AND pg.id =e.Numero_Ens AND (DATE_ADD(pe.dateEch, INTERVAL 42 MONTH ) BETWEEN ('" + Annee + "-09-01') and ('" + (Annee + 1) + "-09-01'))";
        if (Teste) {
            table_Ens.setItems(remplir(sql1, sql2, sql3));
            //table_Ens.setItems(remplir(sql2));
        }
    }

    private ObservableList<ClassEnseignant> remplir(String sql1, String sql2, String sql3) {
        ObservableList<ClassEnseignant> listTest = FXCollections.observableArrayList();
        int i = 0;
        try {

            pst = conn.createStatement();
            rs = pst.executeQuery(sql1);

            while (rs.next()) {

                listTest.add(new ClassEnseignant(rs.getInt("idEnseignant"), rs.getString("Numero_Ens"), rs.getString("Nom"), rs.getString("Prenom"), rs.getString("Sexe"),
                        rs.getString("Date_naissance"), rs.getString("Lieu_naissance"), rs.getString("Nationalité"), rs.getInt("Nombre_enfant"), rs.getString("Situation_familiale"),
                        rs.getString("Adresse"), rs.getString("Telephone"), rs.getString("Email"), rs.getString("Date_recrutement"), rs.getString("lieu_recrutement"),
                        rs.getString("Date_instalation"), rs.getString("statu"), rs.getString("Date_statu"), rs.getString("idEchelon"), rs.getString("intituleGrade"), "MIN"));
                i += 1;
            }
            min.setText(String.valueOf(i));
            i = 0;
            rs2 = pst.executeQuery(sql2);
            while (rs2.next()) {

                ClassEnseignant m = new ClassEnseignant(rs2.getInt("idEnseignant"), rs2.getString("Numero_Ens"), rs2.getString("Nom"), rs2.getString("Prenom"), rs2.getString("Sexe"),
                        rs2.getString("Date_naissance"), rs2.getString("Lieu_naissance"), rs2.getString("Nationalité"), rs2.getInt("Nombre_enfant"), rs2.getString("Situation_familiale"),
                        rs2.getString("Adresse"), rs2.getString("Telephone"), rs2.getString("Email"), rs2.getString("Date_recrutement"), rs2.getString("lieu_recrutement"),
                        rs2.getString("Date_instalation"), rs2.getString("statu"), rs2.getString("Date_statu"), rs2.getString("idEchelon"), rs2.getString("intituleGrade"), "MEDIUM");
                // System.out.println(doublons(listTest, m));

                if (!doublons(listTest, m)) {
                    i += 1;
                    listTest.add(m);
                }

            }
            medium.setText(String.valueOf(i));
            i = 0;
            rs3 = pst.executeQuery(sql3);
            while (rs3.next()) {

                ClassEnseignant m = new ClassEnseignant(rs3.getInt("idEnseignant"), rs3.getString("Numero_Ens"), rs3.getString("Nom"), rs3.getString("Prenom"), rs3.getString("Sexe"),
                        rs3.getString("Date_naissance"), rs3.getString("Lieu_naissance"), rs3.getString("Nationalité"), rs3.getInt("Nombre_enfant"), rs3.getString("Situation_familiale"),
                        rs3.getString("Adresse"), rs3.getString("Telephone"), rs3.getString("Email"), rs3.getString("Date_recrutement"), rs3.getString("lieu_recrutement"),
                        rs3.getString("Date_instalation"), rs3.getString("statu"), rs3.getString("Date_statu"), rs3.getString("idEchelon"), rs3.getString("intituleGrade"), "MAX");
                // System.out.println(doublons(listTest, m));

                if (!doublons(listTest, m)) {
                    i += 1;
                    listTest.add(m);
                }

            }
            max.setText(String.valueOf(i));

        } catch (SQLException ex) {
            Logger.getLogger(AvancemmentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!listTest.isEmpty()) {
            if (Teste) {
                notification(" Enseignant à promouvoir");
            }
        }
        return listTest;

    }

    private Boolean doublons(ObservableList<ClassEnseignant> list, ClassEnseignant m) {
        ListIterator<ClassEnseignant> it = list.listIterator();
        Boolean Test = false;
        while (it.hasNext()) {

            if (it.next().getIdEns() == m.getIdEns()) {
                Test = true;
                break;
            } else {
                Test = false;
            }
        }
        return Test;
    }

    @FXML
    public void tester() {

        if (activer.isSelected()) {
            Teste = true;
        }
        if (desactiver.isSelected()) {
            Teste = false;
        }

    }

    private void notification(String s) {//notification de comfirmation

        Image m = new Image("/IMAGE/checked(3).png");
        Notifications n = Notifications.create()
                .title("Comfirmation")
                .text(s)
                .graphic(new ImageView(m))
                .hideAfter(Duration.minutes(15))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.print("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });
        n.darkStyle();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                n.show();
            }
        });

    }

}
