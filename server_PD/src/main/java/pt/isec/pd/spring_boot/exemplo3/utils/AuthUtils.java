package pt.isec.pd.spring_boot.exemplo3.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthUtils {

    public static boolean isAdmin(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority it : authorities) {

            if (it.getAuthority().equals("SCOPE_ADMIN"))
                return true;
            System.out.println(it.getAuthority()+"::::::::");
        }
        // Verifica se o usuário possui a role "ADMIN"
        return false;
    }


}
