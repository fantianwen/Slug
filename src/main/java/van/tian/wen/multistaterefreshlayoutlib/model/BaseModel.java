package van.tian.wen.multistaterefreshlayoutlib.model;

import van.tian.wen.multistaterefreshlayoutlib.factory.ModelFactory;

/**
 * IModel的基础实现类
 */
public class BaseModel implements IModel {

    private final ModelFactory mModelFactory;

    public BaseModel() {
        mModelFactory = ModelFactory.getInstance();
        mModelFactory.register(this);
    }

    @Override
    public String provideModelName() {
        return getClass().getName();
    }

}
