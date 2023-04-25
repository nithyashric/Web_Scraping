package com.tarladalal.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.*;

//import ch.qos.logback.classic.BasicConfigurator;

public class MyLogger {

	static final Logger logger = LogManager.getLogger(MyLogger.class.getName());
	public static void debug(String str)
	{
		logger.info(str);
	}
	public static void info(String str)
	{
		logger.info(str);
	}
	public static void warning(String str)
	{
		logger.warn(str);
	}
	public static void error(String str)
	{
		logger.error(str);
	}
	public static void fatal(String str)
	{
		logger.fatal(str);
	}
  /*  public static void main(String[] args) {
        logger.debug("Sample debug message");
        logger.info("Sample info message");
        logger.warn("Sample warn message");
        logger.error("Sample error message");
        logger.fatal("Sample fatal message");
    }*/
    
}
