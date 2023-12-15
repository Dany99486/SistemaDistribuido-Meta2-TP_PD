package pt.isec.pd.spring_boot.exemplo3.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pt.isec.pd.spring_boot.exemplo3.models.BD;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider
{
    String[] args = {"5100", "dataBase", "asas", "5200"};
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        System.out.println("authenticate");
        List<GrantedAuthority> authorities = new ArrayList<>();
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        int userexists = BD.checkClientIfExists(username, password,args, "serverdatabase.db");
        System.out.println("userexists: " + userexists);
        if (userexists == 1) {

            authorities.add(new SimpleGrantedAuthority("ADMIN"));
            System.out.println("Bem vindo admin");
            return new UsernamePasswordAuthenticationToken(username, password, authorities);
        } else if (userexists == 2) {
            authorities.add(new SimpleGrantedAuthority("USER"));
            System.out.println("Bem vindo user");
            return new UsernamePasswordAuthenticationToken(username, password, authorities);
        }
        return new UsernamePasswordAuthenticationToken(username, password, authorities);


        //return null;
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
