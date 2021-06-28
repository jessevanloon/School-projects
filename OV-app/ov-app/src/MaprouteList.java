import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MaprouteList
{
    private ArrayList<Maproute> mapRouteList = new ArrayList<>();

    public MaprouteList(File file) throws FileNotFoundException
    {
        load(file);
    }

    public void load(File file) throws FileNotFoundException
    {
        mapRouteList.clear();
        InputStream inputStream = new FileInputStream(file);
        JSONTokener tokener = new JSONTokener(inputStream);

        while (true)
        {
            JSONObject object = new JSONObject(tokener);
            Maproute maproute = new Maproute(object);

            if (!maproute.getKey().equals("===EOF==="))
            {
                mapRouteList.add(maproute);
            } else
            {
                break;
            }
        }
    }

    public ArrayList<Maproute> getList()
    {
        return mapRouteList;
    }
}
