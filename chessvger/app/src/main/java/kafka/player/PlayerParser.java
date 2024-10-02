package kafka.player;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class PlayerParser {
  public  List<CommonPlayer> parse() {
    try {
      File inputFile = new File( "data/players_list_xml_foa.xml");
      //C:\home\developpement\chessvger\server\data\reference\players_list_xml_foa.xml
      //server/data/reference/players_list_xml_foa.xml
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      PlayersListHandler handler = new PlayersListHandler();
      saxParser.parse(inputFile, handler);

      System.out.println("parse players done");
      List<CommonPlayer> players = handler.getPlayers();

      return players;

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return new ArrayList<>();
  }
}