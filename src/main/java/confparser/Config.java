package confparser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 22.03.15.
 */
public class Config {
    private final Map<String, Section> sections = new HashMap<>();

    private static class LazyHolder {
        private static final String configPath = "conf/config.xml";
        private static final Config INSTANCE = new Config(configPath);
    }

    public static Config getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Config(String configPath) {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser parser = factory.newSAXParser();
            ConfigParser xmlp = new ConfigParser(sections);
            parser.parse(new File(configPath), xmlp);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public int getInt(String sectionName, String optionName) {
        return Integer.parseInt(getOption(sectionName, optionName, "int"));
    }

    public String getString(String sectionName, String optionName) {
        return getOption(sectionName, optionName, "string");
    }

    public boolean getBoolean(String sectionName, String optionName) {
        return Boolean.parseBoolean(getOption(sectionName, optionName, "boolean"));
    }

    private String getOption(String sectionName, String optionName, String typeName) {
        try {
            return this.sections.get(sectionName).getOption(optionName, typeName);
        }
        catch (WrongOptionType e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
