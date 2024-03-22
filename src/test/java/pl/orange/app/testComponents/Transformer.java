package pl.orange.app.testComponents;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Transformer implements IAnnotationTransformer {

    //Do zmiany ilości powtórzeń całego testu
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        int someNumber = System.getProperty("REPEAT_TESTS") != null
                ? Integer.parseInt(System.getProperty("REPEAT_TESTS"))
                : 1; // liczba powtórzeń testów;
        annotation.setInvocationCount(someNumber);
    }
}
