# Custom query parsing with an Elastic plugin

Let's build an [Elastic plugin](http://www.elastic.co/guide/en/elasticsearch/reference/current/modules-plugins.html).

First, set up the project structure:

```
query-parser/
├── build.sbt
├── project
│   └── build.properties
├── README.md
└── src
    └── main
        ├── java
        │   └── queryparser
        │       ├── CustomQueryParser.java
        │       └── CustomQueryParserPlugin.java
        └── resources
            └── es-plugin.properties
```

*build.sbt:*

```scala
libraryDependencies += "org.elasticsearch" % "elasticsearch" % "1.3.8"
```

*project/build.properties:*

```
sbt.version=0.13.7
```

Next, write a plugin.

*src/main/java/queryparser/CustomQueryParserPlugin.java:*

```java

package queryparser;

// import ...

public class CustomQueryParserPlugin extends AbstractPlugin {

  // ...

  public void onModule(Module module) {
    if (module instanceof IndicesQueriesModule) {
      QueryParser queryParserClass = new CustomQueryParser(settings);
      ((IndicesQueriesModule) module).addQuery(queryParserClass);
    }
  }

}
```

For brevity, our custom query parser will simply delegate to QueryStringQueryParser.

*src/main/java/queryparser/CustomQueryParser.java:*

```java
package queryparser;

// import ...

public class CustomQueryParser extends QueryStringQueryParser {

  // ...

  @Override
  public String[] names() {
    String name = "custom_query";
    return new String[] { name, Strings.toCamelCase(name) };
  }

  @Override
  public Query parse(QueryParseContext parseContext) throws IOException,
      QueryParsingException {
    logger.info("CUSTOM QUERY PARSER PARSING CUSTOM QUERY!!!");
    return super.parse(parseContext);
  }

}
```

Finally, we need to tell Elastic the full name of our plugin class:

*src/main/resources/es-plugin.properties:*

```
plugin=queryparser.CustomQueryParserPlugin
```

Let's package it up:

```
$ sbt package

...

[info] Packaging target/scala-2.10/query-parser_2.10-0.1-SNAPSHOT.jar
```

Now we can install it, bounce Elastic, and try it out:

```
# cd /usr/share/elasticsearch/bin
# ./plugin --url file:///.../query-parser_2.10-0.1-SNAPSHOT.jar --install query-parser
# service elasticsearch restart
```

```
curl -s -XGET localhost:9200/foo/_search -d '
{
  "query": {
    "custom_query": {
      "fields": [ "bar" ],
      "query": "baz"
    }
  }
}
'
```

We should see our parser's output in Elastic's logs:

```
$ grep 'CUSTOM QUERY PARSER' /var/log/elasticsearch/elasticsearch.log
[2015-03-12 16:29:47,886][INFO ][queryparser.CustomQueryParser] [Agatha Harkness] CUSTOM QUERY PARSER PARSING CUSTOM QUERY!!!
```
