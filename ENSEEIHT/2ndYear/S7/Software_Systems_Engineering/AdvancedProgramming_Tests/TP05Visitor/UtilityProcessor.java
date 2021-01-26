import java.lang.annotation.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.*;
import java.util.*;
import javax.tools.Diagnostic.Kind;
import javax.lang.model.util.*;	// Visitor9...

class UtilityVisitor extends ElementKindVisitor9<Void, Void> {
	private Messager messager;
	private Element root;	// l'Ã©lÃ©ment racine de l'analyse

	public UtilityVisitor(Messager messager, Element root) {
		super();
		this.messager = messager;
		this.root = root;
	}

	@Override
	public Void visitTypeAsClass(TypeElement e, Void p) {
		if (e == root) {
			if (! e.getModifiers().contains(Modifier.FINAL)) {
				messager.printMessage(Kind.ERROR, "A @Utility class must be final.", e);
				for (Element elt : e.getEnclosedElements()) {
					elt.accept(this, null);
				}
			}
		} else {
			if (! e.getModifiers().contains(Modifier.STATIC)) {
				messager.printMessage(Kind.ERROR, "An internal class of a @Utility class must be static.", e);
			}
		}
		return null;
	}

	@Override
	public Void visitVariableAsField(VariableElement e, Void p) {
		if (! e.getModifiers().contains(Modifier.STATIC)) {
			messager.printMessage(Kind.ERROR, "A field of a @Utility class must be static:", e);
		}
		return null;
	}

	@Override
	public Void visitExecutableAsMethod(ExecutableElement e, Void p) {
		
		System.out.println("visit e = " + e);
		if (! e.getModifiers().contains(Modifier.STATIC)) {
			messager.printMessage(Kind.ERROR, "A method of a @Utility class must be static:", e);
		}
		return null;
	}

	@Override
	public Void visitExecutableAsConstructor(ExecutableElement e, Void p) {
		if (e.getParameters().size() > 0) {
			messager.printMessage(Kind.WARNING,
					"A @Utility class should only have a default constructor:", e);
		}
		// else {	// The default constructor
		if (! e.getModifiers().contains(Modifier.PRIVATE)) {
			messager.printMessage(Kind.ERROR,
				"A constructor of a @Utility class must be private:", e);
		}
		// }
		return null;
	}

}

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
					UtilityVisitor uv = new UtilityVisitor(messager, elt);
					elt.accept(uv, null);
				} else {
					messager.printMessage(Kind.ERROR,
							"@Utility applies to class only:", elt);
				}
			}
		}
		return true;
	}

}
