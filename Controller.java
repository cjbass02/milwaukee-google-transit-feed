/*
 * Course: SE 2030 - 041
 * Fall 22-23 test
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

import java.io.*;

import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MarkerEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.time.Duration;
import java.time.Instant;

/**
 * This class handles the methods from the GUI
 *
 * @author czerkisi
 * @version 1.0
 * @created 05-Oct-2022 12:59:52 PM
 */
public class Controller {

    private final URL url = this.getClass().getResource("mapmarkerhd_106079 (1).png");
    private final URL busURL = this.getClass().getResource("ic_directions_bus_128_28256.png");


    @FXML
    Button importButton;

    @FXML
    Button exportButton;

    @FXML
    Button stopIdButton;

    @FXML
    Button routeIdButton;

    @FXML
    Button tripIdButton;

    @FXML
    TextField searchBar;
    @FXML
    Stage tripDisplay;

    @FXML
    Stage routeDisplay;

    @FXML
    Stage stopDisplay;

    StopController stopController;
    TripController tripController;

    @FXML
    MapView mapView;

    Stage stage;

    public List<Marker> markers;


    protected HashMap<String, ArrayList<Stop>> allStopsList = new HashMap<>();
    protected HashMap<String, ArrayList<Trip>> tripsList = new HashMap<>();
    protected HashMap<String, ArrayList<Route>> routesList = new HashMap<>();

    /**
     * Creates Controller instance
     */
    public Controller() {

    }

    /**
     * This adds Trips to an arrayList. This method will handle the chaining for the
     * Trips in tripList
     *
     * @param key
     * @param val
     * @author Patrick
     */
    public void addTrip(String key, Trip val) {
        if (tripsList.containsKey(key)) {
            tripsList.get(key).add(val);
        } else {
            tripsList.put(key, new ArrayList<>());
            tripsList.get(key).add(val);
        }
    }

    /**
     * This adds Routes to an arrayList. This method will handle the chaining for the
     * Routes in RouteList
     * @param key
     * @param val
     * @author Patrick
     */
    public void addRoute(String key, Route val) {
        if (routesList.containsKey(key)) {
            routesList.get(key).add(val);
        } else {
            routesList.put(key, new ArrayList<>());
            routesList.get(key).add(val);
        }
    }

    /**
     * This adds Stops to an arrayList. This method will handle the chaining for the
     * stops in allStopsList
     * @param key
     * @param val
     * @author Patrick
     */
    public void addStops(String key, Stop val) {
        if (allStopsList.containsKey(key)) {
            allStopsList.get(key).add(val);
        } else {
            allStopsList.put(key, new ArrayList<>());
            allStopsList.get(key).add(val);
        }
    }


    /**
     * gets the text from the search bar
     *
     * @return String id
     */
    public String getId() {
        return searchBar.getText();
    }

    public void setStopController(StopController stop) {
        stopController = stop;
    }

    public void setTripController(TripController trip) {
        tripController = trip;
    }

    public void setStage(Stage primary){
        this.stage = primary;
    }

    /**
     * Show the stop stage and sets all information inside it
     *
     * @param actionevent when button is clicked
     * @author Matt Wehman
     */
    @FXML
    public void generateStopIdInterface(ActionEvent actionevent) {
        String stopId = getId();
        if (allStopsList.containsKey(stopId)) {
            stopController.setTripsText(String.valueOf(tripsPerStop(stopId)));
            ArrayList<Stop> selectedStopId = allStopsList.get(stopId);
            for (Stop s : selectedStopId) {
                if (s.getStopID().equals(stopId)) {
                    stopController.setStopID(s.getStopName());
                }
            }
            ArrayList<String> routesContaining = routesContainingStop(stopId);
            String routesString = "";
            for (int i = 0; i < routesContaining.size() - 1; i++) {
                routesString += routesContaining.get(i) + ", ";
            }
            if (routesContaining.size() > 0) {
                routesString += routesContaining.get(routesContaining.size() - 1);
            } else {
                routesString = "No routes service this stop";
            }
            stopController.setRoutesText(routesString);
            stopDisplay.setX(stage.getX() + stopDisplay.getWidth());
            stopDisplay.setY(stage.getY());
            stopDisplay.show();
            stopController.setTripsText(String.valueOf(tripsPerStop(stopId)));
            stopController.setStopID(stopId);
            plotStop(stopId);
            Time currentTime = java.sql.Time.valueOf(LocalTime.now());
            stopController.setNextTrip(nextTripAtStop(stopId, currentTime));
        } else {
            errorAlert("Stop Not Found", "Ensure the GTFS files have been imported");
        }
    }



    /**
     * Shows the trip stage and sets all information inside it
     *
     * @param actionevent when button is clicked
     */
    @FXML
    public void generateTripIdInterface(ActionEvent actionevent) {
        String tripId = getId();
        if (tripsList.containsKey(tripId)) {
            stopController.setTripsText(String.valueOf(tripsPerStop(tripId)));
            ArrayList<Trip> selectedTripId = tripsList.get(tripId);
            for (Trip t : selectedTripId) {
                if (t.getTripID().equals(tripId)) {
                    tripController.setTripId(t.getTripID());
                }
            }
            if(stopDisplay.isShowing()){
                stopDisplay.hide();
            }
            tripDisplay.setX(0);
            tripDisplay.show();
            tripController.setTripDistance(displayCumulativeDistance(tripId) + " mile");
            tripController.setTripSpeed(speedOfTrip(tripId) + " mph");
            if (plotBus(tripId)){
                tripController.setTripStatus("Active");
            } else {
                tripController.setTripStatus("Inactive");
            }
        } else {
            errorAlert("Trip Not Found", "Ensure the GTFS files have been imported");
        }

    }

