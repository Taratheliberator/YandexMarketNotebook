package org.example.tests;

import org.junit.jupiter.api.AfterEach;
import org.example.appmanager.ApplicationManager;
import org.junit.jupiter.api.BeforeEach;
/**
 * Базовый класс для тестовых классов.
 * Этот класс содержит общую настройку и завершение для тестов,
 * использующих {@link ApplicationManager} для управления веб-драйвером и другими ресурсами.
 */
public class TestBase {

    protected static final ApplicationManager app = new ApplicationManager();
    /**
     * Выполняет начальную настройку перед каждым тестом.
     * Этот метод инициализирует {@link ApplicationManager}, который управляет ресурсами,
     * такими как веб-драйвер, необходимые для выполнения тестов.
     *
     * @throws Exception в случае возникновения ошибок при инициализации.
     */
    @BeforeEach
    public void setUp() throws Exception {
        app.init();
    }
    /**
     * Завершает работу и освобождает ресурсы после каждого теста.
     * Этот метод останавливает {@link ApplicationManager} и связанные с ним ресурсы,
     * обеспечивая корректное закрытие веб-драйвера и других используемых сервисов.
     */
    @AfterEach
    public void tearDown() {
        app.stop();
    }
}

