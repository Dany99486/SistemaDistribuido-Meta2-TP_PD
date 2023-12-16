package pt.isec.pd.spring_boot.exemplo3.models;

import java.io.File;
import java.sql.*;

public class BD {
    private static final String ADMIN = "admin";
    private static final String USER = "user";


    //TODO: Criar admin
    //TODO: Criar base de dados se nao existir
    public static void createBDIfNotExists(String[] args, String BDFileName) {

        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;


        try {
            // Estabelece a conexão com a base de dados ou cria uma nova se não existir
            System.out.println(url);
            System.out.println("\nConectando à base de dados...");
            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                System.out.println("\nConexão com a base de dados estabelecida com sucesso.");
            else {
                System.out.println("\nConexão com a base de dados não foi estabelecida.");
                return;
            }
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS versao (" +
                    "id INTEGER PRIMARY KEY, " +
                    "versao INTEGER " +
                    ")");
            statement.execute("CREATE TABLE IF NOT EXISTS presencas (" +
                    "id INTEGER PRIMARY KEY, " +
                    "idEvento INTEGER REFERENCES eventos (idEvento), " +
                    "idCC TEXT REFERENCES utilizadores (cartaoCidadao)" +
                    ")");
            statement.execute("CREATE TABLE IF NOT EXISTS utilizadores (" +
                    "nome TEXT, " +
                    "cartaoCidadao TEXT PRIMARY KEY, " +
                    "email TEXT UNIQUE, " +
                    "pass TEXT, " +
                    "role TEXT NOT NULL" +
                    ")");
            statement.execute("CREATE TABLE IF NOT EXISTS eventos (" +
                    "idEvento INTEGER PRIMARY KEY, " +
                    "nome TEXT, " +
                    "local TEXT, " +
                    "data TEXT, " +
                    "hora_inicio TEXT, " +
                    "hora_fim TEXT," +
                    "codigo INTEGER," +
                    "code_validade TEXT" +
                    ")");
            //System.out.println("\nTabelas criadas com sucesso.");

            String query = "SELECT * FROM versao ORDER BY versao DESC;";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next())
                System.out.println("\nVersão da base de dados: " + resultSet.getInt("versao"));
            else {
                query = "INSERT INTO versao (versao) VALUES (0);";
                boolean v = statement.execute(query);
                if (v)
                    System.out.println("\nVersão da base de dados: 0");
                else
                    System.out.println("\nVersão da base de dados: 0 não inserida");
            }

            query = "SELECT * FROM utilizadores WHERE role='" + ADMIN + "';";
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                query = "INSERT INTO utilizadores (nome, pass,cartaoCidadao,email,role) VALUES ('admin','123','admin','admin@isec.pt','admin');";
                boolean v = statement.execute(query);
                if (v)
                    System.out.println("\nUtilizador admin criado com sucesso.");
                else
                    System.out.println("\nUtilizador admin não criado.");
            }

            connection.close();

        } catch (SQLException e) {
            System.out.println("\nErro ao conectar à base de dados: " + e.getMessage());
            System.exit(-1);
        }
    }


    /**
     * @return 0-Não existe
     * 1-Existe e é admin
     * 2-Existe e é user
     */
    public static int checkClientIfExists(String user, String pass, String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        int exist = 0;

        try {
            // Estabelece a conexão com a base de dados ou cria uma nova se não existir
            System.out.println(url);
            System.out.println("\nConectando à base de dados...");

            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                System.out.println("\nConexão com a base de dados estabelecida com sucesso.");
            else {
                System.out.println("\nConexão com a base de dados não foi estabelecida.");
                return 0;
            }
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM utilizadores WHERE email='" + user + "' AND pass='" + pass + "';";
            System.out.println(query);
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals(ADMIN)) {
                    exist = 1;
                } else if (role.equals(USER)) {
                    exist = 2;
                }

            }

            connection.close();

        } catch (SQLException e) {
            System.out.println("\nErro ao conectar à base de dados: " + e.getMessage());
        }

        return exist;
    }

    /**
     * @return (0)-Email já usado
     * (1)-Registou
     * (<0)-Erro
     */
    public static int registClient(String user, String passe, String cc, String email, String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        int registed = 0;

        try {
            System.out.println(url);
            System.out.println("\nConectando à base de dados...");

            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                System.out.println("\nConexão com a base de dados estabelecida com sucesso.");
            else {
                System.out.println("\nConexão com a base de dados não foi estabelecida.");
                return -1;
            }
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM utilizadores WHERE email='" + email + "';";
            ResultSet resultSetS = statement.executeQuery(query);
            if (resultSetS.next())//Se já existir retorna 0
                return registed;

            query = "INSERT OR IGNORE INTO utilizadores (nome,pass,cartaoCidadao,email,role) VALUES (?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, passe);
            preparedStatement.setString(3, cc);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, USER);
            int resultSet = preparedStatement.executeUpdate();

            if (resultSet > 0)
                registed = 1;

            connection.close();

        } catch (SQLException e) {
            System.out.println("\nErro ao conectar à base de dados: " + e.getMessage());
            registed = -2;
        }
        return registed;
    }


}