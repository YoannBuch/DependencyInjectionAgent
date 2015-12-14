package diagent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class DependencyGraphService {

	private static ConcurrentHashMap<Object, List<Object>> dependencyMap = new ConcurrentHashMap<>();

	public static synchronized void addDependency(Object bean, Object[] dependencies) {

		if (dependencies == null) {
			return;
		}

		System.out.println(bean + " depends on " + Arrays.toString(dependencies));

		List<Object> previousValue = dependencyMap.putIfAbsent(bean,
				new ArrayList<Object>(Arrays.asList(dependencies)));

		if (previousValue != null) {
			previousValue.addAll(Arrays.asList(dependencies));
		}

		for (Object dependency : dependencies) {
			if (dependency != null) {
				dependencyMap.putIfAbsent(dependency, new ArrayList<Object>());
			}
		}
	}

	public static synchronized DependencyGraph getDependencyGraph() {

		List<Bean> beans = new ArrayList<>();
		List<Dependency> dependencies = new ArrayList<>();

		for (Entry<Object, List<Object>> dependencyEntry : dependencyMap.entrySet()) {
			Object bean = dependencyEntry.getKey();
			List<Object> beanDependencies = dependencyEntry.getValue();

			String beanId = Integer.toHexString(bean.hashCode());

			beans.add(new Bean(beanId, bean.getClass().getName()));

			for (Object beanDependency : beanDependencies) {

				String injectBeanId = Integer.toHexString(beanDependency.hashCode());

				dependencies.add(new Dependency(beanId, injectBeanId));
			}
		}

		return new DependencyGraph(beans, dependencies);
	}

}

class DependencyGraph {
	private final List<Dependency> dependencies;
	private final List<Bean> beans;

	public DependencyGraph(List<Bean> beans, List<Dependency> dependencies) {
		this.dependencies = dependencies;
		this.beans = beans;
	}

	public List<Bean> getBeans() {
		return this.beans;
	}

	public List<Dependency> getDependencies() {
		return this.dependencies;
	}
}

class Bean {
	private String id;
	private String typeName;

	public Bean(String id, String typeName) {
		super();
		this.id = id;
		this.typeName = typeName;
	}

	public String getId() {
		return id;
	}

	public String getTypeName() {
		return typeName;
	}
}

class Dependency {

	private String sourceBeanId;
	private String destinationBeanId;

	public Dependency(String sourceBeanId, String destinationBeanId) {
		this.sourceBeanId = sourceBeanId;
		this.destinationBeanId = destinationBeanId;
	}

	public String getSourceBeanId() {
		return sourceBeanId;
	}

	public String getDestinationBeanId() {
		return destinationBeanId;
	}
}
