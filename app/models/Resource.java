package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * @author sondre
 */
@Entity
public class Resource extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Constraints.Required
    @Column(length = 512, nullable = false)
    public String title;

    @Constraints.Required
    @Constraints.Pattern(value = "^(http|https)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*$",
            message = "error.constraint.url")
    @Column(length = 512, nullable = false)
    public String url;

    public static Finder<Long, Resource> find = new Finder<Long, Resource>(Long.class, Resource.class);

}
