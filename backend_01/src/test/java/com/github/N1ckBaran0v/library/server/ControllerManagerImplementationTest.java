package com.github.N1ckBaran0v.library.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ControllerManagerImplementationTest {
    private ControllerManager manager;
    private final MiniController controller = ctx -> ctx.setResponseBody(Thread.currentThread());

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        manager = new ControllerManagerImplementation();
        var field = ControllerManagerImplementation.class.getDeclaredField("controllers");
        field.setAccessible(true);
        var map = (Map<RequestMethod, Map<String, MiniController>>) field.get(manager);
        map.get(RequestMethod.GET).put("/thread", controller);
    }

    @Test
    void registerControllerSuccess() {
        manager.registerController(RequestMethod.GET, "/thread1", controller);
    }

    @Test
    void getControllerSuccess() {
        var result = manager.getController(RequestMethod.GET, "/thread");
        assertEquals(controller, result);
    }

    @Test
    void controllerConflictException() {
        assertThrows(ControllersConflictException.class,
                () -> manager.registerController(RequestMethod.GET, "/thread", controller));
    }

    @Test
    void invalidPathException1() {
        assertThrows(InvalidPathException.class, () -> manager.registerController(RequestMethod.GET, "", controller));
    }

    @Test
    void invalidPathException2() {
        assertThrows(InvalidPathException.class,
                () -> manager.registerController(RequestMethod.GET, "a/a", controller));
    }

    @Test
    void invalidPathException3() {
        assertThrows(InvalidPathException.class,
                () -> manager.registerController(RequestMethod.GET, "/e-e", controller));
    }

    @Test
    void controllerNotFoundException() {
        assertThrows(ControllerNotFoundException.class, () -> manager.getController(RequestMethod.GET, "/"));
    }
}