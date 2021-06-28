import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swing.BrowserView;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

public class MapBrowser
{
    private String url;
    private final Engine engine = Engine.newInstance(EngineOptions.newBuilder(HARDWARE_ACCELERATED).licenseKey("1BNDHFSC1FZAHMLACFOV55N9D3S6WRPA9Y0VM7FOOMZTSPQGUPLQWCXQX8IJQWPVXGGQGL").build());
    private final Browser browser = engine.newBrowser();
    private final BrowserView view;

    public MapBrowser()
    {
        view = BrowserView.newInstance(browser);
    }

    public void load(String url)
    {
        view.getBrowser().navigation().loadUrl(url);
    }

    public Engine getEngine()
    {
        return engine;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public BrowserView getView()
    {
        return view;
    }

    public Browser getBrowser()
    {
        return browser;
    }
}
