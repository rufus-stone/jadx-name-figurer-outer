package jadx.plugins.tfoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jadx.api.plugins.JadxPlugin;
import jadx.api.plugins.JadxPluginContext;
import jadx.api.plugins.JadxPluginInfo;
import jadx.api.plugins.JadxPluginInfoBuilder;

public class JadxNameFigurerOuter implements JadxPlugin {
	public static final String PLUGIN_ID = "jadx-name-figurer-outer";

	private final ExampleOptions options = new ExampleOptions();

	private static final Logger LOG = LoggerFactory.getLogger(JadxNameFigurerOuter.class);

	@Override
	public JadxPluginInfo getPluginInfo() {
		return JadxPluginInfoBuilder.pluginId(PLUGIN_ID)
				.name("Jadx Name Figurer-Outer")
				.description("Rename fields based on the string argument to JSONObject's getString(), etc.")
				.homepage("https://github.com/rufus-stone/jadx-name-figurer-outer")
				.build();
	}

	@Override
	public void init(JadxPluginContext context) {
		LOG.debug("init!");
		context.registerOptions(options);
		if (options.isEnable()) {
			context.addPass(new FigureOutNamesPass());
		}
	}
}
