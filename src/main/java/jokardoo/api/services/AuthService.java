package jokardoo.api.services;

import jokardoo.api.web.dto.auth.JwtRequest;
import jokardoo.api.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
