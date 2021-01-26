import java.lang.annotation.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.*;
import java.util.*;
import javax.tools.Diagnostic.Kind;

/** Check that a class marked {@code @Utility} is indeed a utility class. */
@SupportedAnnotationTypes("Utility")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class UtilityProcessor extends AbstractProcessor {

	@Override
	public boolean process(
			Set<? extends TypeElement> annotations,
			RoundEnvironment roundingEnvironment)
	{
		Messager messager = processingEnv.getMessager();
		for (TypeElement te : annotations) {
			for (Element elt : roundingEnvironment.getElementsAnnotatedWith(te)) {
				if (elt.getKind().equals(ElementKind.CLASS)) {	// elt is a class
					// Check that the class is declared final
					if (! elt.getModifiers().contains(Modifier.FINAL)) {
						messager.printMessage(Kind.ERROR,
								"A @Utility class must be final.", elt);
					}

					// Check that enclosed elements are static
					for (Element enclosedElement: elt.getEnclosedElements()) {
						if (! enclosedElement.getModifiers().contains(Modifier.STATIC)) {
							if (enclosedElement.getKind().equals(ElementKind.CONSTRUCTOR)) {
								// Check constructor rules
								ExecutableElement e = (ExecutableElement) enclosedElement;
								if (e.getParameters().size() > 0) {
									messager.printMessage(Kind.WARNING,
											"A @Utility class should only have a default constructor:", e);
								}
								if (! e.getModifiers().contains(Modifier.PRIVATE)) {
									messager.printMessage(Kind.ERROR,
										"A constructor of a @Utility class must be private:", e);
								}
							} else {
								messager.printMessage(Kind.ERROR,
										"A member of a @Utility class must be static:",
										enclosedElement);
							}
						}
					}

				} else {
					messager.printMessage(Kind.ERROR,
							"@Utility applies to class only:", elt);
				}
			}
		}
		return true;
	}

}