    /**
     * Shows the route stage and sets all information inside it
     *
     * @param actionevent when button is clicked
     */
    @FXML
    public void generateRouteIdInterface(ActionEvent actionevent) {
        routeDisplay.show();
    }


    /**
     * Sets the trip stage
     *
     * @param stage stage to be set
     */
    protected void setTripStage(Stage stage) {
        this.tripDisplay = stage;
    }

    /**
     * Sets the stop stage
     *
     * @param stage stage to be set
     */
    protected void setStopStage(Stage stage) {
        this.stopDisplay = stage;
    }


    /**
     * Gets all the stops in a route by searching the routeID
     * This method has not been implemented
     *
     * @param routeID
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> allStopsInRoute(int routeID) {
        return null;

    }


    /**
     * Displays the total distance of a route
     *
     * @return double
     * @author Patrick
     */
    public String displayCumulativeDistance(String tripId) {
        ArrayList<Trip> trips = tripsList.get(tripId);
        double cuDist = 0;
        for (Trip trip : trips) {
            if (trip.getTripID().equals(tripId)) {
                int startSeq = 1;
                String startStopId = "";
                String endStopId = "";
                boolean startChanged = false;
                boolean endChanged = false;
                double distance = 0;
                HashSet<String> lat1Set = new HashSet<>();
                HashSet<String> lat2Set = new HashSet<>();

                for(int i = 0; i < trip.getStopTimes().size(); i++) {
                    for (Map.Entry<String, ArrayList<StopTime>> stopTimesMap : trip.getStopTimes().entrySet()) {
                        ArrayList<StopTime> stopTimesList = stopTimesMap.getValue();
                        for (StopTime stopTime : stopTimesList) {
                            if (stopTime.getStopSequence().equals("" + startSeq)) {
                                startStopId = stopTime.getStopID();
                            } else if (stopTime.getStopSequence().equals("" + (startSeq + 1))) {
                                endStopId = stopTime.getStopID();
                            }
                        }
                    }
                    startSeq++;
                    double lat1 = 0;
                    double lon1 = 0;
                    double lat2 = 0;
                    double lon2 = 0;

                    Set<Map.Entry<String, ArrayList<Stop>>> stopSet = allStopsList.entrySet();
                    for (Map.Entry<String, ArrayList<Stop>> stops : stopSet) {
                        for (Stop s : stops.getValue()) {
                            if (s.getStopID().equals(startStopId)) {
                                lat1 = s.getStopLat();
                                lon1 = s.getStopLong();
                                startChanged = true;
                            } else if (s.getStopID().equals(endStopId)) {
                                lat2 = s.getStopLat();
                                lon2 = s.getStopLong();
                                endChanged = true;
                            }
                        }
                    }
                    if (!startChanged || !endChanged) {
                        System.out.println("throw exception");
                    } else if(!startStopId.equals(endStopId)) {
                        double latDistance = Math.toRadians(lat2 - lat1);
                        double lonDistance = Math.toRadians(lon2 - lon1);
                        lat1 = Math.toRadians(lat1);
                        lat2 = Math.toRadians(lat2);
                        double a = Math.pow(Math.sin(latDistance / 2), 2) +
                                Math.pow(Math.sin(lonDistance / 2), 2) *
                                        Math.cos(lat1) *
                                        Math.cos(lat2);
                        double rad = 6371;
                        double c = 2 * Math.asin(Math.sqrt(a));
                        distance = rad * c;
                        cuDist += distance;

                    }
                }
            }
        }

        DecimalFormat df = new DecimalFormat("#.###");
        double distMile = cuDist * 0.621;
        return "" + df.format(distMile);
    }

    /**
     * Displays the average speed of a route
     *
     * @return double speed the speed of the trip in mph
     * @author Patrick
     */
    public String speedOfTrip(String tripId) {
        ArrayList<Trip> trips = tripsList.get(tripId);
        String distance = displayCumulativeDistance(tripId);
        int totalMin = 0;
        double dist = 0;
        try{
            dist = Double.parseDouble(distance);
        } catch(NumberFormatException e){
            System.out.println("Tried to parse something that can't be turned into a double");
        }
        for (Trip trip : trips) {
            if (trip.getTripID().equals(tripId)) {
                int startSeq = 1;
                StopTime time1 = null;
                StopTime time2 = null;
                for(int i = 0; i < trip.getStopTimes().size(); i++) {
                    for (Map.Entry<String, ArrayList<StopTime>> stopTimesMap : trip.getStopTimes().entrySet()) {
                        ArrayList<StopTime> stopTimesList = stopTimesMap.getValue();
                        for (StopTime stopTime : stopTimesList) {
                            if (stopTime.getStopSequence().equals("" + startSeq)) {
                                time1 = stopTime;
                            } else if (stopTime.getStopSequence().equals("" + (startSeq + 1))) {
                                time2 = stopTime;
                            }
                        }
                    }
                    startSeq++;
                    long seconds = 0;
                    if((!time2.getIsNextDay() && !time1.getIsNextDay()) ||
                            (time1.getIsNextDay() && time2.getIsNextDay())){
                        seconds = Math.abs(Duration.between(LocalTime.parse(time2.getArrivalTime().toString()),
                                LocalTime.parse(time1.getArrivalTime().toString())).getSeconds());
                    } else if(!time2.getIsNextDay() && time1.getIsNextDay()){
                        seconds = Math.abs(Duration.between(LocalTime.parse(time2.getArrivalTime().toString()),
                                LocalTime.parse("23:59:59")).getSeconds()) + 1;
                        seconds = seconds + Math.abs(Duration.between(LocalTime.parse(time1.getArrivalTime().toString()),
                                LocalTime.parse("00:00:00")).getSeconds());
                    } else if(time2.getIsNextDay() && !time1.getIsNextDay()){
                        seconds = Math.abs(Duration.between(LocalTime.parse(time1.getArrivalTime().toString()),
                                LocalTime.parse("23:59:59")).getSeconds()) + 1;
                        seconds = seconds + Math.abs(Duration.between(LocalTime.parse(time2.getArrivalTime().toString()),
                                LocalTime.parse("00:00:00")).getSeconds());
                    }
                    int secondsInt = (int)seconds;
                    int minutes = secondsInt/60;
                    totalMin = totalMin + minutes;
                }
            }
        }
        double spd = dist/totalMin;
        spd *= 60; // change to mph
        DecimalFormat df = new DecimalFormat("#.###");
        String ret = "" + df.format(spd);
        return ret;
    }


