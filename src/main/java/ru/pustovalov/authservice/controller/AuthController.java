package ru.pustovalov.authservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pustovalov.authservice.dto.ErrorResponse;
import ru.pustovalov.authservice.dto.UserDto;
import ru.pustovalov.authservice.exception.LoginException;
import ru.pustovalov.authservice.exception.RegistrationException;
import ru.pustovalov.authservice.service.JwtTokenService;
import ru.pustovalov.authservice.service.UserService;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping
    public void register(@RequestBody UserDto user) {
        userService.register(user.login(), user.password());
    }

    @PostMapping("/token")
    public ErrorResponse getToken(@RequestBody UserDto user) {
        userService.checkCredentials(user.login(), user.password());
        return new ErrorResponse(jwtTokenService.generateToken(user.login()));
    }

    @ExceptionHandler({RegistrationException.class, LoginException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(exception.getMessage()));
    }
}
