import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Station
{

    private String stationName;
    private String adres;
    private String ovServiceStore;
    private String bicycleStorage;
    private String bicycleRent;
    private String toilet;
    private String elevator;

    // TextArea
    JTextArea textArea2 = new JTextArea();

    public Station(String stationName, String adres, String ovServiceStore, String bicycleStorage, String bicycleRent, String toilet, String elevator)
    {
        this.stationName = stationName;
        this.adres = adres;
        this.ovServiceStore = ovServiceStore;
        this.bicycleStorage = bicycleStorage;
        this.bicycleRent = bicycleRent;
        this.toilet = toilet;
        this.elevator = elevator;
    }

    public Station(JSONObject object)
    {
        stationName = object.getString("StationName");
        adres = object.getString("Adres");
        ovServiceStore = object.getString("ovServiceStore");
        bicycleStorage = object.getString("bicycleStorage");
        bicycleRent = object.getString("bicycleRent");
        toilet = object.getString("Toilet");
        elevator = object.getString("Elevator");
    }

    public JSONObject toJson()
    {
        JSONObject object = new JSONObject();

        object.put("StationName", stationName);
        object.put("Adres", adres);
        object.put("ovServiceStore", ovServiceStore);
        object.put("bicycleStorage", bicycleStorage);
        object.put("bicycleRent", bicycleRent);
        object.put("Toilet", toilet);
        object.put("Elevator", elevator);

        return object;
    }

    public String getStationName()
    {
        return stationName;
    }

    public void printStationInfo()
    {
        System.out.println("Stationnaam: " + stationName);
        System.out.println("Adres: " + adres);
        System.out.println("OV servicepunt: " + ovServiceStore);
        System.out.println("Fietsenstalling: " + bicycleStorage);
        System.out.println("Fietsverhuur " + bicycleRent);
        System.out.println("Toilet: " + toilet);
        System.out.println("Elevator: " + elevator);
        System.out.println();
    }

    public Component makeTextArea()
    {
        textArea2.setBounds(50, 50, 300, 500);
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);
        textArea2.setBorder(BorderFactory.createBevelBorder(1));
        textArea2.setEditable(false);
        return textArea2;
    }

    public void removeAllTextArea()
    {
        textArea2.setText(null);
    }

    public String getAdres()
    {
        return adres;
    }

    public String getOvServiceStore()
    {
        return ovServiceStore;
    }

    public String getBicycleStorage()
    {
        return bicycleStorage;
    }

    public String getBicycleRent()
    {
        return bicycleRent;
    }

    public String getToilet()
    {
        return toilet;
    }

    public String getElevator()
    {
        return elevator;
    }

}
