package com.tpg.mediation.metaswitch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.tpg.mediation.metaswitch.conf.Setting;
import com.tpg.mediation.metaswitch.convertor.CfsConverter;
import com.tpg.mediation.metaswitch.convertor.Converter;
import com.tpg.mediation.metaswitch.convertor.UnzipConverter;

/**
 * @author t5381272
 *
 */
@SpringBootApplication
public class Application {

	static Setting setting;
	
	static ApplicationContext ctx;
	
	static Logger logger;
	
	static Logger display;
	
	public static void main(String[] args) {
		ctx = SpringApplication.run(Application.class, args);
		
		setting = ctx.getBean(Setting.class);
		
		logger = LoggerFactory.getLogger(Application.class);
		display = LoggerFactory.getLogger("display");
		
		display.info("Welcome to Mediation CDR Conversion Tool...");
		
		Converter cfsConverter = ctx.getBean(CfsConverter.class);
		Converter unzipConverter = ctx.getBean(UnzipConverter.class);
		
		if (args.length != 1) {
			display.info(getUsage());
		} else {
			switch (args[0]) {
			case "-e": 
				unzipConverter.convert();
				cfsConverter.convert();
				break;
			default: display.info(getUsage());
			}
		}
//		unzipConverter.convert();
//		cfsConverter.convert();
		display.info("Finishing Tool. Good Bye!");
	}
	
	private static String getUsage() {
		
		return "Usage: " + setting.getJdk() + "java -jar metaconvert-0.0.1-SNAPSHOT.jar [-e|-h]\r\n" + 
		"-e\t\tExecute conversion from XML to CSV\r\n" + 
		"-h\t\tDisplay usage of this command\r\n";
	}
	
}
