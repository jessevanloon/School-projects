import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class Route
{
    private String traveltime;
    private String days;
    private String lineNumber;
    private String departure;
    private String timeStampD;
    private LocalTime timeD;
    private String arrival;
    private String timeStampA;
    private LocalTime timeA;

    public Route(String days, String lineNumber, String departure, String timeStampD, LocalTime timeD)
    {
        this.days = days;
        this.lineNumber = lineNumber;
        this.departure = departure;
        this.timeStampD = timeStampD;
        this.timeD = timeD;
    }

    public Route(String days, String lineNumber, String departure, String timeStampD, LocalTime timeD, String arrival, String timeStampA, LocalTime timeA)
    {
        this.days = days;
        this.lineNumber = lineNumber;
        this.departure = departure;
        this.timeStampD = timeStampD;
        this.timeD = timeD;
        this.arrival = arrival;
        this.timeStampA = timeStampA;
        this.timeA = timeA;
    }

    public Route(String traveltime, String days, String lineNumber, String departure, String timeStampD, LocalTime timeD, String arrival, String timeStampA, LocalTime timeA)
    {
        this.traveltime = traveltime;
        this.days = days;
        this.lineNumber = lineNumber;
        this.departure = departure;
        this.timeStampD = timeStampD;
        this.timeD = timeD;
        this.arrival = arrival;
        this.timeStampA = timeStampA;
        this.timeA = timeA;
    }

    public Route(JSONObject object)
    {
        try
        {
            days = object.getString("days");
            lineNumber = object.getString("lineNumber");
            departure = object.getString("station");
            timeStampD = object.getString("depArr");
            String timeDstring = object.getString("time");
            timeD = LocalTime.parse(timeDstring, DateTimeFormatter.ofPattern("HHmm"));
        } catch (DateTimeParseException dateTimeParseException)
        {
            System.out.print("");
        }
    }

    public JSONObject toJSON()
    {
        JSONObject object = new JSONObject();

        object.put("traveltime", traveltime).put("lineNumber", lineNumber).put("departure", departure).put("arrival", arrival).put("timeStampD", timeStampD).put("timeStampA", timeStampA).put("timeD", timeD).put("timeA", timeA).put("days", days);
        return object;
    }

    public String getDays()
    {
        return days;
    }

    public String getLineNumber()
    {
        return lineNumber;
    }

    public String getDepartureStation()
    {
        return departure;
    }

    public String getTimeStampD()
    {
        return timeStampD;
    }

    public LocalTime getTimeD()
    {
        return timeD;
    }

    public String getArrivalStation()
    {
        return arrival;
    }

    public String getTimeStampA()
    {
        return timeStampA;
    }

    public LocalTime getTimeA()
    {
        return timeA;
    }

    public String getTravelTime()
    {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        long diff = 0;
        try
        {
            Date date1 = format.parse(String.valueOf(timeD));
            Date date2 = format.parse(String.valueOf(timeA));
            diff = date2.getTime() - date1.getTime();
        } catch (ParseException parseException)
        {
            parseException.printStackTrace();
        }

        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        String s = (diffHours + "h" + " " + diffMinutes + "m");
        if (!((diffMinutes < 0) || (diffHours < 0)))
        {
            return s;
        } else
        {
            return "Negative";
        }
    }

    public void setTraveltime(String traveltime)
    {
        getTravelTime();
        this.traveltime = traveltime;
    }

    public void write()
    {
        System.out.printf("%-8s%-10s%-20s%-2s%-8s%n", days, lineNumber, departure, timeStampD, timeD);
    }

    //@Override
    public String toString()
    {
        return String.format("%-8s%-10s%-2s%-8s%-30s%-2s%-8s%-30s%n", traveltime, lineNumber, timeStampD, timeD, departure, timeStampA, timeA, arrival);
    }

}
