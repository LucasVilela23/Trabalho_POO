public class Usuario {
    private String username;
    private String senha;
    private boolean isAdmin;

    public Usuario(String username, String senha, boolean isAdmin) {
        this.username = username;
        this.senha = senha;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getSenha() {
        return senha;
    }

    public boolean validarSenha(String senha) {
        return this.senha.equals(senha);
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
