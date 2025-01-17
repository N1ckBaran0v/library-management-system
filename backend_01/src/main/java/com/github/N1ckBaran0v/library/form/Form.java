package com.github.N1ckBaran0v.library.form;

import java.io.Serializable;

public interface Form extends Serializable {
    default boolean hasErrors() {
        return false;
    }
}
