package pt.isec.pd.spring_boot.exemplo3.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.isec.pd.spring_boot.exemplo3.models.BD;
import pt.isec.pd.spring_boot.exemplo3.utils.AuthUtils;

@RestController
public class RegisterController {
    String[] args = {"5100", "dataBase", "asas", "5200"};

    @GetMapping("/role")
    public ResponseEntity role(Authentication authentication){

        if (AuthUtils.isAdmin(authentication)) return ResponseEntity.ok("Olá Admin!");
        else return ResponseEntity.ok("Olá Cliente!");

    }
    @PostMapping("/register")
    public ResponseEntity<String> register(Authentication authentication,
                                           @RequestParam(value = "name", required = true) String name,
                                           @RequestParam(value="password", required=true) String password,
                                           @RequestParam(value="cc", required=true) String cc,
                                           @RequestParam(value="email", required=true) String email) {
        // Verifique se o usuário já existe
        int registed= BD.registClient(name, password, cc, email,args, "serverdatabase.db");
        if ( registed== 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        } else if (registed == 1) {
            return ResponseEntity.ok("User registered successfully");
        }
        System.out.println("registed: " + registed);


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
    }

}



