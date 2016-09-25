package van.tian.wen.multistaterefreshlayoutlib.mvp;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import van.tian.wen.multistaterefreshlayoutlib.annotation.SingleModel;
import van.tian.wen.multistaterefreshlayoutlib.factory.ModelFactory;
import van.tian.wen.multistaterefreshlayoutlib.model.IModel;

/**
 * Presenter
 */
public abstract class BasePresenter<V extends BasePresenter.IView, M extends IModel> {

    public Context mContext;
    /**
     * View
     */
    protected V mView;

    /**
     * Model
     */
    @SingleModel
    protected M mModel;

    public BasePresenter(V view) {
        this(view, null);
    }

    public BasePresenter(V view, Class<M> modelClass) {
        this.mView = view;
        if (mView instanceof Context) {
            this.mContext = (Context) mView;
        }

        if (modelClass != null) {
            this.mModel = provideModelInstance(modelClass);
        } else {
            try {
                this.mModel = provideModelInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    protected M provideModelInstance(Class<M> modelClass) {
        return ModelFactory.getInstance().register(modelClass);
    }

    private M provideModelInstance() throws ClassNotFoundException {

        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (Type type : actualTypeArguments) {
                if (type instanceof Class && IModel.class.isAssignableFrom((Class) type)) {
                    return ModelFactory.getInstance().register((Class<M>) type);
                }
            }
        }

        throw new ClassNotFoundException("could not find Single Model");
    }

    public V getView() {
        return mView;
    }

    public M getModel() {
        return mModel;
    }

    public void onStop() {
    }

    public interface IView {

    }

}
