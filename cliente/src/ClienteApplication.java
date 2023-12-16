import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;

public class ClienteApplication {

    public static String sendRequestAndShowResponse(String uri, String verb, String authorizationValue) throws MalformedURLException, IOException {

        String responseBody = null;
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(verb);
        connection.setRequestProperty("Accept", "application/xml, */*");

        if(authorizationValue!=null) {
            connection.setRequestProperty("Authorization", authorizationValue);
        }

        connection.connect();

        int responseCode = connection.getResponseCode();
        //System.out.println("Response code: " +  responseCode + " (" + connection.getResponseMessage() + ")");
        Scanner s;

        if(connection.getErrorStream()!=null) {
            s = new Scanner(connection.getErrorStream()).useDelimiter("\\A");
            responseBody = s.hasNext() ? s.next() : null;
        }

        try {
            s = new Scanner(connection.getInputStream()).useDelimiter("\\A");
            responseBody = s.hasNext() ? s.next() : null;
        } catch (IOException e){}

        connection.disconnect();

        //System.out.println(verb + " " + uri + " -> " + responseBody);
        //System.out.println();

        return responseBody;
    }

    public static void main(String args[]) throws MalformedURLException, IOException {
        Scanner scanner = new Scanner(System.in);

        String uri;

        String helloUri = "http://localhost:8080/hello/fr?name=Jeanne";
        String helloUri2 = "http://localhost:8080/hello/gr?name=Jeanne";
        String helloUri3 = "http://localhost:8080/hello/UK?name=Danny&age=20";
        String loginUri = "http://localhost:8080/login";
        String loremUri = "http://localhost:8080/lorem?type=paragraph";

        System.out.println("============================================\n");

        do {
            String token;
            do {

                System.out.println("Escolha uma opção:");
                System.out.println("1. Autenticar");
                System.out.printf("2. Registar\n->");
                int choice = scanner.nextInt();
                if (choice==1) {
                    System.out.print("Email:\n->");
                    String email = scanner.next();
                    System.out.print("Password:\n->");
                    String password = scanner.next();
                    String bytes64 = Base64.getEncoder().encodeToString((email+":"+password).getBytes());
                    //System.out.println("Autenticar "+email+password+ "="+bytes64);
                    token = sendRequestAndShowResponse(loginUri, "POST","basic "+bytes64);
                    //System.out.println("Token: "+token);

                    if (token!=null&&isAdmin(token)) {

                        System.out.println("\n[Bem vindo admin!]\n");
                        break;
                    } else if (token!=null) {
                        System.out.println("\n[Bem vindo user!]\n");
                        break;
                    } else {
                        System.out.println("\n[Credenciais inválidas!]\n");
                        System.out.println("============================================\n");

                    }
                    //TODO: Autenticar
                } else if (choice==2) {
                    System.out.print("Nome:\n->");
                    String nome = scanner.next();
                    System.out.print("Email:\n->");
                    String email = scanner.next();
                    System.out.print("Password:\n->");
                    String password = scanner.next();
                    System.out.print("Cartao de Cidadao:\n->");
                    String cc = scanner.next();
                    uri = "http://localhost:8080/register?name="+nome+"&password="+password+"&cc="+cc+"&email="+email;
                    System.out.println(uri);
                    String response=sendRequestAndShowResponse(uri, "POST",null);
                    System.out.println("server response:"+response);

                    //TODO: Registar

                } else {
                    System.out.println("\n[Opção inválida]\n");
                    System.out.println("============================================\n");
                }

            }while (true);//login ou register
            System.out.println("============================================\n");

            do {
                System.out.println("Escolha uma opção:");
                System.out.println("1. Receba ola");
                System.out.println("2. Submeter codigo de presenca");
                System.out.println("3. Consulte as suas presencas(com filtro)");
                if (isAdmin(token)){
                    System.out.println("4. Criar evento");
                    System.out.println("5. Eliminar eventos");
                    System.out.println("6. Consultar eventos(com filtro)");
                    System.out.println("7. Gerar codigo de presenca");
                    System.out.println("8. Consultar presencas num evento");
                    System.out.println("9. Sair");
                }else System.out.println("4. Sair");
                System.out.print("->");
                int choice = scanner.nextInt();
                switch (choice){
                    /*case 1:
                        uri = "http://localhost:8080/hello/fr?name=Jeanne";
                        System.out.println(sendRequestAndShowResponse(uri, "GET", "bearer "+token));
                        break;
                    case 2:
                        System.out.print("Codigo de presenca:\n->");
                        String code = scanner.next();
                        uri = "http://localhost:8080/submit?code="+code;
                        System.out.println(sendRequestAndShowResponse(uri, "POST", "bearer "+token));
                        break;*/
                }
                System.out.println("\n============================================\n");

            }while (true);//funcionalidades

        }while (true);


        /*System.out.println();
        System.out.println("========== Exemplo de utilização da API REST com segurança ==========");
        System.out.println();

        //OK
        sendRequestAndShowResponse(helloUri, "GET", null);

        //Língua "gr" não suportada
        sendRequestAndShowResponse(helloUri2, "GET", null);
        sendRequestAndShowResponse(helloUri3, "GET", null);
        System.out.println("=====================================================================\n");

        //Falta um campo "Authorization: basic ..." válido no cabeçalho do pedido para autenticação básica
        String token = sendRequestAndShowResponse(loginUri, "POST",null);

        //OK
        token = sendRequestAndShowResponse(loginUri, "POST","basic YWRtaW46MTIz"); //Base64(admin:admin)
        System.out.println("=====================================================================\n");

        //Falta um campo "Authorization: bearer ..." no cabeçalho do pedido com um token JWT válido
        sendRequestAndShowResponse(loremUri, "GET", null);
        //OK
        sendRequestAndShowResponse(loremUri, "GET", "bearer " + token);

        //POST não suportado para esta URI
        sendRequestAndShowResponse(loremUri, "POST", "bearer " + token);
        System.out.println("=====================================================================");*/

    }
    public static boolean isAdmin(String jwtToken) {
        try {
            String[] parts = jwtToken.split("\\.");
            String payloadBase64 = parts[1];

            String decodedPayload = new String(Base64.getUrlDecoder().decode(payloadBase64), "UTF-8");

            JsonObject payloadJson = JsonParser.parseString(decodedPayload).getAsJsonObject();

            if (payloadJson.has("scope"))
                return Objects.equals(payloadJson.get("scope").getAsString(), "ADMIN");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void clearConsole() {
        // Imprime várias linhas em branco para criar uma aparência de limpeza
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
    }
}
