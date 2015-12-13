package diagent;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.This;

public class InjectionInterceptor {
	
	public static void intercept(@This Object currentInstance, @AllArguments Object[] injectedInstances) {
		
		DependencyGraphService.addDependency(currentInstance, injectedInstances);
	}
}
