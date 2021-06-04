package ru.david.compmath2.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class StringPrompter implements Prompter<String> {
    private String prompt = "(строка) > ";

    public StringPrompter() {}

    public StringPrompter(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public Optional<String> prompt() {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            return Optional.of(reader.readLine());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
