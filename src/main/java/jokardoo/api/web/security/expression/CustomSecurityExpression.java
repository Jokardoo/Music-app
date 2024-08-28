package jokardoo.api.web.security.expression;

import jokardoo.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Для работы кастомных Security настроек необходимо указать аннотацию @EnableGlobalMethodSecurity
// в этот класс будет обращаться Контроллер, методы которого
// помечены аннотацией @PreAuthorize("@название_Security_класса.метод(#поле)")

@RequiredArgsConstructor
@Service("customSecurityExpression")
public class CustomSecurityExpression {
    private final UserService userService;

    // Сюда добавляем методы, возвращающие boolean значения

    public boolean isItMuse(String name) {
        if (name.equalsIgnoreCase("muse")) {
            return false;
        }
        else return true;
    }
}
