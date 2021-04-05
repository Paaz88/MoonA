package pzionit.lisenceplate;

import java.sql.Timestamp;

/**
 * VehicleData obj - includes all vehicle params.
 */
public class VehicleData {

    public static int counter = 0;

    int id;
    String url;
    String plate = "";
    Boolean decision = false;
    Timestamp timestamp;


    /**
     * Creates a new object of VehicleData
     * id - number of vehicle - counter
     *
     * @param url - image url of current plate
     */
    public VehicleData(String url) {
        this.id = counter++;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.url = url;
    }

    /**
     * @param plate - license plate number
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }

    /**
     * @param decision - the decision about parking.
     */
    public void setDecision(boolean decision) {
        this.decision = decision;
    }

    public String getPlate() {
        return this.plate;
    }

    public int getID() {
        return this.id;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public Boolean getDecision() {
        return this.decision;
    }
}