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

/**
 * This is the Route class and it is the path which a bus will take
 * @author czerkisi
 * @version 1.0
 * @created 05-Oct-2022 12:59:52 PM
 */
public class Route {

    private String agencyID;
    private String routeColor;
    private String routeDesc;
    private String routeID;
    private String routeLongName;
    private String routeShortName;
    private String routeTextColor;
    private String routeType;
    private String routeURL;
    private HashMap<String, Stop> stops = new HashMap<>();
    private HashMap<String, Trip> trips = new HashMap<>();
    //Testing
    private HashMap<String, ArrayList<Stop>> allStopsList = new HashMap<>();
    private HashMap<String, ArrayList<Trip>> tripsList = new HashMap<>();

    /**
     * Creates an instance of a Route
     * @param routeID
     * @param agencyID
     * @param routeShortName
     * @param routeLongName
     * @param routeDesc
     * @param routeType
     * @param routeURL
     * @param routeColor
     * @param routeTextColor
     */
    public Route(String routeID, String agencyID, String routeShortName, String routeLongName, String routeDesc, String routeType, String routeURL, String routeColor, String routeTextColor) {
        try {
            this.agencyID = agencyID;
            this.routeColor = routeColor;
            this.routeDesc = routeDesc;
            this.routeID = routeID;
            this.routeLongName = routeLongName;
            this.routeShortName = routeShortName;
            this.routeTextColor = routeTextColor;
            this.routeType = routeType;
            this.routeURL = routeURL;
        } catch (NumberFormatException e){
            throw new RuntimeException("File is not formatted correctly");
        }
    }

    public void setAgencyID(String agencyID) {
        this.agencyID = agencyID;
    }

    public void setRouteColor(String routeColor) {
        this.routeColor = routeColor;
    }

    public void setRouteDesc(String routeDesc) {
        this.routeDesc = routeDesc;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public void setRouteLongName(String routeLongName) {
        this.routeLongName = routeLongName;
    }

    public void setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
    }

    public void setRouteTextColor(String routeTextColor) {
        this.routeTextColor = routeTextColor;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public void setRouteURL(String routeURL) {
        this.routeURL = routeURL;
    }

    public String getAgencyID() {
        return agencyID;
    }

    public String getRouteColor() {
        return routeColor;
    }

    public String getRouteDesc() {
        return routeDesc;
    }

    public String getRouteID() {
        return routeID;
    }

    public String getRouteLongName() {
        return routeLongName;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public String getRouteTextColor() {
        return routeTextColor;
    }

    public String getRouteType() {
        return routeType;
    }

    public String getRouteURL() {
        return routeURL;
    }

    public HashMap<String, Stop> getStops() {
        return stops;
    }

    public HashMap<String, Trip> getTrips() {
        return trips;
    }

    public HashMap<String, ArrayList<Stop>> getStopsList() {
        return allStopsList;
    }

    public HashMap<String, ArrayList<Trip>> getTripsList() {
        return tripsList;
    }

    private int displayDist() {
        return 0;
    }

    public void addStop(Stop stop){
        ArrayList<Stop> stops1 = new ArrayList<>();
        stops1.add(stop);
        allStopsList.put(stop.getStopID(), stops1);
    }

    /**
     * This takes in a new Route in place of the old one
     * This method has not been implemented yet
     * @param newRoute
     * @return boolean
     */
    public boolean update(Route newRoute) {
        return false;
    }

    /**
     * This adds Trips to an arrayList. This method will handle the chaining for the
     * Trips in tripList
     * @param key
     * @param val
     * @author Patrick
     */
    public void addTrip(String key, Trip val){
        if (tripsList.containsKey(key)){
            tripsList.get(key).add(val);
        } else {
            tripsList.put(key, new ArrayList<>());
            tripsList.get(key).add(val);
        }
    }

    /**
     * This adds Stops to an arrayList. This method will handle the chaining for the
     * stops in allStopsList
     * @param key
     * @param val
     * @author Patrick
     */
    public void addStops(String key, Stop val){
        if (allStopsList.containsKey(key)){
            allStopsList.get(key).add(val);
        } else {
            allStopsList.put(key, new ArrayList<>());
            allStopsList.get(key).add(val);
        }
    }

    /**
     * checks that all required fields are filled in
     * @throws CSVReader.MissingRequiredFieldException if a required field is empty
     */
    public void checkRequired() throws CSVReader.MissingRequiredFieldException {
        if (routeID.isEmpty() | routeColor.isEmpty()){
            throw new CSVReader.MissingRequiredFieldException("A required field is missing");
        }
    }



    public boolean equals(Route r) {
        return
                this.routeID.equals(r.routeID) &&
                        this.routeColor.equals(r.routeColor) &&
                        this.routeDesc.equals((r.routeDesc)) &&
                        this.agencyID.equals(r.agencyID) &&
                        this.routeLongName.equals(r.routeLongName) &&
                        this.routeShortName.equals(r.routeShortName) &&
                        this.routeURL.equals(r.routeURL) &&
                        this.routeType.equals(r.routeType);

    }

    @Override
    public String toString() {
        return routeID + ","
                +agencyID + ","
                +routeShortName + ","
                +routeLongName  + ","
                +routeDesc + ","
                +routeType  + ","
                +routeURL + ","
                +routeColor + ","
                +routeTextColor;
    }
}