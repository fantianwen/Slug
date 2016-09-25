package van.tian.wen.multistaterefreshlayoutlib.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import van.tian.wen.multistaterefreshlayoutlib.exception.ModelFactoryInitialException;
import van.tian.wen.multistaterefreshlayoutlib.model.IModel;

/**
 * model工厂创建
 */
public class ModelFactory {

    private static ModelFactory mFactoryInstance;

    private ModelFactory() {
        this.mModelCache = new ConcurrentHashMap<>();
    }

    public static ModelFactory getInstance() {
        if (mFactoryInstance == null) {
            synchronized (ModelFactory.class) {
                if (mFactoryInstance == null) {
                    mFactoryInstance = new ModelFactory();
                }
            }
        }
        return mFactoryInstance;
    }

    private ConcurrentMap<String, IModel> mModelCache;

    /**
     * 获得Model
     *
     * @param className model类的全路径名称
     * @param <M>
     * @return
     */
    private <M extends IModel> M getModelInstance(String className) {

        if (mModelCache != null) {
            if (mModelCache.containsKey(className)) {
                return (M) getModelClass(className);
            } else {

                M m = null;
                try {
                    Class<M> aClass = (Class<M>) Class.forName(className);
                    m = aClass.newInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (m != null) {
                    add(className, m);
                    return m;
                } else {
                    throw new IllegalArgumentException("The " + className + " should be constructed with default constructor");
                }
            }
        } else {
            try {
                throw new ModelFactoryInitialException();
            } catch (ModelFactoryInitialException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void add(String modelClassName, IModel iModel) {
        if (mModelCache.containsKey(modelClassName)) {
            mModelCache.replace(modelClassName, iModel);
        } else {
            mModelCache.put(modelClassName, iModel);
        }
    }

    private IModel getModelClass(String className) {
        return mModelCache.get(className);
    }

    public <M extends IModel> M register(IModel iModel) {
        return getModelInstance(iModel.provideModelName());
    }

    public <M extends IModel> M register(Class<? extends IModel> modelClass) {
        return getModelInstance(modelClass.getCanonicalName());
    }

}
