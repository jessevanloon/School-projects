import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Data
{
    private final ArrayList<String> stationList = new ArrayList<>();
    private RouteList routeList = new RouteList();
    private RouteList busRouteList = new RouteList();
    private final ArrayList<Route> trainList = routeList.load(new File("data/routeList.json"));
    private final ArrayList<Route> busList = busRouteList.load(new File("data/busstations.json"));

    public Data() throws FileNotFoundException
    {
        routeList = loadFullData();
    }

    // Creates Route objects from the JSON files and puts them into a list
    public RouteList loadFullData()
    {
        ArrayList<Route> completeList = new ArrayList<>();

        for (Route value : trainList)
        {
            Route route;
            if (value.getTimeStampD().equals("O"))
            {
                route = new Route(value.getDays(), value.getLineNumber(), value.getDepartureStation(), "A", value.getTimeD());
                completeList.add(route);
                route = new Route(value.getDays(), value.getLineNumber(), value.getDepartureStation(), "V", value.getTimeD());
            } else
            {
                route = new Route(value.getDays(), value.getLineNumber(), value.getDepartureStation(), value.getTimeStampD(), value.getTimeD());
            }
            completeList.add(route);
        }

        for (Route value : busList)
        {
            Route route;
            if (value.getTimeStampD().equals("O"))
            {
                route = new Route(value.getDays(), value.getLineNumber(), value.getDepartureStation(), "A", value.getTimeD());
                completeList.add(route);
                route = new Route(value.getDays(), value.getLineNumber(), value.getDepartureStation(), "V", value.getTimeD());
            } else
            {
                route = new Route(value.getDays(), value.getLineNumber(), value.getDepartureStation(), value.getTimeStampD(), value.getTimeD());
            }
            completeList.add(route);
        }

        RouteList routeList = new RouteList();
        for (Route route : completeList)
        {
            routeList.add(route);
        }

        return routeList;
    }

    // Loads all trainstations for the comboboxes
    public String[] loadTrainStationList()
    {
        Set<String> set = new LinkedHashSet<>();
        for (var s : routeList.getRouteList())
        {
            if (s.getLineNumber().contains("IC"))
            {
                set.add(s.getDepartureStation());
            }
        }

        return set.toArray(new String[stationList.size()]);
    }

    // Loads all busstations for the comboboxes
    public String[] loadBusStationList()
    {
        Set<String> set = new LinkedHashSet<>();
        for (var s : routeList.getRouteList())
        {
            if (!s.getLineNumber().contains("IC"))
            {
                set.add(s.getDepartureStation());
            }
        }

        return set.toArray(new String[stationList.size()]);
    }

    // Creates a list of all possible routes with the correct departure and arrival Station (Called in gui.outputListmodel)
    public RouteList inputList(String departure, String arrival)
    {
        RouteList completeList = loadFullData();
        RouteList routeList = new RouteList();

        for (int i = 0; i < completeList.size(); i++) //loop through full routeList
        {
//find departure station that is equal to given departure (and making sure it is a departure route for the correct time)
            if ((completeList.get(i).getDepartureStation().equals(departure)) && ((completeList.get(i).getTimeStampD().equals("V"))))
            {
                for (int j = 0; j < completeList.size(); j++) //loop through full routeList
                {
//find arrival station that is equal to given arrival (and making sure it is an arrival route for the correct time)
                    if ((completeList.get(j).getDepartureStation().equals(arrival)) && ((completeList.get(j).getTimeStampD().equals("A"))))
                    {
//When the trainnumbers of both searches are equal they will be added together and put in a list)
                        if (completeList.get(i).getLineNumber().equals(completeList.get(j).getLineNumber()))
                        {
                            LocalTime departureTime = completeList.get(i).getTimeD();
                            LocalTime arrivalTime = completeList.get(j).getTimeD();
                            //if (arrivalTime.isAfter(departureTime))
                            {
                                Route route = new Route(completeList.get(i).getDays(), completeList.get(i).getLineNumber(), completeList.get(i).getDepartureStation(), completeList.get(i).getTimeStampD(), departureTime, completeList.get(j).getDepartureStation(), "A", arrivalTime);
                                route.setTraveltime(route.getTravelTime());
                                routeList.add(route);
                            }
                        }
                    }
                }
            }
        }
        return routeList;
    }

    // Filters all routes on the given timeframe from the inputList  (Called in outputListmodel)
    public RouteList outputList(RouteList searchList, LocalTime depTime)
    {
        RouteList searchList2 = new RouteList();

        for (int i = 0; i < searchList.size(); i++)
        {
            LocalTime depTime2 = searchList.get(i).getTimeD();

            if ((depTime2.isAfter(depTime.minusHours(1))) && (depTime2.isBefore(depTime.plusHours(1))))
            {
                Route route = new Route(searchList.get(i).getDays(), searchList.get(i).getLineNumber(), searchList.get(i).getDepartureStation(), searchList.get(i).getTimeStampD(), searchList.get(i).getTimeD(), searchList.get(i).getArrivalStation(), searchList.get(i).getTimeStampA(), searchList.get(i).getTimeA());
                searchList2.add(route, searchList2);
            }
        }
        return searchList2;
    }
}