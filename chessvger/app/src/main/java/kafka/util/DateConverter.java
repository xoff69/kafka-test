package kafka.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class DateConverter {

  // Liste des formats acceptés
  private static final List<String> DATE_FORMATS = Arrays.asList(
      "yyyy",            // Format YYYY
      "yyyy.MM.dd",      // Format YYYY.MM.DD
      "dd.MM.yyyy"       // Format DD.MM.YYYY
  );

  public static Date convertToDate(String input) {
    for (String format : DATE_FORMATS) {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // Pour éviter la tolérance sur les dates invalides
        java.util.Date parsedDate = sdf.parse(input);
        return new Date(parsedDate.getTime()); // Convertir en java.sql.Date
      } catch (ParseException e) {
        // Continuer si le format actuel ne correspond pas
      }
    }
    return null; // Aucun format n'a fonctionné
  }

}
