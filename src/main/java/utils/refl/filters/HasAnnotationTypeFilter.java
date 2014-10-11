package utils.refl.filters;

import java.lang.annotation.Annotation;

public class HasAnnotationTypeFilter extends DecoratorTypeFilter<Object, Object> {

	private Class<? extends Annotation> annotationType;
	
	public HasAnnotationTypeFilter(ITypeFilter<Object, ? extends Object> filter, Class<? extends Annotation> annotationType) {
		super(filter);
		
		this.annotationType = annotationType;
	}
	
	public HasAnnotationTypeFilter(Class<? extends Annotation> annotationType) {
		this(null, annotationType);
	}

	@Override
	public boolean acceptImpl(Class<?> type) {
		return type.getAnnotation(this.annotationType) != null;
	}
}