package diagent;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatcher.Junction;

public class DIAgent {

	public static void premain(String args, Instrumentation instrumentation) {

		System.out.println("Hello World from Agent");
		
		transformClasses(instrumentation);
		
		startWebServer(9090);
	}

	private static void transformClasses(Instrumentation instrumentation) {

		new AgentBuilder.Default().withListener(new AgentListener()).type(geTypeMatchers())
				.transform(new Transformer() {

					@Override
					public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription) {

						return builder
								.invokable(isAnnotatedWith(named(INJECT_ANNOTATION))
										.or(isAnnotatedWith(named(AUTOWIRED_ANNOTATION))))
								.intercept(SuperMethodCall.INSTANCE
										.andThen(MethodDelegation.to(InjectionInterceptor.class)));
					}

				}).installOn(instrumentation);
	}

	private static void startWebServer(final int port) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					
					System.out.println(String.format("Setting up web server on port %s...", port));
					
					new DependencyGraphWebServer(port).start();
					
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}).start();
	}

	private static Junction<NamedElement> geTypeMatchers() {

		// TODO: try that instead: type(any(), not(isBootstrapClassLoader()))
		return not(nameStartsWith("java").or(nameStartsWith("sun")).or(nameStartsWith("javax"))
				.or(nameStartsWith("com.sun")));
	}

	private static final String AUTOWIRED_ANNOTATION = "org.springframework.beans.factory.annotation.Autowired";
	private static final String INJECT_ANNOTATION = "javax.inject.Inject";
}

class AgentListener implements AgentBuilder.Listener {

	@Override
	public void onComplete(String arg0) {
	}

	@Override
	public void onError(String typeName, Throwable throwable) {
		// System.err.println("Error for " + typeName);
		// throwable.printStackTrace();
	}

	@Override
	public void onIgnored(TypeDescription arg0) {
	}

	@Override
	public void onTransformation(TypeDescription arg0, DynamicType arg1) {
	}

}
