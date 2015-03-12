package queryparser;

import java.util.Collection;

import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryParser;
import org.elasticsearch.indices.query.IndicesQueriesModule;
import org.elasticsearch.plugins.AbstractPlugin;

public class CustomQueryParserPlugin extends AbstractPlugin {

	private Settings settings;

	@Inject
	public CustomQueryParserPlugin() {
	}

	@Override
	public String name() {
		return "custom-query-parser";
	}

	@Override
	public String description() {
		return "Custom query parser";
	}

	@Override
	public Collection<Module> modules(Settings settings) {
		this.settings = settings;
		return ImmutableList.of();
	}

	public void onModule(Module module) {
		if (module instanceof IndicesQueriesModule) {
			QueryParser queryParserClass = new CustomQueryParser(settings);
			((IndicesQueriesModule) module).addQuery(queryParserClass);
		}
	}

}
