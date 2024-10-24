package Services;

import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.injection.scopes.ApplicationScoped;

@ApplicationScoped
public class BaseService {

	private NavigationService navigationService;
	private UnoService unoService;

	public BaseService() {
		this.navigationService = NavigationService.getInstance();
		this.unoService = UnoService.getInstance();
	}

	public NavigationService getNavigationService() {
		return navigationService;
	}

	public UnoService getUnoService() {
		return unoService;
	}

	public void initNavigationService(ViewFlowContext context) {
		this.navigationService.initNavigationService(context);
	}
}