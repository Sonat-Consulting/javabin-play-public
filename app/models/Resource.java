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
    @Column(length = 512, nullable = false)
    public String url;

    public static Finder<Long, Resource> find = new Finder<Long, Resource>(Long.class, Resource.class);

}
