package org.sterl.training.springquartz.pmw.model;

import java.util.function.Consumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SequentialStep<T extends AbstractWorkflowContext> implements Step<T> {
    private final Consumer<T> fn;

    @Override
    public void apply(T c) {
        fn.accept(c);
    }

}
