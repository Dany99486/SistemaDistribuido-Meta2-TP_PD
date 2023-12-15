package pt.isec.pd.spring_boot.exemplo3.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.pd.spring_boot.exemplo3.models.Evento;
import pt.isec.pd.spring_boot.exemplo3.utils.AuthUtils;

import java.util.Objects;

@RestController
public class ConsultasController {
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

    @GetMapping("/Consulta")
    public ResponseEntity getComandString(Authentication authentication,
                                          @RequestParam(value = "arg1") String arg1,
                                          @RequestParam(value = "arg2", required=false) String arg2,
                                          @RequestParam(value = "arg3", required = false) String arg3,
                                          @RequestParam(value = "arg4", required = false) String arg4,
                                          @RequestParam(value = "arg5", required = false) String arg5){


        String resultado = "Erro";
        String subject = authentication.getName();
        Jwt userDetails = (Jwt) authentication.getPrincipal();
        String scope = userDetails.getClaim("scope");
        String Username = authentication.getName();

        String cc= Evento.getCCFromUsername(Username,args, "serverdatabase.db");
        //Cliente
        if (Objects.equals(arg1, PRESENCAS) &&arg3==null){
            if (arg2==null) arg2="sem_filtro";
            resultado=Evento.consultaPresencasClienteFiltro(cc,arg2, args, "serverdatabase.db");

        }
        //Admin
        if(AuthUtils.isAdmin(authentication)){
            if(Objects.equals(arg1, EVENTO) && Objects.equals(arg2, CONSULTA) && arg5==null) {
                if (arg4 == null) arg4 = "sem_filtro";
                resultado=Evento.consultaEventoFiltro(arg3, arg4, args, "serverdatabase.db");
            } else if(Objects.equals(arg1, PRESENCAS) && arg3==null) {
                resultado=Evento.consultaPresenca(arg2, args, "serverdatabase.db");
            }
        }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");

        System.out.println(resultado);
        return ResponseEntity.ok(resultado);
    }
}
