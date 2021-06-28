import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


public class RouteList
{
   private final ArrayList<Route> routeList = new ArrayList<>();

   public RouteList()
   {
   }

   public void add(Route station)
   {
      routeList.add(station);
   }

   public void add(Route station, RouteList searchList)
   {
      searchList.add(station);
   }

   public int size()
   {
      return this.routeList.size();
   }

   public Route get(int i)
   {
      return this.routeList.get(i);
   }

   public void clear()
   {
      this.routeList.clear();
   }

   public ArrayList<Route> load(File file) throws FileNotFoundException, DateTimeParseException
   {
      routeList.clear();
      InputStream inputStream = new FileInputStream(file);
      JSONTokener tokener = new JSONTokener(inputStream);

      while (true)
      {
         JSONObject object = new JSONObject(tokener);
         Route route = new Route(object);

         if (!route.getDays().equals("===EOF==="))
         {
            routeList.add(route);
         }
         else
         {
            break;
         }
      }

      return routeList;
   }

   public ArrayList<Route> loadFavorites(File file) throws FileNotFoundException, DateTimeParseException
   {
      routeList.clear();
      InputStream inputStream = new FileInputStream(file);
      JSONTokener tokener = new JSONTokener(inputStream);

      while (true)
      {
         JSONObject object = new JSONObject(tokener);
         Route route = new Route(object.getString("traveltime"), object.getString("days"), object.getString("lineNumber"),
                 object.getString("departure"), object.getString("timeStampD"), LocalTime.parse(object.getString("timeD"), DateTimeFormatter.ofPattern("HH:mm")),
                 object.getString("arrival"), object.getString("timeStampA"), LocalTime.parse(object.getString("timeA"), DateTimeFormatter.ofPattern("HH:mm")));

         if (!route.getDays().equals("===EOF==="))
         {
            routeList.add(route);
         }
         else
         {
            break;
         }
      }

      return routeList;
   }

   public void save(File file)
   {
      try
      {
         FileWriter fileWriter = new FileWriter(file);
         for (var s : routeList)
         {
            fileWriter.write(s.toJSON().toString() + "\n");
         }
         Route s = new Route(" ", "===EOF===", " ", " ", " ", LocalTime.parse("1200", DateTimeFormatter.ofPattern("HHmm")), " ", " ", LocalTime.parse("1300", DateTimeFormatter.ofPattern("HHmm")));
         fileWriter.write(s.toJSON().toString());
         fileWriter.close();

      } catch (IOException ioException)
      {
         ioException.printStackTrace();
      }
   }

   public ArrayList<Route> getRouteList()
   {
      return routeList;
   }

   public void write()
   {
      for (Route route : routeList)
      {
         route.write();
      }
   }
}
