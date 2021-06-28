import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Gui
{
    private int departureIndex;
    private int arrivalIndex;

    private final JFrame frame = new JFrame("OV-App!");
    private final Dimension dimensionStart = new Dimension(700, 400);
    private final Dimension dimension = new Dimension(700, 700);

    private final JTabbedPane tabbedPane;
    private final JPanel showRoutesPanel = new JPanel();
    private final JPanel stationinfo = new JPanel();
    private final MapBrowser browser = new MapBrowser();
    private final Data data = new Data();
    private final RouteList completeList;
    private final File jsonFileFavorites = new File("data/favorites.json");
    private final File mapRoutesJSON = new File("data/mapRoutes.json");
    private final MaprouteList mapRouteList = new MaprouteList(mapRoutesJSON);
    // Comboboxes + input
    private String[] stations = data.loadTrainStationList();
    private final String[] timeD = {"00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"};
    private JComboBox<String> comboBoxDeparture;
    private JComboBox<String> comboBoxArrival;
    private final JComboBox<String> comboBoxTime = new JComboBox<>(timeD);
    private final JComboBox<String> comboBoxStationInfo = new JComboBox<>(data.loadTrainStationList());
    // DefaultListModel & list
    private final RouteList trainnumberList = new RouteList();
    private final JList<Route> displayOutput = new JList<>();
    private final JList<Route> displayFavorites = new JList<>();
    private final JList<Route> displayHistory = new JList<>();
    private final Listmodel listModelFav = new Listmodel();
    private final Listmodel listModelHis = new Listmodel();
    private DefaultListModel<Route> outputModel = new DefaultListModel<>();
    // Labels
    private final JLabel departureLabel = new JLabel(LanguageChoice.getCurrent().getString("departureStationLabel"));
    private final JLabel arrivalLabel = new JLabel(LanguageChoice.getCurrent().getString("arrivalStationLabel"));
    private final JLabel vehicleLabel = new JLabel(LanguageChoice.getCurrent().getString("vehicleLabel"));
    private final JLabel timeLabel = new JLabel(LanguageChoice.getCurrent().getString("timeLabel"));
    private final JLabel departureTimeLabel = new JLabel(LanguageChoice.getCurrent().getString("departureTime"));
    private final JLabel arrivalTimeLabel = new JLabel(LanguageChoice.getCurrent().getString("arrivalTime"));
    private final JLabel travelTimeLabel = new JLabel(LanguageChoice.getCurrent().getString("travelTime"));
    private final JLabel travelDistance = new JLabel(LanguageChoice.getCurrent().getString("travelDistance"));
    private final JLabel departurdeTimeChar = new JLabel(LanguageChoice.getCurrent().getString("departureTimeChar"));
    private final JLabel arrivalTimeChar = new JLabel(LanguageChoice.getCurrent().getString("arrivalTimeChar"));
    private final JLabel noRouteLabel = new JLabel(LanguageChoice.getCurrent().getString("noRoute"));
    private final JLabel hyperlink = new JLabel(LanguageChoice.getCurrent().getString("viewMap"));
    // Buttons
    private final JButton addToFavorites = new JButton(LanguageChoice.getCurrent().getString("addFavorites"));
    private final JButton removeFromFavorites = new JButton(LanguageChoice.getCurrent().getString("remove"));
    private final JButton showRoutesButton = new JButton(LanguageChoice.getCurrent().getString("planTrip"));
    private final JButton planTripFavorites = new JButton(LanguageChoice.getCurrent().getString("planTrip"));
    private final JButton planTripHistory = new JButton(LanguageChoice.getCurrent().getString("planTrip"));
    private final JButton viewTrip = new JButton(LanguageChoice.getCurrent().getString("viewTrip"));
    private final JButton moreInfo = new JButton(LanguageChoice.getCurrent().getString("moreInfo"));

    private final JButton hyperlinkButton = new JButton(LanguageChoice.getCurrent().getString("viewMap"));
    private final JRadioButton train = new JRadioButton(LanguageChoice.getCurrent().getString("train"));
    private final JRadioButton bus = new JRadioButton(LanguageChoice.getCurrent().getString("bus"));
    // JTextArea
    private final JTextArea textArea = new JTextArea();
    private final TitledBorder title = BorderFactory.createTitledBorder(LanguageChoice.getCurrent().getString("traject"));
    private final TitledBorder stationInfoBorder = BorderFactory.createTitledBorder("Station info");
    private final TitledBorder language = BorderFactory.createTitledBorder(LanguageChoice.getCurrent().getString("language"));

    // Constructor
    public Gui() throws FileNotFoundException
    {
        completeList = data.loadFullData();
        loadFavorites();
        tabbedPane = tabbedPane();
        LanguageChoice.ukFlagButton.addActionListener(this::changeLanguageAction);
        LanguageChoice.dutchFlagButton.addActionListener(this::changeLanguageAction);
        LanguageChoice.frFlagButton.addActionListener(this::changeLanguageAction);
        LanguageChoice.dutchFlagButton.setSelected(true);
        saveFavorites();
    }

    // Changes language with the click of one of the language buttons (Called in constructor)
    public void changeLanguageAction(ActionEvent e)
    {
        if (e.getSource() == LanguageChoice.ukFlagButton)
        {
            LanguageChoice.setCurrent(ResourceBundle.getBundle("Bundle", Locale.forLanguageTag("en")));
        } else if (e.getSource() == LanguageChoice.dutchFlagButton)
        {
            LanguageChoice.setCurrent(ResourceBundle.getBundle("Bundle", Locale.forLanguageTag("nl")));
        } else if (e.getSource() == LanguageChoice.frFlagButton)
        {
            LanguageChoice.setCurrent(ResourceBundle.getBundle("Bundle", Locale.forLanguageTag("fr")));
        }
        tabbedPane.setTitleAt(0, LanguageChoice.getCurrent().getString("mainPage"));
        tabbedPane.setTitleAt(1, LanguageChoice.getCurrent().getString("favorites"));
        tabbedPane.setTitleAt(2, LanguageChoice.getCurrent().getString("history"));
        tabbedPane.setTitleAt(3, LanguageChoice.getCurrent().getString("instruction"));
        tabbedPane.setTitleAt(4, LanguageChoice.getCurrent().getString("dutchMap"));
        planTripHistory.setText(LanguageChoice.getCurrent().getString("planTrip"));
        planTripFavorites.setText(LanguageChoice.getCurrent().getString("planTrip"));
        noRouteLabel.setText(LanguageChoice.getCurrent().getString("noRoute"));
        viewTrip.setText(LanguageChoice.getCurrent().getString("viewTrip"));
        title.setTitle(LanguageChoice.getCurrent().getString("traject"));
        moreInfo.setText(LanguageChoice.getCurrent().getString("moreInfo"));
        train.setText(LanguageChoice.getCurrent().getString("train"));
        bus.setText(LanguageChoice.getCurrent().getString("bus"));
        stationInfoBorder.setTitle(LanguageChoice.getCurrent().getString("stationInfo"));
        language.setTitle(LanguageChoice.getCurrent().getString("language"));
        hyperlink.setText(LanguageChoice.getCurrent().getString("viewMap"));
        hyperlinkButton.setText(LanguageChoice.getCurrent().getString("viewMap"));
        setAllText();
        new Thread(() ->
        {
            moreInfo.doClick();
            frame.revalidate();
        }).start();

        frame.pack();
        frame.setVisible(true);
    }

    // Setup runnable with frame and calls contentPane (Called in Program.java)
    public void frame()
    {
        var runner = new Runnable()
        {
            public void run()
            {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Container contentPane = frame.getContentPane();
                contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
                contentPane.add(tabbedPane);
                frame.setPreferredSize(dimensionStart);
                frame.pack();
                frame.setVisible(true);
            }
        };
        EventQueue.invokeLater(runner);
    }

    //Starting components (Called in frame)
    public JTabbedPane tabbedPane()
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        try
        {
            homeTab(tabbedPane);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        favoritesTab(tabbedPane);
        historyTab(tabbedPane);
        instructionTab(tabbedPane);
        mapTab(tabbedPane);

        return tabbedPane;
    }

    // Creates the instructionTab
    public void instructionTab(JTabbedPane tabbedPane)
    {
        Instruction instruction = new Instruction();
        tabbedPane.addTab(LanguageChoice.getCurrent().getString("instruction"), instruction);
    }

    //HomeTabe for input & output panel (Called in contentPane)
    public void homeTab(JTabbedPane tabbedPane) throws FileNotFoundException
    {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.add(LanguageChoice.languagePanel());
        homePanel.add(inputPanel());
        JScrollPane scrollPane = new JScrollPane(outputPanel(showRoutesPanel));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        homePanel.add(scrollPane);
        tabbedPane.addTab((LanguageChoice.getCurrent().getString("mainPage")), homePanel);
    }

    // FavoritesTab to show added routes, remove from the list (Called in contentPane)
    public void favoritesTab(JTabbedPane tabbedPane)
    {
        JPanel favoritesPanel = favoritesPanel();
        favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));
        favoritesPanel.setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollPane = new JScrollPane(favoritesPanel);
        tabbedPane.addTab((LanguageChoice.getCurrent().getString("favorites")), scrollPane);
    }

    // Creates the hyperlink to open the map in a browser
    public JLabel clickableHyperlink(String text)
    {
        hyperlink.setForeground(Color.BLUE.darker());
        hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hyperlink.setForeground(Color.BLUE.darker());
        hyperlink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        hyperlink.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI(text));
                    hyperlink.repaint();
                } catch (IOException | URISyntaxException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
        return hyperlink;
    }

    // Searches through mapRoutes.json to find the correct URL corresponding with the chosen route
    public String getHyperlink(Route route)
    {
        String url = "";
        String departure = route.getDepartureStation();
        String arrival = route.getArrivalStation();

        for (var m : mapRouteList.getList())
        {
            String key = m.getKey();
            if (key.contains(departure) && key.contains(arrival))
            {
                url = m.getLink();
            }
        }
        return url;
    }

    // HistoryTab for all your routes you have selected (Called in contentPane)
    public void historyTab(JTabbedPane tabbedPane)
    {
        JPanel historyPanel = historyPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        tabbedPane.addTab((LanguageChoice.getCurrent().getString("history")), historyPanel);
    }

    // Shows the map of the Netherlands (Called in contentPane)
    public void mapTab(JTabbedPane tabbedPane)
    {
        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new BoxLayout(mapPanel, BoxLayout.Y_AXIS));

        browser.setUrl("https://www.google.com/maps/d/embed?mid=1onpGOFB5GvUiDbg1-63V-73ccx2AdfRV");
        browser.load("https://www.google.com/maps/d/embed?mid=1onpGOFB5GvUiDbg1-63V-73ccx2AdfRV");

        mapPanel.add(browser.getView());
        mapPanel.add(stationInfoPanel());

        tabbedPane.addTab((LanguageChoice.getCurrent().getString("dutchMap")), mapPanel);
    }

    // Get inputPanel with all panels related to get input value (Called in homeTab)
    public JPanel inputPanel()
    {
        comboBoxDeparture = new JComboBox<>(stations);
        comboBoxArrival = new JComboBox<>(stations);
        comboBoxDeparture.setSelectedIndex(0);
        comboBoxArrival.setSelectedIndex(4);
        comboBoxTime.setSelectedIndex(17);
        train.setSelected(true);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        if (train.isSelected())
        {
            addToInputPanel(inputPanel);
        }

        bus.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                inputPanel.removeAll();
                stations = data.loadBusStationList();
                repaintComboboxes();
                comboBoxArrival.setSelectedIndex(8);
                addToInputPanel(inputPanel);
            }
        });

        train.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                inputPanel.removeAll();
                stations = data.loadTrainStationList();
                repaintComboboxes();
                comboBoxArrival.setSelectedIndex(4);
                addToInputPanel(inputPanel);
            }
        });
        return inputPanel;
    }

    // Called in inputPanel(). Does as the title says
    public void repaintComboboxes()
    {
        comboBoxDeparture = new JComboBox<>(stations);
        comboBoxArrival = new JComboBox<>(stations);
        comboBoxDeparture.repaint();
        comboBoxArrival.repaint();
    }

    // Adds all buttons to the inputpanel
    public void addToInputPanel(JPanel inputPanel)
    {
        JPanel busTrainButtons = new JPanel();
        ButtonGroup buttonGroup = new ButtonGroup();
        busTrainButtons.setLayout(new GridLayout(1, 2));
        buttonGroup.add(bus);
        buttonGroup.add(train);
        busTrainButtons.add(bus);
        busTrainButtons.add(train);
        inputPanel.add(vehicleLabel);
        inputPanel.add(busTrainButtons);
        inputPanel.add(departureLabel);
        inputPanel.add(comboBoxDeparture);
        inputPanel.add(arrivalLabel);
        inputPanel.add(comboBoxArrival);
        inputPanel.add(timeLabel);
        inputPanel.add(comboBoxTime);
        inputPanel.add(addToFavorites);
        inputPanel.add(showRoutesButton);
        setAllText();

        showRoutesButton.addActionListener(e -> showRoutesButtonActionListener());
    }

    // Sets all the text when a different language has been selected
    public void setAllText()
    {
        departureLabel.setText(LanguageChoice.getCurrent().getString("departureStationLabel"));
        arrivalLabel.setText(LanguageChoice.getCurrent().getString("arrivalStationLabel"));
        vehicleLabel.setText(LanguageChoice.getCurrent().getString("vehicleLabel"));
        timeLabel.setText(LanguageChoice.getCurrent().getString("timeLabel"));
        departureTimeLabel.setText(LanguageChoice.getCurrent().getString("departureTime"));
        arrivalTimeLabel.setText(LanguageChoice.getCurrent().getString("arrivalTime"));
        travelTimeLabel.setText(LanguageChoice.getCurrent().getString("travelTime"));
        travelDistance.setText(LanguageChoice.getCurrent().getString("travelDistance"));
        departurdeTimeChar.setText(LanguageChoice.getCurrent().getString("departureTimeChar"));
        arrivalTimeChar.setText(LanguageChoice.getCurrent().getString("arrivalTimeChar"));
        noRouteLabel.setText(LanguageChoice.getCurrent().getString("noRoute"));
        // Buttons
        addToFavorites.setText(LanguageChoice.getCurrent().getString("addFavorites"));
        removeFromFavorites.setText(LanguageChoice.getCurrent().getString("remove"));
        showRoutesButton.setText(LanguageChoice.getCurrent().getString("planTrip"));
        planTripFavorites.setText(LanguageChoice.getCurrent().getString("planTrip"));
        planTripHistory.setText(LanguageChoice.getCurrent().getString("planTrip"));
        viewTrip.setText(LanguageChoice.getCurrent().getString("viewTrip"));
        moreInfo.setText(LanguageChoice.getCurrent().getString("moreInfo"));
        bus.setText(LanguageChoice.getCurrent().getString("bus"));
        train.setText(LanguageChoice.getCurrent().getString("train"));
    }

    // Creates the outputPanel when showRoutesButton is selected it fills the displayOutput with available routes (Called in homeTab)
    public JPanel outputPanel(JPanel showRoutesPanel)
    {
        showRoutesPanel.setLayout(new BoxLayout(showRoutesPanel, BoxLayout.Y_AXIS));
        displayOutput.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        displayOutputListener();
        addToFavoritesActionListener();
        frame.pack();

        return showRoutesPanel;
    }

    // Creates a DefaultListModel to show the output (Called in showRoutesActionListener)
    public DefaultListModel<Route> outputListmodel(String departure, String arrival, String timeD)
    {
        RouteList searchList = data.inputList(departure, arrival);
        DefaultListModel<Route> listModel = new DefaultListModel<>();

        StringBuilder depTimeString = new StringBuilder(timeD);
        depTimeString.deleteCharAt(2);
        LocalTime deptime = LocalTime.parse(depTimeString, DateTimeFormatter.ofPattern("HHmm"));
        RouteList searchList2 = data.outputList(searchList, deptime);

        if (searchList2.size() != 0)
        {
            for (int i = 0; i < searchList2.size(); i++)
            {
                if (!searchList2.get(i).getTravelTime().equals("Negative"))
                {
                    listModel.addElement(new Route(searchList2.get(i).getTravelTime(), searchList2.get(i).getDays(), searchList2.get(i).getLineNumber(), searchList2.get(i).getDepartureStation(), searchList2.get(i).getTimeStampD(), searchList2.get(i).getTimeD(), searchList2.get(i).getArrivalStation(), searchList2.get(i).getTimeStampA(), searchList2.get(i).getTimeA()));
                }
            }
        }

        return listModel;
    }

    // ActionListener for addToFavorites (Called in outputPanel)
    private void addToFavoritesActionListener()
    {
        addToFavorites.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    int i = 0;
                    Route route = outputListmodel(stations[comboBoxDeparture.getSelectedIndex()], stations[comboBoxArrival.getSelectedIndex()], timeD[comboBoxTime.getSelectedIndex()]).get(displayOutput.getSelectedIndex());
                    listModelFav.add(i, route);
                } catch (NullPointerException | ArrayIndexOutOfBoundsException exception)
                {
                    exception.printStackTrace();
                }
            }
        });
    }

    // ListSelectionListener for informationPanel (Called in outputPanel)
    private void displayOutputListener()
    {
        displayOutput.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent)
            {
                boolean adjust = listSelectionEvent.getValueIsAdjusting();
                if (!adjust)
                {
                    int[] selections = displayOutput.getSelectedIndices();
                    Object[] selectionValues = displayOutput.getSelectedValues();
                    for (int i = 0, n = selections.length; i < n; i++)
                    {
                        JPanel selectedRoutePanel = new JPanel(new GridLayout(2, 4));
                        Route route = (Route) selectionValues[i];
                        route.getTravelTime();
                        showRoutesPanel.removeAll();
                        selectedRoutePanel.removeAll();
                        displayOutput.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
                        JScrollPane scrollPane = new JScrollPane(displayOutput);
                        showRoutesPanel.remove(displayOutput);
                        showRoutesPanel.add(scrollPane);
                        selectedRoutePanel.add(travelDistance);
                        selectedRoutePanel.add(departureTimeLabel);
                        selectedRoutePanel.add(arrivalTimeLabel);
                        selectedRoutePanel.add(travelTimeLabel);
                        selectedRoutePanel.add(new JLabel(String.valueOf(route.getDays())));
                        selectedRoutePanel.add(new JLabel(String.valueOf(route.getTimeD())));
                        selectedRoutePanel.add(new JLabel(String.valueOf(route.getTimeA())));
                        selectedRoutePanel.add(new JLabel(String.valueOf(route.getTravelTime())));

                        showRoutesPanel.add(selectedRoutePanel);
                        showRoutesPanel.add(stationRoutePanel(route));
                        listModelHis.addToHistory(route);
                    }
                }
            }
        });
    }

    // Creates the historyPanel including the actionlistener to plan a trip from history
    public JPanel historyPanel()
    {
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));

        displayHistory.setModel(listModelHis.getListModel());
        displayHistory.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(displayHistory);
        historyPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(planTripHistory);
        historyPanel.add(buttonPanel);

        planTripHistory.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (listModelHis.getListModel().isEmpty())
                {
                    return;
                }

                Route route = listModelHis.get(displayHistory.getSelectedIndex());
                setInput(route);
            }

        });

        return historyPanel;
    }

    // FavoritesPanel to show the saved routes and remove them from the list (Called in favoritesTab)
    public JPanel favoritesPanel()
    {
        JPanel favoritesPanel = new JPanel(new GridLayout());
        displayFavorites.setModel(listModelFav.getListModel());
        displayFavorites.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(displayFavorites);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        favoritesPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(removeFromFavorites);
        buttonPanel.add(planTripFavorites);
        buttonPanel.add(viewTrip);
        favoritesPanel.add(buttonPanel);

        removeFromFavorites.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    listModelFav.remove(displayFavorites.getSelectedIndex());
                } catch (NullPointerException | ArrayIndexOutOfBoundsException exception)
                {
                    exception.printStackTrace();
                }
            }
        });

        planTripFavoritesActionListener();
        viewTrip.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (favoritesPanel.getComponentCount() > 3)
                {
                    favoritesPanel.removeAll();
                    favoritesPanel.add(scrollPane);
                    favoritesPanel.add(buttonPanel);
                    favoritesPanel.revalidate();
                }
                Route route = listModelFav.get(displayFavorites.getSelectedIndex());
                JPanel information = new JPanel();
                information.add(stationRoutePanel(route));
                favoritesPanel.add(information);
                favoritesPanel.revalidate();
            }
        });

        return favoritesPanel;
    }

    // Actionlistener to plan a trip from favorites
    public void planTripFavoritesActionListener()
    {
        planTripFavorites.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (listModelFav.getListModel().isEmpty())
                {
                    return;
                }

                Route route = listModelFav.get(displayFavorites.getSelectedIndex());
                setInput(route);
            }

        });
    }

    // Sets all input buttons corresponding to a chosen route from favorites or history
    public void setInput(Route route)
    {
        if (route.getLineNumber().contains("IC"))
        {
            stations = data.loadTrainStationList();
            train.setSelected(true);
            train.doClick();
        } else
        {
            bus.setSelected(true);
            bus.doClick();
        }

        for (int h = 0; h < stations.length; h++)
        {
            if (stations[h].equals(route.getDepartureStation()))
            {
                comboBoxDeparture.setSelectedIndex(h);
            }
            if (stations[h].equals(route.getArrivalStation()))
            {
                comboBoxArrival.setSelectedIndex(h);
            }
        }

        ArrayList<String> timeList = new ArrayList<>();
        for (int i = 0; i < timeD.length; i++)
        {
            LocalTime time = LocalTime.parse(timeD[i], DateTimeFormatter.ofPattern("HH:mm"));
            if (time.isAfter(route.getTimeD().minusHours(1)) && time.isBefore(route.getTimeA()))
            {
                if (time.getHour() == route.getTimeD().getHour())
                {
                    timeList.add(timeD[i]);
                }
            }
        }

        String time = timeList.get(0);
        for (int j = 0; j < timeD.length; j++)
        {
            if (time.equals(timeD[j]))
            {
                comboBoxTime.setSelectedIndex(j);
            }
        }
        tabbedPane.setSelectedIndex(0);
        showRoutesButtonActionListener();
    }

    // Runs when you click plan route
    public void showRoutesButtonActionListener()
    {
        outputModel.clear();
        showRoutesPanel.removeAll();
        outputModel = outputListmodel(stations[comboBoxDeparture.getSelectedIndex()], stations[comboBoxArrival.getSelectedIndex()], timeD[comboBoxTime.getSelectedIndex()]);
        displayOutput.setModel(outputModel);
        JScrollPane scrollPane = new JScrollPane(displayOutput);
        showRoutesPanel.add(scrollPane, BorderLayout.PAGE_END);
        if (outputModel.isEmpty())
        {
            showRoutesPanel.removeAll();
            showRoutesPanel.add(noRouteLabel);
        } else
        {
            showRoutesPanel.remove(noRouteLabel);
        }
        frame.setPreferredSize(dimension);
        frame.pack();
    }

    // Creates the textarea to see the vehicle, linenumber and stations
    public void setInformationTextArea(JTextArea trainInformation, String beginTrainStation, String endTrainStation)
    {
        try
        {
            if (!trainnumberList.get(0).getLineNumber().contains("IC"))
            {
                trainInformation.setText("\n" + LanguageChoice.getCurrent().getString("bus") + ": " + beginTrainStation + " - " + endTrainStation + "\n");
            } else if (trainnumberList.get(0).getLineNumber().contains("ICD"))
            {
                trainInformation.setText("\nIntercity direct: " + beginTrainStation + " - " + endTrainStation + "\n");
            } else
            {
                trainInformation.setText("Intercity: " + beginTrainStation + " - " + endTrainStation + "\n");
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException)
        {
            indexOutOfBoundsException.fillInStackTrace();
        }
    }

    // Shows specific information from a selected route from the outputListmodel (Called in informationPanel)
    public JPanel stationRoutePanel(Route route)
    {
        JPanel stationRoute = new JPanel();
        trainnumberList.clear();
        // Get all routes that belong to this trainRoute
        for (int i = 0; i < completeList.size(); i++)
        {
            if (completeList.get(i).getLineNumber().equals(route.getLineNumber()))
            {
                Route route2 = new Route(completeList.get(i).getDays(), completeList.get(i).getLineNumber(), completeList.get(i).getDepartureStation(), completeList.get(i).getTimeStampD(), completeList.get(i).getTimeD());
                trainnumberList.add(route2);
            }
        }

        String beginTrainStation = String.valueOf(trainnumberList.get(0).getDepartureStation());
        String endTrainStation = String.valueOf(trainnumberList.get((trainnumberList.size() - 1)).getDepartureStation());

        JTextArea trainInformation = new JTextArea();
        setInformationTextArea(trainInformation, beginTrainStation, endTrainStation);

        for (int i = 0; i < trainnumberList.size(); i++)
        {
            if (trainnumberList.get(i).getDepartureStation().equals(route.getDepartureStation()))
            {
                departureIndex = i;
            }

            if (trainnumberList.get(i).getDepartureStation().equals(route.getArrivalStation()))
            {
                arrivalIndex = i;
            }
        }


        for (int i = departureIndex; i <= arrivalIndex; i++)
        {
            LocalTime time = trainnumberList.get(i).getTimeD();
            String station = trainnumberList.get(i).getDepartureStation();

            if ((trainnumberList.get(i).getTimeStampD().equalsIgnoreCase("A")) && (trainnumberList.get(i).getDepartureStation().equalsIgnoreCase(route.getArrivalStation())))
            {
                trainInformation.append("\n" + time + "   " + station);
                break;
            } else if (trainnumberList.get(i).getTimeStampD().equals("V"))
            {
                trainInformation.append("\n" + time + "   " + station);
            }

        }
        JPanel informationPanel = new JPanel();
        informationPanel.setBorder(title);
        trainInformation.setEditable(false);
        informationPanel.add(trainInformation);
        stationRoute.add(informationPanel);

        stationRoute.add(clickableHyperlink(getHyperlink(route)));

        hyperlinkButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                browser.setUrl(getHyperlink(route));
                browser.load(getHyperlink(route));
                tabbedPane.setSelectedIndex(4);
                tabbedPane.getTabComponentAt(0).repaint();
            }
        });
        frame.pack();

        stationRoute.add(hyperlinkButton);

        return stationRoute;
    }

    //Shows specific information from a selected station from the stationRoutePanel (Called in informationPanel)
    public JPanel stationInfoPanel()
    {
        stationinfo.setBorder(stationInfoBorder);
        stationinfo.add(comboBoxStationInfo);
        stationinfo.add(moreInfo);
        moreInfoActionListener();

        return stationinfo;
    }

    // Actionlistener for stationinfo (on the mapTab)
    public void moreInfoActionListener()
    {
        moreInfo.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent p)
            {
                if (tabbedPane.getSelectedIndex() == 4)
                {
                    if (!browser.getUrl().equals("https://www.google.com/maps/d/embed?mid=1onpGOFB5GvUiDbg1-63V-73ccx2AdfRV"))
                    {
                        browser.setUrl("https://www.google.com/maps/d/embed?mid=1onpGOFB5GvUiDbg1-63V-73ccx2AdfRV");
                        browser.load("https://www.google.com/maps/d/embed?mid=1onpGOFB5GvUiDbg1-63V-73ccx2AdfRV");
                    }
                }
                StationList stationList = new StationList();
                File stationsInfoFile = new File("data/stationsInfo.txt");
                try
                {
                    Scanner s = new Scanner(stationsInfoFile);
                    while (s.hasNextLine())
                    {
                        Station station = new Station(s.nextLine(), s.nextLine(), s.nextLine(), s.nextLine(), s.nextLine(), s.nextLine(), s.nextLine());
                        stationList.add(station);
                        if ((data.loadTrainStationList())[comboBoxStationInfo.getSelectedIndex()].equals(station.getStationName()))
                        {
                            textArea.setText(LanguageChoice.getCurrent().getString("StationName") + " " + station.getStationName() + "\n" + LanguageChoice.getCurrent().getString("adres") + " " + station.getAdres() + "\n" + LanguageChoice.getCurrent().getString("nsPoint") + " " + station.getOvServiceStore() + "\n" + LanguageChoice.getCurrent().getString("bicycleStorage") + " " + station.getBicycleStorage() + "\n" + LanguageChoice.getCurrent().getString("bikeRental") + " " + station.getBicycleRent() + "\n" + LanguageChoice.getCurrent().getString("toilet") + " " + station.getToilet() + "\n" + LanguageChoice.getCurrent().getString("lift") + " " + station.getElevator());
                            textArea.setBounds(50, 50, 300, 500);
                            textArea.setLineWrap(true);
                            textArea.setWrapStyleWord(true);
                            textArea.setBorder(BorderFactory.createBevelBorder(1));
                            textArea.setEditable(false);
                            stationinfo.add(textArea);

                        }
                        //textArea.removeAll();
                    }
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    // When the program shuts down trips in favorites get saved to a JSON file
    public void saveFavorites()
    {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                RouteList favoriteRoutes = new RouteList();
                for (int i = 0; i < listModelFav.getListModel().size(); i++)
                {
                    Route route = listModelFav.getListModel().get(i);
                    favoriteRoutes.add(route);
                }
                favoriteRoutes.save(jsonFileFavorites);
            }
        });
    }

    // When the program starts it loads all saved favorites from the JSON file
    public void loadFavorites() throws FileNotFoundException
    {
        RouteList favoriteRoutes = new RouteList();
        for (var r : favoriteRoutes.loadFavorites(jsonFileFavorites))
        {
            listModelFav.add(0, r);
        }
    }
}