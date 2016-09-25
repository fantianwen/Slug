package van.tian.wen.multistaterefreshlayoutlib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicate that this is SingleInstance made by {@link van.tian.wen.multistaterefreshlayoutlib.factory.ModelFactory}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SingleModel {

}
