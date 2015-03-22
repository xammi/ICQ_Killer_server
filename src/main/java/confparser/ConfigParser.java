package confparser;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

import java.util.Map;

/**
 * Created by max on 23.03.15.
 */
public class ConfigParser extends DefaultHandler {

    private final Map<String, Section> sections;

    private Section currentSection;
    private String currentOptionName, currentOptionValue, currentOptionType;

    public ConfigParser(Map<String, Section> sections) {
        this.sections = sections;
    }

    @Override
    public void startDocument() throws SAXException {}

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException
    {
        if (qName.equals("section")) {
            currentSection = new Section(atts.getValue(0));
        }
        else if (qName.equals("option")) {
            currentOptionName = atts.getValue(0);
            currentOptionType = atts.getValue(1);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException
    {
        currentOptionValue = new String(ch, start, length);
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException
    {
        if (qName.equals("option")) {
            currentSection.addOption(currentOptionName, currentOptionValue, currentOptionType);
        }
        else if (qName.equals("section")) {
            sections.put(currentSection.getName(), currentSection);
        }
    }

    @Override
    public void endDocument() {}
}
