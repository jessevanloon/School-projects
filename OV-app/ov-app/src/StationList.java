import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;

public class StationList
{
    ArrayList<Station> stationList = new ArrayList<>();

    public StationList()
    {

    }

    public void add(Station station)
    {
        stationList.add(station);
    }

    public void write()
    {
        for (var s : stationList)
        {
            s.printStationInfo();
        }
    }

    public void save(File file)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(file);
            for (var s : stationList)
            {
                fileWriter.write(s.toJson().toString() + "\n");
            }
            Station s = new Station("===EOF===", " ", " ", " ", " ", " ", "");
            fileWriter.write(s.toJson().toString());
            fileWriter.close();

        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public void load(File file)
    {
        stationList.clear();

        try
        {
            InputStream inputStream = new FileInputStream(file);
            JSONTokener tokener = new JSONTokener(inputStream);
            while (true)
            {
                JSONObject object = new JSONObject(tokener);
                Station station = new Station(object);

                if (!station.getStationName().equals("===EOF==="))
                {
                    stationList.add(station);
                } else
                {
                    break;
                }
            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
