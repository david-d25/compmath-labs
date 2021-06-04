package ru.david.compmath.ui;

import java.util.Optional;

public interface Prompter<T> {
    Optional<T> prompt();
}
