package com.example.demo.controller;

import com.example.demo.model.UsersModel;
import com.example.demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {

    @Autowired
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        // Como o projeto é simples, foi utilizado com entity
        // porém é mais comum o dto
        model.addAttribute("registerRequest", new UsersModel());
        return "register_page";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        //Quando a pagina de registro ou login renderizar o Spring
        //vai colocar "loginRequest" num novo UsersModel.
        model.addAttribute("loginRequest", new UsersModel());
        return "login_page";
    }

    @GetMapping("/sucessfully_login")
    public String getSucessfullyPage(Model model) {
        model.addAttribute("sucessfullyRequest", new UsersModel());
        return "sucessfully_login";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UsersModel usersModel) {
        System.out.println("register request: " + usersModel);
        //A classe userService estabelece a condição de que se
        //registeredUser não for null, é renderizada a pagina de login e
        // o usuário se cadastrou com sucesso.
        //se registeredUser for null, significa que o login ou password
        //esta errado.
        UsersModel registeredUser = usersService.registerUser(
                usersModel.getLogin(),
                usersModel.getPassword(),
                usersModel.getEmail()
        );
        return registeredUser == null ? "error_page" : "redirect:/sucessfully_login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UsersModel usersModel, Model model) {
        System.out.println("login request: " + usersModel);
        UsersModel authenticated = usersService.authenticate(usersModel.getLogin(), usersModel.getPassword());
        if (authenticated != null) {
            model.addAttribute("userLogin", authenticated.getLogin());
            return "personal_page";
        } else {
            return "error_page";
        }
    }

    @PostMapping("/sucessfully")
    public String sucessfully(@ModelAttribute UsersModel usersModel, Model model) {
        System.out.println("sucessfully request: " + usersModel);
        UsersModel authenticated = usersService.authenticate(usersModel.getLogin(), usersModel.getPassword());
        return null;
    }
}
