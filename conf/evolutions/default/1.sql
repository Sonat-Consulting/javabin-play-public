# --- !Ups
CREATE SEQUENCE sq_resource;
create table resource (
  id                        bigint NOT NULL DEFAULT nextval('sq_resource'),
  title                     varchar(512) not null,
  url                       varchar(512) not null,
  constraint pk_resource primary key (id))
;

INSERT INTO resource (title, url) VALUES ('Play Framwork 2.1.X docs', 'http://www.playframework.com/documentation/2.1.x/Home');
INSERT INTO resource (title, url) VALUES ('RequireJS', 'http://requirejs.org/');
INSERT INTO resource (title, url) VALUES ('LESS', 'http://lesscss.org/');
INSERT INTO resource (title, url) VALUES ('Coffeescript', 'http://coffeescript.org/');
INSERT INTO resource (title, url) VALUES ('Bootstrap', 'http://getbootstrap.com/');
INSERT INTO resource (title, url) VALUES ('EBean ORM', 'http://www.avaje.org/ebean/documentation.html');
INSERT INTO resource (title, url) VALUES ('Play2 Modules directory', 'http://www.playframework.com/documentation/2.1.x/Modules');



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists resource;

SET REFERENTIAL_INTEGRITY TRUE;

