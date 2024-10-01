package kafka.player;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PlayersListHandler extends DefaultHandler {

  private List<CommonPlayer> players = null;
  private CommonPlayer currentPlayer = null;
  private StringBuilder data = null;

  public List<CommonPlayer> getPlayers() {
    return players;
  }

  boolean isPlayer = false;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (qName.equalsIgnoreCase("player")) {
      isPlayer = true;
      currentPlayer = new CommonPlayer();
      if (players == null) {
        players = new ArrayList<>();
      }
    }
    data = new StringBuilder();
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if (isPlayer) {
      switch (qName.toLowerCase()) {
        case "fideid":
          currentPlayer.setFideId(data.toString());
          break;
        case "name":
          currentPlayer.setName(data.toString());
          break;
        case "country":
          currentPlayer.setCountry(data.toString());
          break;
        case "sex":
          currentPlayer.setSex(data.toString());
          break;
        case "title":
          currentPlayer.setTitle(data.toString());
          break;
        case "w_title":
          currentPlayer.setWTitle(data.toString());
          break;
        case "o_title":
          currentPlayer.setOTitle(data.toString());
          break;
        case "foa_title":
          currentPlayer.setFoaTitle(data.toString());
          break;
        case "rating":
          currentPlayer.setRating((data.toString()));
          break;
        case "games":
          currentPlayer.setGames((data.toString()));
          break;
        case "k":
          currentPlayer.setK((data.toString()));
          break;
        case "rapid_rating":
          currentPlayer.setRapidRating((data.toString()));
          break;
        case "rapid_games":
          currentPlayer.setRapidGames((data.toString()));
          break;
        case "rapid_k":
          currentPlayer.setRapidK((data.toString()));
          break;
        case "blitz_rating":
          currentPlayer.setBlitzRating((data.toString()));
          break;
        case "blitz_games":
          currentPlayer.setBlitzGames((data.toString()));
          break;
        case "blitz_k":
          currentPlayer.setBlitzK((data.toString()));
          break;
        case "birthday":
          currentPlayer.setBirthday((data.toString()));
          break;
        case "flag":
          currentPlayer.setFlag(data.toString());
          break;
        case "player":
          players.add(currentPlayer);
          break;
      }
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    data.append(new String(ch, start, length));
  }
}
