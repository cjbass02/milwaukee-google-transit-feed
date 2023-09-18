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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is the Trip class. Most notably, it has a HashMap of StopTimes.
 *
 * @author czerkisi
 * @version 1.0
 * @created 05-Oct-2022 12:59:53 PM
 */
public class Trip {

    private String blockID;
    private String directionID;
    private String routeID;
    private String serviceID;
    private String shapeID;
    private String tripHeadSign;
    private String tripID;
    private final HashMap<String, ArrayList<StopTime>> stopTimes = new HashMap<>();


    public void setBlockID(String blockID) {
        this.blockID = blockID;
    }

    public void setDirectionID(String directionID) {
        this.directionID = directionID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public void setShapeID(String shapeID) {
        this.shapeID = shapeID;
    }

    public void setTripHeadSign(String tripHeadSign) {
        this.tripHeadSign = tripHeadSign;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getBlockID() {
        return blockID;
    }

    public String getDirectionID() {
        return directionID;
    }

    public String getRouteID() {
        return routeID.replaceAll("\\s", "");
    }

    public String getServiceID() {
        return serviceID;
    }

    public String getShapeID() {
        return shapeID;
    }

    public HashMap<String, ArrayList<StopTime>> getStopTimes() {
        return stopTimes;
    }

    public String getTripHeadSign() {
        return tripHeadSign;
    }

    public double distance() {
        return -1;
    }

    public String getTripID() {
        return tripID;
    }

    /**
     * This adds StopTimes to an arrayList. This method will handle the chaining for the
     * stop_times
     *
     * @param key
     * @param val
     * @author Patrick
     */
    public void addStopTime(String key, StopTime val) {
        if (stopTimes.containsKey(key)) {
            stopTimes.get(key).add(val);
        } else {
            stopTimes.put(key, new ArrayList<>());
            stopTimes.get(key).add(val);
        }
    }

    /**
     * Creates an instance of Trip Object
     *
     * @param routeID
     * @param serviceID
     * @param tripID
     * @param tripHeadSign
     * @param directionID
     * @param blockID
     * @param shapeID
     */
    public Trip(String routeID, String serviceID, String tripID, String tripHeadSign, String directionID, String blockID, String shapeID) {
        this.blockID = blockID;
        this.directionID = directionID;
        this.routeID = routeID;
        this.serviceID = serviceID;
        this.shapeID = shapeID;
        this.tripHeadSign = tripHeadSign;
        this.tripID = tripID;
    }

    /**
     * Takes in a new trip object and updates the old one
     * This method has not been implemented yet
     *
     * @param newTrip
     * @return boolean
     */
    public boolean update(Trip newTrip) {
        return false;
    }

    /**
     * checks that all required fields are filled
     *
     * @throws CSVReader.MissingRequiredFieldException if a required field is empty
     */
    public void checkRequired() throws CSVReader.MissingRequiredFieldException {
        if (routeID.isEmpty() | serviceID.isEmpty() | tripID.isEmpty()) {
            throw new CSVReader.MissingRequiredFieldException("A required field is empty");
        }
    }

    /**
     * Makes trip into a string using trip's variables
     *
     * @return string
     * @author wehman
     */
    public String toString() {
        String string = routeID + "," + serviceID + "," + tripID + "," + tripHeadSign + "," + directionID + "," + blockID + "," + shapeID;
        return string;
    }

}