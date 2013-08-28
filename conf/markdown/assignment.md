## Gruppeoppgave
### Del 1

 1. Opprett nytt prosjekt. Velg Java-type (valg 2).
 1. Skru på minnebasert H2 JDBC datasource i `application.conf`
 1. Lag en modelklasse i "models" pakken

        public class Tweet extends Model {

            @Id
            @GeneratedValue
            public long id;

            public String text;

            public String user;

            public String time;

            public static Finder<Long, Tweet> find = new Finder<>(Long.class, Tweet.class);

        }
 1. Lag en view template som tar som input en liste av "Tweet", og viser listen som en UL`
 1. Lag en controller klasse i "controllers" pakken som arver fra play.mvc.Controller og har en statisk metode som kjører "findAll" på Tweet.
    Send resultatet av spørringen til templatet du laget
 1. Map denne statiske metoden til en HTTP sti i "conf/routes"
 1. Åpne ressursen du la til i nettleseren. Dersom alt er vel får du opp en feilside som sier at databasen trenger å oppdateres.

 ### Del 2
 1. I `app/assets/stylesheets
