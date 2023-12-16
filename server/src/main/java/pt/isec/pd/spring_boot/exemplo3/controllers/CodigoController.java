package pt.isec.pd.spring_boot.exemplo3.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pt.isec.pd.spring_boot.exemplo3.models.Evento;
import pt.isec.pd.spring_boot.exemplo3.utils.AuthUtils;

import java.util.Objects;

@RestController
public class CodigoController {
    private static final String DADOS = "DADOS";
    private static final String AUTENTICAR = "AUTENTICAR";
    private static final String LOGOUT = "LOGOUT";
    private static final String CODIGO = "CODIGO";
    private static final String GERAR = "GERAR";
    private static final String EDICAO = "EDICAO";
    private static final String APAGAR = "APAGAR";
    private static final String EMAIL = "EMAIL";
    private static final String CONSULTA = "CONSULTA";
    private static final String EVENTO = "EVENTO";
    private static final String PRESENCAS = "PRESENCAS";
    private static final String PRESENCASUI = "PRESENCASUI";
    private static final String I_AM_ADMIN = "I_AM_ADMIN";



    String[] args = {"5100", "dataBase", "asas", "5200"};
    @PutMapping("/Codigo")
    public ResponseEntity geracodigo(Authentication authentication,
                                          @RequestParam(value = "arg1") String arg1,
                                          @RequestParam(value = "arg2") String arg2,
                                          @RequestParam(value = "arg3") Integer arg3){

        int resultado;
        String subject = authentication.getName();
        Jwt userDetails = (Jwt) authentication.getPrincipal();
        String scope = userDetails.getClaim("scope");
        String Username = authentication.getName();

        String cc= Evento.getCCFromUsername(Username,args, "serverdatabase.db");
        if (Objects.equals(arg1, GERAR)&& AuthUtils.isAdmin(authentication)){
            resultado= Evento.geraCodigo(arg2,arg3, args, "serverdatabase.db");
            if (resultado>0){
                return ResponseEntity.ok("Codigo gerado: "+resultado);
            }
            else if(resultado==-404){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Codigo expirou");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro no servidor");
    }
    @PostMapping("/Codigo")
    public ResponseEntity inserecodigo(Authentication authentication,
                                     @RequestParam(value = "arg1") String arg1,
                                     @RequestParam(value = "arg3") Integer arg3){


        String Username = authentication.getName();

        String cc= Evento.getCCFromUsername(Username,args, "serverdatabase.db");
        if (Objects.equals(arg1,CODIGO)) {
            String res=Evento.insereCodigo(cc,arg3, args, "serverdatabase.db");
            if (res.equals("Erro no servidor")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no servidor");
            }else
                return ResponseEntity.ok(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro no servidor");

    }
}
