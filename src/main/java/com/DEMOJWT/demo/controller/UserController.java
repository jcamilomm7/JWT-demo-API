package com.DEMOJWT.demo.controller;

import com.DEMOJWT.demo.dto.User;
import com.DEMOJWT.demo.repositories.RepositorioUser;
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
    private RepositorioUser repositorioUser;

    @PostMapping("sigin")
    public User user(@RequestParam("user") String username, @RequestParam("password") String pwd) {

        User user = new User();
        user.setUser(username);
        user.setPwd(pwd);
        repositorioUser.save(user);
        return user;

    }

    @PostMapping("login")
    public User login(@RequestParam("user") String username, @RequestParam("password") String pwd) {

        //Lista para obtener resultados desde la BD
        Iterable<User> usuarios = new ArrayList<>();
        usuarios = repositorioUser.findAll();

        //Lista para devolver los resultados al front
        //List<ResponseTodo> response= new ArrayList<>();

        //Recorremos lista de entidad
        for ( User user: usuarios ) {
            if((user.getUser().equals(username))&& (user.getPwd().equals(pwd) )){
                String token = getJWTToken(username);
                user.setToken(token);
                return user;
            }

        }
        //Cuando el usuario no existe falta implementar una excepcion
        return null;
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