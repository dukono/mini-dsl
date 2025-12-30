package dukono.minidsl;

import lombok.Getter;

@Getter
public class DslInstantiationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Class<?> targetClass;

	public DslInstantiationException(final String message, final Throwable cause) {
		super(message, cause);
		this.targetClass = null;
	}

	public DslInstantiationException(final Class<?> targetClass, final Throwable cause) {
		super("Cannot instantiate DSL component: " + targetClass.getName()
				+ ". Ensure class has a public no-args constructor.", cause);
		this.targetClass = targetClass;
	}

	public DslInstantiationException(final Class<?> targetClass, final String additionalInfo, final Throwable cause) {
		super("Cannot instantiate DSL component: " + targetClass.getName() + ". " + additionalInfo, cause);
		this.targetClass = targetClass;
	}

}
