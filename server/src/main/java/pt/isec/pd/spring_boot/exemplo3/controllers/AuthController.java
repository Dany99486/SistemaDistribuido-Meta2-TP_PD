package pt.isec.pd.spring_boot.exemplo3.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import pt.isec.pd.spring_boot.exemplo3.security.TokenService;


@RestController
public class AuthController
{
    private final TokenService tokenService;

    public AuthController(TokenService tokenService)
    {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public String login(Authentication authentication)
    {
        return tokenService.generateToken(authentication);
    }


}
