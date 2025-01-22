package com.github.N1ckBaran0v.library;

import com.github.N1ckBaran0v.library.context.Context;

public class Main {
    public static void main(String[] args) {
        var context = new Context(8080);
        context.getServer().start();
    }
}
