import org.json.JSONObject;

public class Maproute
{
    private String key;
    private String link;

    // Constructor to create maproute objects to open the correct link from a chosen route
    public Maproute(JSONObject object)
    {
        key = object.getString("key");
        link = object.getString("link");
    }

    public String getKey()
    {
        return key;
    }

    public String getLink()
    {
        return link;
    }
}
