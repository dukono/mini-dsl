package dukono.minidsl.processor;

import com.squareup.javapoet.ClassName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for parsing nested class names in DslProcessor.
 */
class DslProcessorNestedClassTest {

	@Test
	void testParseSimpleClassName() throws Exception {
		final DslProcessor processor = new DslProcessor();
		final Method parseMethod = DslProcessor.class.getDeclaredMethod("parseClassName", String.class);
		parseMethod.setAccessible(true);

		final ClassName result = (ClassName) parseMethod.invoke(processor, "com.example.MyClass");

		assertThat(result.packageName()).isEqualTo("com.example");
		assertThat(result.simpleName()).isEqualTo("MyClass");
		assertThat(result.toString()).isEqualTo("com.example.MyClass");
	}

	@Test
	void testParseNestedClassName() throws Exception {
		final DslProcessor processor = new DslProcessor();
		final Method parseMethod = DslProcessor.class.getDeclaredMethod("parseClassName", String.class);
		parseMethod.setAccessible(true);

		final ClassName result = (ClassName) parseMethod.invoke(processor,
				"dukono.minidsl.example.generated.OrderDomainDefinitionConfigConcise.OrderOperationsEnum");

		assertThat(result.packageName()).isEqualTo("dukono.minidsl.example.generated");
		assertThat(result.simpleName()).isEqualTo("OrderOperationsEnum");
		assertThat(result.toString())
				.isEqualTo("dukono.minidsl.example.generated.OrderDomainDefinitionConfigConcise.OrderOperationsEnum");
	}

	@Test
	void testParseDeeplyNestedClassName() throws Exception {
		final DslProcessor processor = new DslProcessor();
		final Method parseMethod = DslProcessor.class.getDeclaredMethod("parseClassName", String.class);
		parseMethod.setAccessible(true);

		final ClassName result = (ClassName) parseMethod.invoke(processor, "com.example.Outer.Middle.Inner");

		assertThat(result.packageName()).isEqualTo("com.example");
		assertThat(result.simpleName()).isEqualTo("Inner");
		assertThat(result.toString()).isEqualTo("com.example.Outer.Middle.Inner");
	}

	@Test
	void testParseClassInDefaultPackage() throws Exception {
		final DslProcessor processor = new DslProcessor();
		final Method parseMethod = DslProcessor.class.getDeclaredMethod("parseClassName", String.class);
		parseMethod.setAccessible(true);

		final ClassName result = (ClassName) parseMethod.invoke(processor, "MyClass");

		assertThat(result.packageName()).isEmpty();
		assertThat(result.simpleName()).isEqualTo("MyClass");
		assertThat(result.toString()).isEqualTo("MyClass");
	}
}
