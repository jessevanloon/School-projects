import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Instruction extends JPanel
{
    // Buttons
    private final JButton nlButton = new JButton();
    private final JButton enButton = new JButton();
    private final JButton frButton = new JButton();
    // ImageIcon
    private final ImageIcon dutchFlag = new ImageIcon("images/NL-flag.png");
    private final Image nlFlag = dutchFlag.getImage();
    private final ImageIcon englishFlag = new ImageIcon("images/UK-flag.png");
    private final Image ukFlag = englishFlag.getImage();
    private final ImageIcon frenchFlag = new ImageIcon("images/FR-flag.png");
    private final Image frFlag = frenchFlag.getImage();

    private final ImageIcon frManual = new ImageIcon("images/Handleiding FR ov-app.png");
    private final ImageIcon nlManual = new ImageIcon("images/Handleiding NL ov-app.png");
    private final ImageIcon enManual = new ImageIcon("images/Handleiding EN ov-app.png");
    // Labels
    private final JLabel pdfHyperlink = new JLabel("PDF");
    private final JLabel manualLabel = new JLabel();
    // Scrollpane
    private JScrollPane manualScrollPane = new JScrollPane(manualLabel);
    private String pdfPathname = "manuals/NL-manual.pdf";

    // Constructor
    public Instruction()
    {
        setLayout(new BorderLayout());
        manualLabel.setIcon(new ImageIcon(nlManual.getImage()));
        addToPanel();
    }

    // Adds all components to the panel
    public void addToPanel()
    {
        removeAll();
        JPanel buttonGroup = new JPanel();
        nlButton.setIcon(new ImageIcon(nlFlag.getScaledInstance(40, 20, Image.SCALE_SMOOTH)));
        enButton.setIcon(new ImageIcon(ukFlag.getScaledInstance(40, 20, Image.SCALE_SMOOTH)));
        frButton.setIcon(new ImageIcon(frFlag.getScaledInstance(40, 20, Image.SCALE_SMOOTH)));
        manualScrollPane.getVerticalScrollBar().setUnitIncrement(18);
        manualScrollPane.getVerticalScrollBar().setVisible(true);
        buttonGroup.add(nlButton);
        buttonGroup.add(enButton);
        buttonGroup.add(frButton);
        buttonGroup.add(clickableHyperlink());
        buttonGroup.add(pdfHyperlink);
        add(buttonGroup, BorderLayout.NORTH);
        add(manualScrollPane, BorderLayout.CENTER);
        actionFRbutton();
        actionENbutton();
        actionNLbutton();
    }

    // Actionlistener for when the language changes
    public void actionNLbutton()
    {
        nlButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent s)
            {
                if (s.getSource() == nlButton)
                {
                    manualLabel.setIcon(new ImageIcon(nlManual.getImage()));
                    pdfPathname = "manuals/NL-manual.pdf";
                    addToPanel();
                }
            }
        });
    }

    // Actionlistener for when the language changes
    public void actionENbutton()
    {
        enButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent p)
            {
                if (p.getSource() == enButton)
                {
                    manualLabel.setIcon(new ImageIcon(enManual.getImage()));
                    pdfPathname = "manuals/EN-manual.pdf";
                    addToPanel();
                }
            }
        });
    }

    // Actionlistener for when the language changes
    public void actionFRbutton()
    {
        frButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (e.getSource() == frButton)
                {
                    manualLabel.setIcon(new ImageIcon(frManual.getImage()));
                    pdfPathname = "manuals/FR-manual.pdf";
                    addToPanel();
                }
            }
        });
    }

    // To open the correct PDF manual after switching language
    public JLabel clickableHyperlink()
    {
        pdfHyperlink.setForeground(Color.BLUE.darker());
        pdfHyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pdfHyperlink.setForeground(Color.BLUE.darker());
        pdfHyperlink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pdfHyperlink.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                try
                {
                    Desktop.getDesktop().open(new File(pdfPathname));
                } catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }
                pdfHyperlink.repaint();
            }
        });
        return pdfHyperlink;
    }
}
