package org.example.helpers;

import io.qameta.allure.Step;

/**
 * Класс помощник для утверждений в тестах.
 * Обеспечивает интеграцию с Allure и дополнительные функциональности для утверждений.
 */
public class Assertions {

    /**
     * Проверяет, что условие истинно. Если нет, выбрасывает исключение с указанным сообщением.
     * Интегрируется с Allure для отображения шагов проверки в отчетах.
     *
     * @param condition Условие, которое должно быть истинным.
     * @param message   Сообщение, которое будет выведено в случае, если условие ложно.
     */
    @Step("Проверяем что нет ошибки: {message}")
    public static void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }
}
