package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestConroller {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserRestConroller(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping()
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Redirect-Ref", "/admin");
        return new ResponseEntity<>(userService.findById(id), httpHeaders, HttpStatus.OK);
        //return userService.findById(id);
    }

    /*TODO возвращаемое значение должно быть id созданного юзера --  в заголовке Location: /:entity/:new_id
      TODO в случае ошибки здесь могу отправить о существующем email */
    @PostMapping()
    public ModelAndView addUser(@RequestBody @Valid User user,
                                BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("Будем считать, что здесь логи, frontend не справился с проверкой");
            return null;
        }

        String enterEmail = user.getEmail();
        if (userService.save(user).getEmail() == "") {
            System.out.println("Такой email существует: " + enterEmail);
            return null;
        }
        return null;
    }


    //TODO что возвращать
    // TODO в случае ошибки здесь могу отправить о существующем email
    @PutMapping(value = "/{id}")
    public ModelAndView editUser(@RequestBody @Valid User user,
                                 BindingResult result,
                                 @PathVariable Long id) {

        if (result.hasErrors()) {
            System.out.println("Будем считать, что здесь логи, frontend не справился с проверкой");
            return null;
        }

        //TODO подумать, что с этим можно сделать в последней версии, пока переадресация на страницу пользователя
        String enterEmail = user.getEmail();
        if (userService.save(user).getEmail() == "") {
            System.out.println("Такой email существует: " + enterEmail);
            return null;
        }

        return null;
    }

    @DeleteMapping(value = "/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return null;
    }

}
