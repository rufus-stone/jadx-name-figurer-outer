package jadx.plugins.tfoc;

import jadx.api.plugins.options.impl.BasePluginOptionsBuilder;

public class ExampleOptions extends BasePluginOptionsBuilder {

	private boolean enable;

	@Override
	public void registerOptions() {
		boolOption(JadxNameFigurerOuter.PLUGIN_ID + ".enable")
				.description("enable plugin")
				.defaultValue(true)
				.setter(v -> enable = v);
	}

	public boolean isEnable() {
		return enable;
	}
}
