import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * This class handles methods for stopDisplay
 * @author Patrick
 */
public class TripController {

    Controller controller;

    @FXML
    Text tripId;

    @FXML
    Text tripDistance;

    @FXML
    Text tripSpeed;

    @FXML
    Text tripStatus;

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void setTripId(String tId){
        tripId.setText(tId);
    }

    public void setTripDistance(String tripDist){
        tripDistance.setText(tripDist);
    }

    public void setTripSpeed(String tripSpd){
        tripSpeed.setText(tripSpd);
    }

    public void setTripStatus(String tripStatus){this.tripStatus.setText(tripStatus);}
}
