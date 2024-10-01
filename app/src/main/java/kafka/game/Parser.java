package kafka.game;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;


public class Parser {

  public Parser() {

  }


  public String readPgnFile(String emplacement) {
    StringBuilder result = new StringBuilder();
    try {
      // FIXME on fait qu un fichier et pas tout le repertoire
     System.out.println("pgn file: " + emplacement);
      List<String> lines = Files.readAllLines(Paths.get(emplacement), StandardCharsets.ISO_8859_1);
      lines.forEach(ligne -> {
        ligne = ligne.replace("\ufeff", ""); // ajout pour palier bug BOM
        ligne = ligne.replace("0.", "0. ");
        ligne = ligne.replace("1.", "1. ");
        ligne = ligne.replace("2.", "2. ");
        ligne = ligne.replace("3.", "3. ");
        ligne = ligne.replace("4.", "4. ");
        ligne = ligne.replace("5.", "5. ");
        ligne = ligne.replace("6.", "6. ");
        ligne = ligne.replace("7.", "7. ");
        ligne = ligne.replace("8.", "8. ");
        ligne = ligne.replace("9.", "9. ");
        ligne = ligne.replace("  ", " "); // fichier pgn lichess
        result.append(ligne).append(" ");
      });
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return result.toString();
  }
  public static int interpreteValue(String s) {

    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return 0;
    }
  }
  public List<CommonGame>  parseData(String data,  String fileName) {
    System.out.println("parse data  " + fileName);
    int etat = 0;
    int len = data.length();
    int compteur = 0;
    int nbgame = 0;
List<CommonGame> games=new ArrayList<>();
    CommonGame game = new CommonGame();
    boolean toAdd = true;
    // TODO game.getMetaCommonGame().setSource(fileName);
    while (compteur < len) {
      switch (data.charAt(compteur)) {
        case '[':
          etat = 1;
          StringBuilder sbt = new StringBuilder();
          for (int j = compteur + 1; j < data.length() - 1; j++) {
            if (data.charAt(j) == ' ' && data.charAt(j + 1) == '"') {
              break;
            }
            sbt.append(data.charAt(j));
          }
          String token = sbt.toString();
          StringBuilder sbtvalue = new StringBuilder();
          int deb = compteur + 1 + token.length() + 2;
          for (int j = deb; j < data.length() - 1; j++) {
            if (data.charAt(j) == '"' && data.charAt(j + 1) == ']') {
              break;
            }
            sbtvalue.append(data.charAt(j));
          }
          String value = sbtvalue.toString();
          //  log.info("value="+value);
          switch (token) {
            case "Event":
              game.setEvent(value);
              break;
            case "Site":
              game.setSite(value);
              break;
            case "Date":
              game.setDate(new Date(System.currentTimeMillis()));
              break;
            case "EventDate":
              game.setEventDate(new Date(System.currentTimeMillis()));
              break;
            case "Round":
              game.setRound(value);
              break;
            case "WhiteTitle":
              game.setWhiteTitle(value);
              break;
            case "BlackTitle":
              game.setBlackTitle(value);
              break;
            case "WhiteElo":
              game.setWhiteElo(interpreteValue(value));
              break;
            case "BlackElo":
              game.setBlackElo(interpreteValue(value));
              break;
            case "ECO":
              game.setEco(value);
              break;
            case "Opening":
              game.setOpening(value);
              break;
            case "WhiteFideId":
              game.setWhiteFideId(Long.parseLong(value));
              break;
            case "BlackFideId":
              game.setBlackFideId(Long.parseLong(value));
              break;
            case "Result":
              game.setResult(value);
              break;
            case "White":

              game.setWhiteFideId(0L);
              break;
            case "Black":

              game.setBlackFideId(0L);
              break;
            case "Variant":
              if ("standard".equalsIgnoreCase(value)) {
                // cas lichess
              }
              break;
            case "Variation":
            case "EventType":
            case "BlackTeam":
            case "WhiteTeam":
            case "BlackTeamCountry":
            case "WhiteTeamCountry":
            case "EventCountry":
            case "EventCategory":
            case "PlyCount":
            case "EventRounds":  // FIXME mettre tout ca en meta info
              break;
            default:
              System.out.println("Parser switch " + token + ":'" + value);
          }
          break;
        //
        case ']':
          if (etat == 1) {
            etat = 2;
          }
          break;
        case '1':
          // on fait sauter les parties sans coup
          if (etat == 2 && data.charAt(compteur + 1) == '.') {
            StringBuilder sbmoves = new StringBuilder();

            //         log.info("resultat=" + resultat);
            for (int debut = compteur; debut < len; debut++) {
              boolean isResultat = true;
              for (int i = 0; i < game.getResult().length(); i++) {
                if (game.getResult().charAt(i) != data.charAt(debut + i)) {
                  isResultat = false;
                  break;
                }
              }
              if (isResultat) {
                break;
              }
              sbmoves.append(data.charAt(debut));
            }
            String coups = sbmoves.toString();
            // log.info("moves=" + coups);
            // on positionnera le game ID dans le game DB à l'ajout du additonnal
            game.setMoves(coups);
            //game.getMetaCommonGame().setSource(fileName);
            int debut = coups.indexOf("1. ") + 3;
            //  log.info(fileName+"***moves=" + coups);
            // log.info(game+"***moves=" + coups);
            coups = coups.substring(debut);
            /// dans certains cas on a encore un espace au debut
            coups = coups.replaceAll("^\\s+", "");
            // TODO gerer les commentaires bien sur
            int fin = coups.indexOf(" ");
            //     log.info("coups.substring(debut)::" + coups);
            game.setFirstMove(coups.substring(0, fin));
            int min = 0;
            int delta = game.getResult().length();
            compteur = compteur + min + delta;
            //log.info("moves=" + coups);
            etat = 0;
            if (toAdd) {
              nbgame++;

              games.add(game);

            }
            game = new CommonGame();
            toAdd = true;
            //game.getMetaCommonGame().setSource(fileName);
          }
          break;
        default:
          break;
      }
      compteur++;
    }
    return games;
  }


  public  List<CommonGame> parseDir(File dir) {
    List<CommonGame> games=new ArrayList<>();
    System.out.println("parseDir A :" + dir.getAbsolutePath());
    File[] files = dir.listFiles();
    if (files == null) {
      return games;
    }
    int total = 0;
    for (File file : files) {
      System.out.println("parseDir :" + file.getAbsolutePath());
      if (file.isDirectory()) {
        System.out.println("sous rep:" + file.getAbsolutePath());
        games.addAll(  parseDir(file));
      } else {

          games.addAll( parseData(readPgnFile(file.getAbsolutePath()), file.getName()));

      }
    }
    return games;
  }

}
