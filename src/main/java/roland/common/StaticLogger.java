package roland.common;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class StaticLogger {
    private StaticLogger(){}

    public static void log(Class<?> clazz, Level level, String message){
        Logger logger = Logger.getLogger(clazz.getName());

        logger.log(level,message);
    }

    public static void log(Class<?> clazz, String message){
        Logger logger = Logger.getLogger(clazz.getName());

        logger.log(Level.INFO,message);
    }

}
