package com.epam.annotation.processor;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import com.epam.annotation.util.EventStringUtil;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({ "com.epam.cdp.events.annotation.EventListener" })
public class EventListenerAnnotationProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> elements,
			RoundEnvironment roundEnv) {
		boolean result = false;
		for (TypeElement te : elements) {
			for (Element e : roundEnv.getElementsAnnotatedWith(te))
				processAnnotatedElement(e);
		}
		return result;
	}

	private boolean processAnnotatedElement(Element e) {
		boolean result = false;
		if (isMethod(e)) {
			result = processMethod((ExecutableElement) e);
		}
		return result;
	}

	private boolean processMethod(ExecutableElement e) {
		boolean result = false;

		result = isValidModifiers(e);
		result = result && isValidParameters(e);

		return result;
	}

	private boolean isValidParameters(ExecutableElement e) {
		boolean result = false;

		List<? extends VariableElement> param = e.getParameters();
		int paramCount = param.size();
		boolean isOneArgument = paramCount == 1 ? true : false;

		if (isOneArgument) {
			VariableElement firstVariableElement = param.get(0);
			TypeMirror firstVariableElementTypeMirror = firstVariableElement
					.asType();
			TypeElement actualTypeElement = (TypeElement) processingEnv
					.getTypeUtils().asElement(firstVariableElementTypeMirror);
			TypeElement expectedTypeElement = processingEnv.getElementUtils()
					.getTypeElement(EventStringUtil.PARAMETER_TYPE);
			result = isSubclassOrImplements(actualTypeElement,
					expectedTypeElement);
		}
		return result;
	}

	private boolean isValidModifiers(ExecutableElement e) {
		boolean result = true;

		boolean isPublic = e.getModifiers().contains(Modifier.PUBLIC);
		boolean isStatic = e.getModifiers().contains(Modifier.STATIC);

		if (!isPublic || isStatic) {
			if (!isPublic) {
				processingEnv.getMessager().printMessage(Kind.ERROR,
						EventStringUtil.PUBLIC_ERROR_MSG, e);
			}
			if (isStatic) {
				processingEnv.getMessager().printMessage(Kind.ERROR,
						EventStringUtil.STATIC_ERROR_MSG, e);
			}
			result = false;
		}
		return result;
	}

	private boolean isMethod(Element e) {
		boolean result = e.getKind().equals(ElementKind.METHOD);
		return result;
	}

	public boolean isSubclassOrImplements(TypeElement element,
			TypeElement interfaceOrClass) {
		boolean result = false;
		
		Types types = processingEnv.getTypeUtils();
		for (TypeMirror interfaceType : element.getInterfaces()) {
			if (types.asElement(interfaceType).equals(interfaceOrClass)) {
				return true;
			}
		}

		if (element.equals(interfaceOrClass)) {
			return true;
		}

		TypeElement superClass = (TypeElement) types.asElement(element
				.getSuperclass());
		
		
		result = superClass != null
				&& isSubclassOrImplements(superClass, interfaceOrClass);
		if (!result){
			processingEnv.getMessager().printMessage(Kind.ERROR,
					EventStringUtil.WRONG_METHOD_PARAMETER_ERROR_MSG, element);
		}
		
		return result;
	}
}
