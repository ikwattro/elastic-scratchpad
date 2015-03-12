package queryparser;

import java.io.IOException;

import org.apache.lucene.search.Query;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.query.QueryParsingException;
import org.elasticsearch.index.query.QueryStringQueryParser;

public class CustomQueryParser extends QueryStringQueryParser {

	private final ESLogger logger;

	public CustomQueryParser(Settings settings) {
		super(settings);
		logger = Loggers.getLogger(getClass(), settings);
	}

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
