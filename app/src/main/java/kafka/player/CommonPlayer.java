package kafka.player;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonPlayer {


  private Long id;

  private String fideId;
  private String name;
  private String country;
  private String sex;
  private String title;
  private String wTitle;
  private String oTitle;
  private String foaTitle;
  private String rating;
  private String games;
  private String k;
  private String rapidRating;
  private String rapidGames;
  private String rapidK;
  private String blitzRating;
  private String blitzGames;
  private String blitzK;
  private String birthday;
  private String flag;


}
