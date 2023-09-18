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

/**
 * This is the stop class and represents the places at which the buses stop
 * @author czerkisi
 * @version 1.0
 * @created 05-Oct-2022 12:59:52 PM
 */
public class Stop {

    private String stopDesc;
    private String stopID;
    private double stopLat;
    private double stopLong;
    private String stopName;

    public String getStopDesc() {
        return stopDesc;
    }

    public void setStopDesc(String stopDesc) {
        this.stopDesc = stopDesc;
    }

    public String getStopID() {
        return stopID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    public double getStopLat() {
        return stopLat;
    }

    public void setStopLat(int stopLat) {
        this.stopLat = stopLat;
    }

    public double getStopLong() {
        return stopLong;
    }

    public void setStopLong(int stopLong) {
        this.stopLong = stopLong;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

	/**
	 * Creates an instance of the Stop Object
	 * @param stopID
	 * @param stopName
	 * @param stopDesc
	 * @param stopLat
	 * @param stopLong
	 */
    public Stop(String stopID, String stopName, String stopDesc, double stopLat, double stopLong) {
        this.stopDesc = stopDesc;
        this.stopID = stopID;
        this.stopLat = stopLat;
        this.stopLong = stopLong;
        this.stopName = stopName;
    }

    /**
	 * Changes the Longitude and Latitude of a Stop
	 * This method has not been implemented yet
     * @param longitude
     * @param latitude
	 * @return boolean
     */
    public boolean changeLocation(int longitude, int latitude) {
        return false;
    }

    /**
	 * Takes in a new Stop object in place of the old Stop, this will update the certain
	 * attributes that need tobe updated
	 * This method has not been implemented yet
     * @param newStop
	 * @return boolean
     */
    public boolean update(Stop newStop) {
        return false;
    }

    /**
     * ensures all required fields are filled in
     * @throws CSVReader.MissingRequiredFieldException if a required field is empty
     */
    public void checkRequired() throws CSVReader.MissingRequiredFieldException {
        if (stopID.isEmpty()){
            throw new CSVReader.MissingRequiredFieldException("Missing a required field");
        }
    }

    @Override
    public String toString() {
        return stopID + "," +
                stopName + "," +
                stopDesc + "," +
                stopLat + "," +
                stopLong;
    }
}