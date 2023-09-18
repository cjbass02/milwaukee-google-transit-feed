/*
 * Course: SE 2030 - 041
 * Fall 22-23
 * GTFS Project
 * Created by: Christian Basso, Ian Czerkis, Matt Wehman, Patrick McDonald.
 * Created on: 09/10/22
 * Copyright 2022 Ian Czerkis, Matthew Wehman, Patrick McDonald, Christian Basso

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * This class handles the method for the stop display
 * @author wehmanm
 * @version 1.0
 */
public class StopController {

    Controller controller;

    @FXML
    Text tripsText;

    @FXML
    Text stopID;

    @FXML
    Text routeList;

    @FXML
    Text nextTrip;


    public void setController(Controller controller){
        this.controller = controller;
    }

    protected void setTripsText(String string){
        tripsText.setText(string);
    }

    protected void setStopID(String string){
        stopID.setText(string);
    }

    protected void setRoutesText(String string) {
        routeList.setText(string);
    }

    protected void setNextTrip(String string){
        nextTrip.setText(string);
    }

}
