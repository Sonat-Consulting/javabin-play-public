import play.Application;
import play.GlobalSettings;
import play.Logger;

/**
 * @author sondre
 */
public class Global extends GlobalSettings{
    @Override
    public void onStart(Application app) {
        Logger.info("Starting application");
    }
}
