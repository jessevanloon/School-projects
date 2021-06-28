import javax.swing.*;

public class Listmodel
{
    private DefaultListModel<Route> listModel = new DefaultListModel<>();

    public Listmodel()
    {

    }

    // Every selected route gets added to history with this method
    public void addToHistory(Route route)
    {
        try
        {
            if (listModel.size() > 15)
            {
                int i = listModel.size() - 1;
                listModel.removeElementAt(i);
            }
            int i = 0;
            listModel.add(i, route);
        } catch (NullPointerException | ArrayIndexOutOfBoundsException exception)
        {
            exception.printStackTrace();
        }
    }

    public void add(int index, Route element)
    {
        listModel.add(index, element);
    }

    public Route get(int index)
    {
        return listModel.get(index);
    }

    public void remove(int index)
    {
        listModel.remove(index);
    }

    public void clear()
    {
        listModel.clear();
    }

    public DefaultListModel<Route> getListModel()
    {
        return listModel;
    }
}
