package com.DEMOJWT.demo.services;

import com.DEMOJWT.demo.dto.User;
import com.DEMOJWT.demo.repositories.RepositorioUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServices {
    @Autowired
    private RepositorioUser repositorioUser;

    public List<User> list() {

        //Lista para obtener resultados desde la BD
        Iterable<User> usuarios = new ArrayList<>();
        usuarios = repositorioUser.findAll();


        //Lista para almacenar todos los usuarios de la base de datos
        List<User> response = new ArrayList<>();

        for (User user : usuarios) {
            response.add(user);
        }
        return response;
    }

    public User sigin(String username, String pwd) {

        User user = new User();
        user.setUser(username);
        user.setPwd(pwd);
        repositorioUser.save(user);
        return user;

    }

    public List<User> login(String username, String pwd, String token) {

        //Lista para obtener resultados desde la BD
        Iterable<User> usuarios = new ArrayList<>();
        usuarios = repositorioUser.findAll();
        List<User> listUser = new ArrayList<>();


        //Recorremos lista de entidad
        for (User user : usuarios) {
            if ((user.getUser().equals(username)) && (user.getPwd().equals(pwd))) {

                user.setToken(token);
                listUser.add(user);

            }

        }
        return listUser;

    }

}