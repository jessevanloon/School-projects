import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageChoice
{
    static String language = "nl";
    static Locale locale = Locale.forLanguageTag(language);
    static ResourceBundle current = ResourceBundle.getBundle("Bundle", locale);
    static TitledBorder languageBorder;

    static JButton dutchFlagButton = new JButton();
    static JButton ukFlagButton = new JButton();
    static JButton frFlagButton = new JButton();

    public LanguageChoice()
    {
        languageBorder = BorderFactory.createTitledBorder(getCurrent().getString("language"));
    }

    public static ResourceBundle getCurrent()
    {
        return LanguageChoice.current;
    }

    public static void setCurrent(ResourceBundle current)
    {
        LanguageChoice.current = current;
    }

    public static JPanel languagePanel()
    {
        //NL flag image button
        ImageIcon dutchFlag = new ImageIcon("images/NL-flag.png");
        Image NL = dutchFlag.getImage();
        dutchFlagButton.setIcon(new ImageIcon(NL.getScaledInstance(40, 20, Image.SCALE_SMOOTH)));

        //UK flag image button
        ImageIcon ukFlag = new ImageIcon("images/UK-flag.png");
        Image UK = ukFlag.getImage();
        ukFlagButton.setIcon(new ImageIcon(UK.getScaledInstance(40, 20, Image.SCALE_SMOOTH)));

        //FR flag image button
        ImageIcon frFlag = new ImageIcon("images/FR-flag.png");
        Image FR = frFlag.getImage();
        frFlagButton.setIcon(new ImageIcon(FR.getScaledInstance(40, 20, Image.SCALE_SMOOTH)));

        //add buttons to panel
        JPanel languagePanel = new JPanel();
        languagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        languagePanel.setBorder(languageBorder);
        languagePanel.add(dutchFlagButton);
        languagePanel.add(ukFlagButton);
        languagePanel.add(frFlagButton);

        return languagePanel;
    }
}
