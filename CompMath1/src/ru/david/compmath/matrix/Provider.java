package ru.david.compmath.matrix;

public interface Provider<T> {
    T get() throws ProvidingException;
}
