package archtoring;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import archtoring.handlers.EPLHandler;
import archtoring.handlers.GithubHandler;
import archtoring.handlers.ModelHandler;

public class Application implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		Map<String, String[]> mm = context.getArguments();
		final List<String> args = Arrays.asList(mm.get(IApplicationContext.APPLICATION_ARGS));
		ModelHandler model = new ModelHandler(args);
		model.execute();
		GithubHandler git = new GithubHandler();
		EPLHandler epl = new EPLHandler();
		epl.execute(ModelHandler.getNames());
		git.execute();
		return EXIT_OK;
	}

	@Override
	public void stop() {
	}

}
