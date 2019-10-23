package org.acra.util;

public final class ReflectionHelper {
    public Object create(String str) {
        try {
            return Class.forName(str).newInstance();
        } catch (ClassNotFoundException e) {
            throw new ReflectionException("Could not find class : " + str, e);
        } catch (InstantiationException e2) {
            throw new ReflectionException("Could not instantiate class : " + str, e2);
        } catch (IllegalAccessException e3) {
            throw new ReflectionException("Could not access class : " + str, e3);
        }
    }
}
