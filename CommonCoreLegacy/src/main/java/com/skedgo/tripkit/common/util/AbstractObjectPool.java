package com.skedgo.tripkit.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Stack;

public abstract class AbstractObjectPool<T> {

  private Stack<T> mObjects;

  public final T retrieve() {
    if (mObjects == null || mObjects.isEmpty()) {
      return newObject();
    } else {
      T obj = mObjects.pop();

      onRecycle(obj);

      return obj;
    }
  }

  public final void save(T obj) {
    if (mObjects == null) {
      mObjects = new Stack<T>();
    }

    mObjects.push(obj);
  }

  /**
   * By default, attempts to invoke a no-arg constructor on class T.
   * <p/>
   * Subclasses should override to implement additional logic
   *
   * @return a new instance of type T
   */
  protected T newObject() {
    Type t = getClass().getGenericSuperclass();
    try {
      if (t != null && t instanceof ParameterizedType) {
        Type[] types = ((ParameterizedType) t).getActualTypeArguments();
        if (types != null & types.length > 0 && types[0] instanceof Class) {
          return (T) ((Class) types[0]).newInstance();
        }
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Error invoking empty constructor", e);
    }

    throw new IllegalArgumentException("Couldnt find empty constructor to execute!");
  }

  /**
   * Gives subclasses the chance to perform additional operations on an
   * object *before* it is recycled.
   *
   * @param obj
   */
  protected void onRecycle(T obj) {
    //Subclasses can override..
  }
}
