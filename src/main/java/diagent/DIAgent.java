package diagent;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;

public class DIAgent {

	public static void premain(String args, Instrumentation instrumentation) throws IOException {

		System.out.println("Hello World from Agent");

		new DependencyGraphWebServer(9090).start();

		new AgentBuilder.Default().withInitializationStrategy(InitializationStrategy.Premature.INSTANCE)
				.type(any()).transform(new Transformer() {

					@Override
					public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription) {

						return builder.invokable(isAnnotatedWith(Inject.class).or(isAnnotatedWith(Autowired.class))).intercept(
								SuperMethodCall.INSTANCE.andThen(MethodDelegation.to(InjectionInterceptor.class)));
					}
				}).installOn(instrumentation);
	}
}
