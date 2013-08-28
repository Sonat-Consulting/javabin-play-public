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
 1. Lag en view template som tar som input en liste av "Tweet", og viser listen som en `UL`
 1. Lag en controller klasse i "controllers" pakken som arver fra play.mvc.Controller og har en statisk metode som kjører "findAll" på Tweet.
    Send resultatet av spørringen til templatet du laget
 1. Map denne statiske metoden til en HTTP sti i "conf/routes"
 1. Åpne ressursen du la til i nettleseren. Dersom alt er vel får du opp en feilside som sier at databasen trenger å oppdateres.

### Del 2
 1. På [http://javabin.zapodot.org/yr/bergen](http://javabin.zapodot.org/yr/bergen) finner du JSON data som viser siste værobservasjoner for Bergen.
 Lag en ny kontroller metode som henter disse dataene asynkront og viser de til klienten
 2. På [http://javabin.zapodot.org/twitter/weather/bergen](http://javabin.zapodot.org/twitter/weather/bergen) finner du tweets som inneholder nøkkelordet `vær` på samme geolokasjon
 Klarer dere å kombinere disse dataene?
 3. Kan dere ved å lese [routes](https://github.com/Sonat-Consulting/javabin-play-public/blob/master/conf/routes) filen finne frem hvordan man kan få tilsvarende sammenstilling av data for Oslo

### Bonuspoeng
 * bytte ut JQuery fra main.scala.html med et som er hostet hos en CDN ved hjelp av [RequireJS](http://www.playframework.com/documentation/2.1.x/RequireJS-support)
 * lagre tweets fra WS i H2
 * Lage en JSon-konsumerende kontroller i Scala

### Noen hints
 * Eksempel applikasjon https://github.com/Sonat-Consulting/javabin-play-public
 * Demo applikasjon (Java): https://github.com/Sonat-Consulting/javabin-play-java-demo


