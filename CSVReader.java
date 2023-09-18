import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 * This class handles the calls from the import methods in the controller
 * @author czerkisi
 * @version 1.0
 * @created 05-Oct-2022 12:59:52 PM
 */
public class CSVReader {
    String line;
    boolean hasNext = true;
    boolean hasRun = false;

    CSVReader(String line){
        this.line = line;
    }

    /**
     * Gets the next parameter after ','
     * @return String
     * @throws EndOfStringException
     */
    public String next() throws EndOfStringException {
        // if the line is in quotation marks
        if (line.length() > 0 && line.charAt(0) == '"'){
            line = line.substring(1);
            int index = line.indexOf('"');
            String ret = line.substring(0, index);
            line = line.substring(index + 2);
            return "\"" + ret + "\"";
        }
        String ret;
        if (line.contains(",")){
            ret = line.substring(0, line.indexOf(','));
            line = line.substring(line.indexOf(',')+1);
        } else {
            if(hasNext){
                ret = line;
                line = "";
                hasNext = false;
            } else {
                throw new EndOfStringException("There is no more text to read");
            }
        }
        return ret;
    }

    /**
     * Gets the next Int
     * @return int
     * @throws EndOfStringException
     * @throws NumberFormatException
     */
    public int nextInt() throws EndOfStringException, NumberFormatException {
        String ret = next();
        return ret.length() > 0 ? Integer.parseInt(ret): -1;
    }

    /**
     * Gets the next Time
     * @return Time
     * @throws EndOfStringException
     * @throws ParseException
     */
    public Time nextTime() throws EndOfStringException, ParseException {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return new Time(formatter.parse(next()).getTime());
    }

    /**
     * gets the next long
     * @return long
     * @throws EndOfStringException
     */
    public long nextLong() throws EndOfStringException {
        String ret = next();
        return ret.length() > 0 ? Long.parseLong(ret): -1;
    }

    /**
     * Gets the nextDouble
     * @return double
     * @throws EndOfStringException
     */
    public double nextDouble() throws EndOfStringException {
        String ret = next();
        return ret.length() > 0 ? Double.parseDouble(ret): -1;
    }

    public void checkEndOfLine() throws EndOfStringException {
        if (line.length()>0){
            throw new EndOfStringException("The line is too long: " + line);
        }
    }

    /**
     * if the end of the String is reached before parsing is finished
     */
    public class EndOfStringException extends Exception {
        /**
         * EndOfStringException
         * @param errorMessage
         */
        public EndOfStringException(String errorMessage){
            super(errorMessage);
        }
    }

    /**
     * if a required field is missing
     */
    public static class MissingRequiredFieldException extends Exception {
        /**
         * EndOfStringException
         * @param errorMessage
         */
        public MissingRequiredFieldException(String errorMessage){
            super(errorMessage);
        }
    }
}
