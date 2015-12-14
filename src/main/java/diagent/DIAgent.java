package diagent;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

public class DIAgent {

	private static final String AUTOWIRED_ANNOTATION = "org.springframework.beans.factory.annotation.Autowired";
	private static final String INJECT_ANNOTATION = "javax.inject.Inject";

	public static void premain(String args, Instrumentation instrumentation) throws IOException {

		System.out.println("Hello World from Agent");

		new DependencyGraphWebServer(9090).start();

		new AgentBuilder.Default().withListener(new AgentListener())
				.withInitializationStrategy(InitializationStrategy.Premature.INSTANCE)

				.type(ElementMatchers.not(ElementMatchers.nameStartsWith("java"))).transform(new Transformer() {

					@Override
					public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription) {

						return builder
								.invokable(isAnnotatedWith(ElementMatchers.named(
										INJECT_ANNOTATION)).or(isAnnotatedWith(
												ElementMatchers.named(AUTOWIRED_ANNOTATION))))
								.intercept(SuperMethodCall.INSTANCE
										.andThen(MethodDelegation.to(InjectionInterceptor.class)));
					}
				}).installOn(instrumentation);
	}
}

class AgentListener implements AgentBuilder.Listener {

	@Override
	public void onComplete(String arg0) {
	}

	@Override
	public void onError(String typeName, Throwable throwable) {
//		System.err.println("Error for " + typeName);
//		throwable.printStackTrace();
	}

	@Override
	public void onIgnored(TypeDescription arg0) {
	}

	@Override
	public void onTransformation(TypeDescription arg0, DynamicType arg1) {
	}

}
