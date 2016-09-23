package van.tian.wen.multistaterefreshlayoutlib.exception;

/**
 * Model工厂类初始化错误
 */
public class ModelFactoryInitialException extends Exception {

    private static final String ERROR_MESSAGE = "modelFactory initialization error";

    public ModelFactoryInitialException() {
        super(ERROR_MESSAGE);
    }
}
