package van.tian.wen.multistaterefreshlayoutlib.model;

/**
 * IModel的基础实现类
 */
public class BaseModel implements IModel {

    @Override
    public String provideModelName() {
        return getClass().getCanonicalName();
    }

}
