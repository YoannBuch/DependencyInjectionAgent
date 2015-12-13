package test;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Test;

import diagent.DependencyGraphService;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.This;

public class InterceptConstructorTest {

	@Test
	public void testConstructorInterception() throws ClassNotFoundException {

		ByteBuddyAgent.install();

		new AgentBuilder.Default().type(nameStartsWith("test")).transform(new AgentBuilder.Transformer() {

			@Override
			public Builder<?> transform(Builder<?> builder, TypeDescription td) {

				return builder.constructor(isAnnotatedWith(Inject.class))
						.intercept(SuperMethodCall.INSTANCE.andThen(MethodDelegation.to(ConstructorInterceptor.class)));
			}
		}).installOnByteBuddyAgent();

		// If this line is uncommented, ClassNotFoundException won't be thrown
		MyClass myClass = new MyClass("a param");
		
		System.out.println(myClass);

		// Manually load MyClass
		Class<?> myClassDefinition = getClass().getClassLoader().loadClass("test.MyClass");

		// Throws NoClassDefFoundError
		for(Constructor<?> constructor : myClassDefinition.getDeclaredConstructors()) {
			System.out.println(constructor);
		}
	}
}

class MyClass {

	@Inject
	public MyClass(String aParam) {
		System.out.println("constructor called");
	}
}

class ConstructorInterceptor {

	public static void intercept(@This Object bean, @AllArguments Object[] dependencies) {
		
		DependencyGraphService.addDependency(bean, dependencies);
	}
}
