package com.DEMOJWT.demo.controller;

import com.DEMOJWT.demo.dto.User;
import com.DEMOJWT.demo.repositories.RepositorioUser;
import com.DEMOJWT.demo.services.UserServices;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserServices userServices;

    @Autowired
    private RepositorioUser repositorioUser;


    //Dejamos este endpoint para registro de usuarios
    @PostMapping("sigin")
    public User user(@RequestParam("user") String username, @RequestParam("password") String pwd) {
        return userServices.sigin(username, pwd);
    }


    //Este endpoint mira en la BD que si este registrado y luego le genera el token
    @PostMapping("login")
    public List<User> login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
        String token = getJWTToken(username);
        return userServices.login(username, pwd, token);

    }

    //Listar todos los usuarios
    @GetMapping(value = "getusers")
    public List<User> list() {
        return userServices.list();
    }


    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("sofkaJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Valido " + token;
    }
}