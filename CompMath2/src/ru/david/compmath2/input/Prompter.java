package ru.david.compmath2.input;

import java.util.Optional;

public interface Prompter<T> {
    Optional<T> prompt();
}
