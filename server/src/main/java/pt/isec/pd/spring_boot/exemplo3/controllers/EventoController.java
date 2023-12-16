package pt.isec.pd.spring_boot.exemplo3.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.pd.spring_boot.exemplo3.models.Evento;
import pt.isec.pd.spring_boot.exemplo3.utils.AuthUtils;

import java.util.Objects;

@RestController
public class EventoController {
    private static final String DADOS = "DADOS";
    private static final String AUTENTICAR = "AUTENTICAR";
    private static final String LOGOUT = "LOGOUT";
    private static final String REGISTAR = "REGISTAR";
    private static final String CODIGO = "CODIGO";
    private static final String EDICAO = "EDICAO";
    private static final String APAGAR = "APAGAR";
    private static final String EMAIL = "EMAIL";
    private static final String CONSULTA = "CONSULTA";
    private static final String EVENTO = "EVENTO";
    private static final String PRESENCAS = "PRESENCAS";
    private static final String PRESENCASUI = "PRESENCASUI";
    private static final String I_AM_ADMIN = "I_AM_ADMIN";



    String[] args = {"5100", "dataBase", "asas", "5200"};


    @PostMapping("/Evento")
    public ResponseEntity criaEvento(Authentication authentication,
                                            @RequestParam(value = "arg1") String arg1,
                                            @RequestParam(value="arg2") String arg2,
                                            @RequestParam(value = "arg3") String arg3,
                                            @RequestParam(value = "arg4") String arg4,
                                            @RequestParam(value = "arg5") String arg5,
                                            @RequestParam(value = "arg6") String arg6,
                                            @RequestParam(value = "arg7") String arg7){
        /*if (!AuthUtils.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not admin");
        }*/
        String resultado = "Erro";
        String subject = authentication.getName();
        Jwt userDetails = (Jwt) authentication.getPrincipal();
        String scope = userDetails.getClaim("scope");
        String Username = authentication.getName();

        String cc= Evento.getCCFromUsername(Username,args, "serverdatabase.db");
        //Cliente
        //Admin
        if(AuthUtils.isAdmin(authentication)){
           if (Objects.equals(arg1, EVENTO) && arg6 != null) {
               int res=Evento.criaEvento(arg2, arg3, arg4, arg5, arg6,arg7, args, "serverdatabase.db");
                if (res==1) return ResponseEntity.status(HttpStatus.CREATED).body("Evento criado com sucesso!");
                else if (res==0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Evento já existe!");
                else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro do servidor");
           }
        }

        System.out.println(resultado);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
    }

    @DeleteMapping("/Evento")
    public ResponseEntity eliminaEvento(Authentication authentication,
                                     @RequestParam(value = "arg1") String arg1,
                                     @RequestParam(value="arg2") String arg2,
                                     @RequestParam(value = "arg3") String arg3){


        if(AuthUtils.isAdmin(authentication)){
            if (Objects.equals(arg1, EVENTO) && Objects.equals(arg2, APAGAR) && arg3 != null) {
                int res=Evento.eliminaEvento(arg3, args, "serverdatabase.db");
                if (res==1) return ResponseEntity.ok("Evento eliminado com sucesso!");
                else if (res==0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nao existe esse evento!");
                else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro do servidor");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
    }
}