    public void exportHelper(ActionEvent actionEvent) {
        if (routesList.size() > 0 && allStopsList.size() > 0) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Save Directory");
            File file = fileChooser.showSaveDialog(null);
            boolean bool = file.mkdir();
            File routeExport = exportRoutes(file.toPath());
            File stopExport = exportStops(file.toPath());
            File tripExport = exportTrips(file.toPath());
            File stopTimeExport = exportStopTimes(file.toPath());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Image image = new Image("https://img.icons8.com/fluency/48/000000/checked.png");
            ImageView imageView = new ImageView(image);
            alert.setGraphic(imageView);
            alert.setTitle("Successful Export");
            alert.setHeaderText("Export Successful");
            alert.setContentText("All files were exported successfully");
            alert.showAndWait();
        } else {
            errorAlert("No files", "Please import files before trying to export files");
        }


    }

    /**
     * Exports the GTFS files to the desired place
     * This method has not been implemented
     *
     * @return route gtfs file
     */
    public File exportRoutes(java.nio.file.Path path) {
        File routeFile = new File(path + "/routes.txt");
        FileWriter writer = null;
        Set<Map.Entry<String, ArrayList<Route>>> routeSet = routesList.entrySet();

        Iterator<Map.Entry<String, ArrayList<Route>>> it = routeSet.iterator();
        try {
            writer = new FileWriter(routeFile);
            writer.write("route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color");
            while (it.hasNext()) {
                Map.Entry<String, ArrayList<Route>> routes = it.next();
                for (Route r : routes.getValue()) {
                    writer.write("\n" + r.toString());
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Route file could not be found");
        }
        return routeFile;

    }


    /**
     * Creates a file of each imported stop
     *
     * @param path the path of where to save the file
     * @return file of all stops, in correct format.
     */
    public File exportStops(java.nio.file.Path path) {
        File stopFile = new File(path + "/stops.txt");
        FileWriter writer = null;
        Set<Map.Entry<String, ArrayList<Stop>>> stopSet = allStopsList.entrySet();
        Iterator<Map.Entry<String, ArrayList<Stop>>> it = stopSet.iterator();
        try {
            writer = new FileWriter(stopFile);
            writer.write("stop_id,stop_name,stop_desc,stop_lat,stop_lon");
            while (it.hasNext()) {
                Map.Entry<String, ArrayList<Stop>> stops = it.next();
                for (Stop s : stops.getValue()) {
                    writer.write("\n" + s.toString());
                }

            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Stop file could not be found");
        }

        return stopFile;

    }

    public File exportTrips(java.nio.file.Path path) {
        File tripFile = new File(path + "/trips.txt");
        FileWriter writer = null;
        Set<Map.Entry<String, ArrayList<Trip>>> tripSet = tripsList.entrySet();
        Iterator<Map.Entry<String, ArrayList<Trip>>> it = tripSet.iterator();
        try {
            writer = new FileWriter(tripFile);
            writer.write("route_id,service_id,trip_id,trip_headsign,direction_id,block_id,shape_id");
            while (it.hasNext()) {
                Map.Entry<String, ArrayList<Trip>> tripss = it.next();
                for (Trip t : tripss.getValue()) {
                    writer.write("\n" + t.toString());
                }

            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Trip file could not be found");
        }

        return tripFile;
    }

    public File exportStopTimes(Path path) {
        File stopTimeFile = new File(path + "/stop_times.txt");
        try (FileWriter writer = new FileWriter(stopTimeFile)) {
            writer.write("trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type");
            Set<String> keys = tripsList.keySet();
            for (String key : keys) {
                ArrayList<Trip> tripss = tripsList.get(key);
                for (Trip t : tripss) {
                    for (Map.Entry<String, ArrayList<StopTime>> stops : t.getStopTimes().entrySet()) {
                        ArrayList<StopTime> stopList = stops.getValue();
                        for (StopTime stop : stopList) {
                            writer.write("\n" + stop.toString());
                        }

                    }
                }

            }

        } catch (IOException e) {
            System.out.println("stopTime file could not be found");
        }
        return stopTimeFile;
    }

    /**
     * Imports the GTFS files and calls helper methods to populate entity objects
     *
     * @param listOfFiles directories of the files to import
     */
    public boolean importFiles(ArrayList<File> listOfFiles) {
        List<File> routeFile = listOfFiles.stream()
                .filter(file -> file.getName().equals("routes.txt"))
                .toList();

        List<File> stopFile = listOfFiles.stream()
                .filter(file -> file.getName().equals("stops.txt"))
                .toList();

        List<File> tripFile = listOfFiles.stream()
                .filter(file -> file.getName().equals("trips.txt"))
                .toList();

        List<File> stopTimesFile = listOfFiles.stream()
                .filter(file -> file.getName().equals("stop_times.txt"))
                .toList();


        if (routeFile.size() > 0 && stopFile.size() > 0 && tripFile.size() > 0 && stopTimesFile.size() > 0) {
            int incorrectLines = 0;
            try {
                FXMLLoader importLoader = new FXMLLoader();
                Parent importRoot = importLoader.load(Objects.requireNonNull(getClass()
                        .getResource("ImportingFilesDisplay.fxml")).openStream());
                Stage importStage = new Stage();
                importStage.setTitle("Importing...");
                importStage.setScene(new Scene(importRoot));
                importStage.show();

                incorrectLines += importFilesNoStage(listOfFiles);

                importStage.hide();


                if (incorrectLines > 0) {
                    errorAlert("Success, But Incorrectly Formatted Lines",
                            incorrectLines + " incorrectly formatted lines were skipped.");
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    Image image = new Image("https://img.icons8.com/fluency/48/000000/checked.png");
                    ImageView imageView = new ImageView(image);
                    alert.setGraphic(imageView);
                    alert.setTitle("Successful Import");
                    alert.setHeaderText("Import Successful");
                    alert.setContentText("All files were imported successfully");
                    alert.showAndWait();
                }
                importAllStopsToAllRoutes();
                return true;
            } catch (InvalidHeaderException e) {
                errorAlert("Invalid Header", "The header for " + e.filename +
                        " is formatted incorrectly. No files were imported");
            } catch (IOException e) {
                errorAlert("File not found", "A required file was not found");
            }
        } else {
            errorAlert("All four files must be imported at the same time", "Accepted filenames: routes.txt, stops.txt, trips.txt, stop_times.txt");
        }
        return false;
    }

    /**
     * Populates the StopTimes in each Trip
     *
     * @param stopTimesFile the File to read from
     * @return the number of incorrectly formatted lines
     * @throws IOException            if there is a problem reading the file
     * @throws InvalidHeaderException if the header is not formatted correctly
     * @author Ian Czerkis
     */
    private int importStopTimes(File stopTimesFile) throws IOException, InvalidHeaderException {
        int invalidLines = 0;
        try (Stream<String> lines = Files.lines(stopTimesFile.toPath())) {
            Iterator<String> it = lines.iterator();
            String firstLine = it.next();
            if (validateFirstStopTimeLine(firstLine)) {
                while (it.hasNext()) {
                    StopTime stopTime = validateStopTimeLine(it.next());
                    if (!Objects.equals(stopTime, null)) {
                        ArrayList<Trip> tripss = tripsList.get(stopTime.getTripID());
                        if (tripss != null) {
                            for (Trip t : tripss) {
                                if (t.getTripID().equals(stopTime.getTripID())) {
                                    t.addStopTime(stopTime.getStopID(), stopTime);
                                }
                            }
                        }

                    } else {
                        invalidLines++;
                    }
                }
            } else {
                throw new InvalidHeaderException("Invalid Header Encountered", "stop_times.txt");
            }
        }
        return invalidLines;
    }

    /**
     * creates a StopTime from a single line in the StopTime file
     *
     * @param line the line to parse
     * @return the StopTime object if the file is valid or null if the file is invalid
     * @author Ian Czerkis
     */
    public static StopTime validateStopTimeLine(String line) {
        StopTime stopTime;
        try {
            CSVReader reader = new CSVReader(line);
            stopTime = new StopTime(
                    reader.next(), reader.next(), reader.next(),
                    reader.next(), reader.next(), reader.next(),
                    reader.next(), reader.next());

            reader.checkEndOfLine();
            stopTime.checkRequired();
        } catch (CSVReader.EndOfStringException | CSVReader.MissingRequiredFieldException
                | NumberFormatException | ParseException e) {
            return null;
        }
        return stopTime;
    }

    /**
     * validates the first line of the StopTime file
     *
     * @param firstLine the line to parse
     * @return True if the line is valid False if it is invalid
     * @author Ian Czerkis
     */
    public static boolean validateFirstStopTimeLine(String firstLine) {
        return firstLine.equals("trip_id,arrival_time,departure_time,stop_id,stop_sequence," +
                "stop_headsign,pickup_type,drop_off_type");
    }

    /**
     * Populates the Trips
     *
     * @param tripFile the file to import
     * @return the number of incorrectly formatted lines
     * @throws IOException            if there is a problem reading the file
     * @throws InvalidHeaderException if the header is not formatted correctly
     */
    private int importTrips(File tripFile) throws IOException, InvalidHeaderException {
        int invalidLines = 0;
        try (Stream<String> lines = Files.lines(tripFile.toPath())) {
            Iterator<String> it = lines.iterator();
            String firstLine = it.next();
            if (!validateTripHeader(firstLine)) {
                throw new InvalidHeaderException("Invalid header encountered", "trips.txt");
            }
            while (it.hasNext()) {
                String tripLine = it.next();
                Trip trip = validateTripLines(tripLine);
                // Add trip to corresponding route
                if (!Objects.equals(null, trip)) {
                    if (routesList.containsKey(trip.getRouteID())) {
                        ArrayList<Route> routes = routesList.get(trip.getRouteID());
                        for (Route r : routes) {
                            if (r.getRouteID().equals(trip.getRouteID())) {
                                r.addTrip(trip.getTripID(), trip);
                            }
                        }
                        addTrip(trip.getTripID(), trip);


                    } else {
                        invalidLines++;
                    }
                }
            }
        }
        return invalidLines;
    }

    public int importFilesNoStage(ArrayList<File> listOfFiles) throws IOException, InvalidHeaderException {
        int ret = 0;

        ret += importRoutes(listOfFiles.stream()
                .filter(file -> file.getName().equals("routes.txt"))
                .toList().get(0));
        ret += importStops(listOfFiles.stream()
                .filter(file -> file.getName().equals("stops.txt"))
                .toList().get(0));
        ret += importTrips(listOfFiles.stream()
                .filter(file -> file.getName().equals("trips.txt"))
                .toList().get(0));
        ret += importStopTimes(listOfFiles.stream()
                .filter(file -> file.getName().equals("stop_times.txt"))
                .toList().get(0));

        return ret;
    }


    /**
     * Checks if Trip header is valid against known valid header.
     *
     * @param header trip header
     * @return boolean
     * @Author Matt Wehman
     */
    public static boolean validateTripHeader(String header) {
        return header.equals("route_id,service_id,trip_id,trip_headsign,direction_id,block_id,shape_id");
    }

    /**
     * Validates each line of trip file.
     *
     * @param tripLine
     * @return trip of no exceptions are thrown and null if line is invalid
     * @Author Matthew Wehman
     */
    public static Trip validateTripLines(String tripLine) {
        try {
            CSVReader reader = new CSVReader(tripLine);
            Trip trip = new Trip(
                    reader.next(), reader.next(), reader.next(),
                    reader.next(), reader.next(), reader.next(),
                    reader.next());
            reader.checkEndOfLine();
            trip.checkRequired();
            return trip;
        } catch (CSVReader.EndOfStringException | CSVReader.MissingRequiredFieldException | NumberFormatException e) {
            return null;
        }
    }


    /**
     * Populates the Stops
     *
     * @param stopFile the file to parse
     * @return the number of incorrectly formatted lines
     * @throws IOException            if there is a problem reading the file
     * @throws InvalidHeaderException if the header is not formatted correctly
     */
    private int importStops(File stopFile) throws IOException, InvalidHeaderException {
        int invalidLines = 0;
        try (Stream<String> lines = Files.lines(stopFile.toPath())) {
            Iterator<String> it = lines.iterator();
            String firstLine = it.next();
            if (!validateStopHeader(firstLine)) {
                throw new InvalidHeaderException("Invalid header encountered", "stops.txt");
            } else {
                while (it.hasNext()) {
                    String stopLine = it.next();
                    Stop stop = validateLinesInStop(stopLine);
                    if (!Objects.equals(null, stop)) {
                        addStops(stop.getStopID(), stop);
                    } else {
                        invalidLines++;
                    }
                }
            }
        }
        return invalidLines;
    }

    /**
     * This method validates the Stop headerline and makes sure it follows the correct syntax
     *
     * @param firstLine
     * @return boolean
     * @author Patrick McDonald
     */
    public static boolean validateStopHeader(String firstLine) {
        return firstLine.equals("stop_id,stop_name,stop_desc,stop_lat,stop_lon");
    }

    /**
     * This method validates each individual Stop and makes sure it is formatted correctly
     * or else it returns a null
     *
     * @param stopLine
     * @return stop
     * @author Patrick McDonald
     */
    public static Stop validateLinesInStop(String stopLine) {
        CSVReader reader = new CSVReader(stopLine);
        Stop stop;
        try {
            String stopId = reader.next();
            String name = reader.next();
            String description = reader.next();
            double lat = reader.nextDouble();
            double lon = reader.nextDouble();
            if (lat == -1 || lon == -1 || (lat < -90.00 || lat > 90.00) ||
                    (lon < -180.00 || lon > 180.00)) {
                throw new NumberFormatException("empty");
            }

            stop = new Stop(stopId, name, description, lat, lon);
            reader.checkEndOfLine();
            stop.checkRequired();
        } catch (CSVReader.EndOfStringException | CSVReader.MissingRequiredFieldException | NumberFormatException e) {
            return null;
        }
        return stop;
    }

    /**
     * Populates the routes
     *
     * @param routeFile the file that contains the route lines
     * @return the number of incorrectly formatted lines
     * @throws IOException            if there is a problem reading the file
     * @throws InvalidHeaderException if the header is incorrectly formatted
     * @author Christian B
     */
    private int importRoutes(File routeFile) throws IOException, InvalidHeaderException {
        int incorrectLines = 0;
        try (Stream<String> lines = Files.lines(routeFile.toPath())) {
            Iterator<String> it = lines.iterator();
            String firstLine = it.next();
            if (validateRouteHeader(firstLine)) {
                while (it.hasNext()) {
                    Route route = validateRouteLine(it.next());
                    if (!Objects.equals(route, null)) {
                        addRoute(route.getRouteID(), route);
                    } else {
                        incorrectLines++;
                    }
                }
            } else {
                throw new InvalidHeaderException("Invalid header encountered", "routes.txt");
            }
        }

        return incorrectLines;
    }


    /**
     * validates that the header for the route file is formatted correctly
     *
     * @param header string header
     * @return true if header is valid, false if not
     * @author Chrstian Basso
     */
    public static boolean validateRouteHeader(String header) {
        return header.equals("route_id,agency_id,route_short_name,route_long_name," +
                "route_desc,route_type,route_url,route_color,route_text_color");
    }


    /**
     * Validates line that represents a route object
     *
     * @param line string of parameters
     * @return the object created from the parameters, or null if an exception is thrown
     * @author Christian B
     */
    public static Route validateRouteLine(String line) {
        Route route;
        CSVReader reader = new CSVReader(line);
        try {
            route = new Route(
                    reader.next(), reader.next(), reader.next(),
                    reader.next(), reader.next(), reader.next(),
                    reader.next(), reader.next(), reader.next());
            reader.checkEndOfLine();
            route.checkRequired();
        } catch (CSVReader.EndOfStringException | CSVReader.MissingRequiredFieldException | NumberFormatException e) {
            return null;
        }
        return route;
    }


    /**
     * Allows user to select multiple files
     *
     * @param actionEvent
     */
    @FXML
    public void importHelper(ActionEvent actionEvent) {
        List<File> f;
        boolean isNull;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(Paths.get("./").toFile());
        File selectedFile;
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GSTF Files", "*.txt"));
        f = fileChooser.showOpenMultipleDialog(null);
        if (f == null) {
            isNull = true;
            Alert alert = errorAlert("Null File Entered", "Can't import" +
                    "null file, only GTFS files are accepted. Please try again");
        } else {
            ArrayList<File> files = new ArrayList<>();
            for (File file : f) {
                files.add(file);
            }
            importFiles(files);
        }


    }

    /**
     * Finds the next trip at a certain stop given the time
     * This method has not been implemented
     *
     * @param stopID the stop being parsed
     * @param currentTime the current time
     */
    public String nextTripAtStop(String stopID, Time currentTime) {
        String ret = "";
        SortedMap<Time, StopTime> map = new TreeMap<>();
        TreeMap<Time, StopTime> nextDayTimes = new TreeMap<>();
        for (Map.Entry<String, ArrayList<Trip>> mapEntry : tripsList.entrySet()) {
            ArrayList<Trip> tripsList = mapEntry.getValue();
            for(Trip trip: tripsList) {
                if (trip.getStopTimes().containsKey(stopID)) {
                    ArrayList<StopTime> stopTimes = trip.getStopTimes().get(stopID);
                    for (StopTime stopTime : stopTimes) {
                        Time stopTimeArr = null;
                        if (!stopTime.getIsNextDay()) {
                            stopTimeArr = stopTime.getArrivalTime();
                            if (currentTime.compareTo(stopTimeArr) < 0) {
                                map.put(stopTimeArr, stopTime);
                            }
                        } else {
                            nextDayTimes.put(stopTime.getArrivalTime(), stopTime);
                        }
                    }

                    if (!map.isEmpty()) {
                        ret = map.get(map.firstKey()).getTripID();
                    } else if (!nextDayTimes.isEmpty()) {
                        ret = nextDayTimes.get(nextDayTimes.firstKey()).getTripID();
                    } else {
                        ret = "No more trips today";
                    }

                }
            }
        }
        return ret;
    }

    private void centerMap(){
        for(Marker m : markers){
            m.setVisible(false);
            mapView.setCenter(new Coordinate(43.0453675,-87.9109152));
            mapView.setZoom(10);
        }
    }
    /**
     * Plots the current trajectory of the bus
     * This method has not been implemented
     *
     * @param tripID the tripID to search
     * @return boolean
     */
    public boolean plotBus(String tripID) {

        ArrayList<Trip> trips = tripsList.get(tripID);
        Trip trip = null;
        for (Trip t: trips){
            if (Objects.equals(t.getTripID(), tripID)){
                trip = t;
            }
        }
        if (trip == null) {
            centerMap();
            return false;
        }
        HashMap<String, ArrayList<StopTime>> stopTimesHashMap = trip.getStopTimes();
        Iterator<Map.Entry<String, ArrayList<StopTime>>> it = stopTimesHashMap.entrySet().iterator();
        ArrayList<StopTime> stopTimes = new ArrayList<>();
        while (it.hasNext()){
            stopTimes.addAll(it.next().getValue());
        }
        if (stopTimes.size() == 0){
            centerMap();
            return false;
        }
        Time currentTime = java.sql.Time.valueOf(LocalTime.now());
        StopTime lastStopTime = null;
        StopTime nextStopTime = null;
        for (StopTime stopTime: stopTimes){
            //if the StopTime is before the current time
            if (stopTime.getArrivalTime().compareTo(currentTime) < 0){
                //if the StopTime is after the last time
                if(lastStopTime == null || stopTime.getArrivalTime().compareTo(lastStopTime.getArrivalTime()) > 0){
                    lastStopTime = stopTime;
                }
                // if the StopTime is after the current time
            } else if (stopTime.getArrivalTime().compareTo(currentTime) > 0){
                // if the StopTime is before the next time
                if(nextStopTime == null || stopTime.getArrivalTime().compareTo(nextStopTime.getArrivalTime()) < 0){
                    nextStopTime = stopTime;
                }
            }
        }
        if (lastStopTime == null || nextStopTime == null){
            centerMap();
            return false;
        }
        ArrayList<Stop> allHashedStops = new ArrayList<>();
        allHashedStops.addAll(allStopsList.get(lastStopTime.getStopID()));
        allHashedStops.addAll(allStopsList.get(nextStopTime.getStopID()));
        Stop lastStop = allHashedStops.get(0);
        Stop nextStop = allHashedStops.get(0);
        for (Stop stop: allHashedStops){
            if (Objects.equals(stop.getStopID(), lastStopTime.getStopID())){
                lastStop = stop;
            } else if (Objects.equals(stop.getStopID(), nextStopTime.getStopID())){
                nextStop = stop;
            }
        }
        double latitude = 0;
        double longitude = 0;
        // if the stop is currently in use
        if (currentTime.compareTo(lastStopTime.getArrivalTime()) > 0 && currentTime.compareTo(nextStopTime.getArrivalTime()) < 0){
            // get weighted average of coordinates
            float percentComplete = (float) (currentTime.getTime()-lastStopTime.getArrivalTime().getTime())/(nextStopTime.getArrivalTime().getTime()-lastStopTime.getArrivalTime().getTime());
            latitude = lastStop.getStopLat() + (percentComplete * (nextStop.getStopLat() - lastStop.getStopLat()));
            longitude = lastStop.getStopLong() + (percentComplete * (nextStop.getStopLong() - lastStop.getStopLong()));
            for(Marker m: markers){
                m.setVisible(false);
            }
            Coordinate coordinate = new Coordinate(latitude, longitude);
            Marker marker = new Marker(busURL, -24,-40).setPosition(coordinate).setVisible(true);
            markers.add(marker);
            mapView.addMarker(marker);
            mapView.setCenter(coordinate);
            mapView.setZoom(17);
            return true;
        } else {
            centerMap();
            return false;
        }
    }


    /**
     * Plots the stops on a given route
     * This method has not been implemented
     *
     * @param stopID
     * @return boolean
     * @author wehman
     */
    public boolean plotStop(String stopID) {
        for (Marker m : markers) {
            m.setVisible(false);
        }
        ArrayList<Stop> stop = allStopsList.get(stopID);
        for (Stop s : stop) {
            if (s.getStopID().equals(stopID)) {
                Stop stop1 = s;
                Coordinate coordinate = new Coordinate(stop1.getStopLat(), stop1.getStopLong());
                Marker marker1 = new Marker(url, -24, -40).setPosition(coordinate).setVisible(true);
                markers.add(marker1);
                mapView.addMarker(marker1);
                mapView.setCenter(coordinate);
                mapView.setZoom(17);
                return true;
            }
        }

        return false;
    }

    /**
     * searches all routes to see if they contain the specified stopID
     *
     * @param stopID the stop ID to search
     * @return ArrayList<String> the list of routeID that contain
     */
    public ArrayList<String> routesContainingStop(String stopID) {
        ArrayList<String> routesContaining = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Trip>> mapEntry : tripsList.entrySet()) {
            ArrayList<Trip> trip = mapEntry.getValue();
            for (Trip t : trip) {
                if (t.getStopTimes().containsKey(stopID)) {
                    if (!routesContaining.contains(t.getRouteID())) {
                        routesContaining.add(t.getRouteID());
                    }
                }
            }
        }
        return routesContaining;
    }

    /**
     * finds all the future trips given a routeID
     * This method has not been implemented
     *
     * @param routeID
     * @return LinkedList<Integer>
     */
    public LinkedList<Integer> tripsInFuture(int routeID) {
        return null;
    }

    /**
     * Counts the number trips that use the specified stop
     *
     * @param stopID the stopID to search for
     * @return the number of occurances of trips containing that stop
     */
    public int tripsPerStop(String stopID) {
        int counter = 0;
        for (Map.Entry<String, ArrayList<Trip>> mapEntry : tripsList.entrySet()) {
            ArrayList<Trip> trip = mapEntry.getValue();
            for (Trip t:trip) {
                if (t.getStopTimes().containsKey(stopID)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Updates all stoptimes in a trip
     * This method has not been implemented
     *
     * @param stopID
     * @return boolean
     */
    public boolean updateAllStopTimes(int stopID) {
        return false;
    }

    /**
     * Updates a group of stoptimes
     * This method has not been implemented
     *
     * @param stopTime
     * @param attribute
     * @param data
     * @return boolean
     */
    public boolean updateGroupStopTime(StopTime stopTime, String attribute, int data) {
        return false;
    }

    /**
     * Updates multiple stoptimes given an List of stoptimes
     * This method has not been implemented
     *
     * @param stopTimes
     * @return boolean
     */
    public boolean updateMultipleStopTimes(ArrayList<StopTime> stopTimes) {
        return false;
    }

    /**
     * Updates a route, certain attributes of the route may be different
     * This method has not been implemented
     *
     * @param route
     * @return boolean
     */
    public boolean updateRoute(Route route) {
        return false;
    }

    /**
     * Updates a stop, certain attributes of the stops may be different
     * This method has not been implemented
     *
     * @param stop
     * @return boolean
     */
    public boolean updateStop(Stop stop) {
        return false;
    }

    /**
     * Updates a StopTime, certain attributes of the StopTime may be different
     * This method has not been implemented
     *
     * @param stopTime
     * @return boolean
     */
    public boolean updateStopTime(StopTime stopTime) {
        return false;
    }

    /**
     * Updates a Trip, certain attributes of the Trip may be different
     * This method has not been implemented
     *
     * @param trip
     * @return boolean
     */
    public boolean updateTrip(Trip trip) {
        return false;
    }

/**
 * Returns all routes that contain a certain stop
 *
 * @param stopId
 * @return Arraylist of routes
 */

    /**
     * an exception to be thrown if
     */
    public static class InvalidHeaderException extends Exception {
        private final String filename;

        InvalidHeaderException(String message, String filename) {
            super(message);
            this.filename = filename;
        }

        public String getFilename() {
            return filename;
        }
    }

    private Alert errorAlert(String header, String context) {
        return alert(header, context, "Error", Alert.AlertType.ERROR);
    }

    private Alert infoAlert(String header, String context) {
        return alert(header, context, "Info", Alert.AlertType.WARNING);
    }

    private Alert alert(String header, String context, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
        return alert;
    }

    /**
     * Gets route id from search bar and plots stops
     * @param actionevent when button is clicked
     * @author wehman
     */
    @FXML
    public void plotStopsOnRoute(ActionEvent actionevent) {
        String routeid = searchBar.getText();
        ArrayList<Route> route = routesList.get(routeid);
        Route actualRoute = null;
        if (route == null) {
            errorAlert("Null Route", "The route id entered in the search bar cannot be found. Please enter a valid route id");
        } else {
            for (Route r : route) {
                if (r.getRouteID().equals(routeid)) {
                    actualRoute = r;
                }
            }
            getMapURL(actualRoute);
        }
    }

    /**
     * Plots all stops on a given route and adds them to the mapview
     * @param route
     * @author Wehman, Bassoc
     */

    private void getMapURL(Route route) {
        for (Marker m : markers) {
            m.setVisible(false);
        }
        HashMap<String, ArrayList<Stop>> stops = route.getStopsList();
        Set<Map.Entry<String, ArrayList<Stop>>> stopSet = stops.entrySet();
        Iterator<Map.Entry<String, ArrayList<Stop>>> it = stopSet.iterator();
        List<Coordinate> coordinates = new ArrayList<>();
        while (it.hasNext()) {
            ArrayList<Stop> stopper = it.next().getValue();
            for (Stop cur : stopper) {
                Coordinate coordinate = new Coordinate(cur.getStopLat(), cur.getStopLong());
                coordinates.add(coordinate);
                Marker marker = new Marker(this.url, -24, -40).setPosition(coordinate).setVisible(true);
                markers.add(marker);
                mapView.addMarker(marker);
            }
        }
        Extent extent = Extent.forCoordinates(coordinates);
        mapView.setExtent(extent);
    }

    /**
     * Adds all stops on a route to a route class
     * @param route
     * @author Bassoc
     */
    public void addStopsToRoute(Route route) {
        Set<Map.Entry<String, ArrayList<Trip>>> tripSet = tripsList.entrySet();
        Iterator<Map.Entry<String, ArrayList<Trip>>> it = tripSet.iterator();

        ArrayList<Trip> currentTrips = it.next().getValue();
        boolean found = false;
        Trip actualTrip = null;
        while (it.hasNext() && !found) {
            for (Trip currentTrip : currentTrips) {
                if (Objects.equals(currentTrip.getRouteID(), route.getRouteID())) {
                    found = true;
                    actualTrip = currentTrip;
                }
            }
            if (!found) {
                currentTrips = it.next().getValue();
            }
        }

        if(actualTrip != null) {
            for (Map.Entry<String, ArrayList<StopTime>> stops : actualTrip.getStopTimes().entrySet()) {
                ArrayList<StopTime> stopList = stops.getValue();
                for (StopTime stoptime : stopList) {
                    String stopTimeId = stoptime.getStopID();
                    for (Map.Entry<String, ArrayList<Stop>> stopsMap : allStopsList.entrySet()) {
                        ArrayList<Stop> s = stopsMap.getValue();
                        for (Stop sto : s) {
                            if (sto.getStopID().equals(stopTimeId)) {
                                route.addStop(sto);
                            }
                        }
                    }


                }

            }
        }
    }

    /**
     * Adds stops to all routes
     * @author Bassoc
     */
    public void importAllStopsToAllRoutes() {
        for (Map.Entry<String, ArrayList<Route>> stops : routesList.entrySet()) {
            ArrayList<Route> routeList = stops.getValue();
            for (Route route: routeList) {
                addStopsToRoute(route);
            }
        }
    }

    /**
     * called after the fxml is loaded and all objects are created. This is not called initialize any more,
     * because we need to pass in the projection before initializing.
     *
     * @param projection
     *     the projection to use in the map.
     */
    public void initMapAndControls(Projection projection) {
        mapView.setBingMapsApiKey("w1oz2x0G8Gn2cpDoyMKM~7z_7StT4ZgJ6x4zLE9oH2w~AnwrC5ThdXoU2STqcTWH_eVRdKc-ezqaFZYB41JUq4fknKQzslqc0_LJ9j0mbv0V");
        mapView.setMapType(MapType.BINGMAPS_ROAD);
        mapView.setCenter(new Coordinate(43.0453675,-87.9109152));
        mapView.setZoom(10);
        markers = new ArrayList<>();
        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            event.consume();
            Marker marker = event.getMarker();
            mapView.setCenter(marker.getPosition());
            mapView.setZoom(17);
        });
        mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());
    }


}
