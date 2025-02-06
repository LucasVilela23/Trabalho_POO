import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Sistema {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private java.util.List<String> registros = new ArrayList<>();
    private Map<String, java.util.List<String>> agenda = new HashMap<>();
    private JFrame mainFrame;
    private final String csvFile = "usuarios.csv";

    public Sistema() {
        loadUsuariosFromCSV();
        criarJanelaLogin();
    }

    private void loadUsuariosFromCSV() {
        File file = new File(csvFile);
        if (!file.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.println("username,senha,isAdmin");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;
                String username = parts[0];
                String senha = parts[1];
                boolean isAdmin = Boolean.parseBoolean(parts[2]);
                usuarios.put(username, new Usuario(username, senha, isAdmin));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveUsuarioToCSV(Usuario user) {
        File file = new File(csvFile);
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            String linha = user.getUsername() + "," + user.getSenha() + "," + user.isAdmin();
            pw.println(linha);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void criarJanelaLogin() {
        JFrame frame = new JFrame("Login");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Usuário:");
        JTextField userField = new JTextField(12);
        JLabel passLabel = new JLabel("Senha:");
        JPasswordField passField = new JPasswordField(12);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Cadastrar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(userLabel, gbc);

        gbc.gridx = 1;
        frame.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(passLabel, gbc);

        gbc.gridx = 1;
        frame.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(loginButton, gbc);

        gbc.gridx = 1;
        frame.add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String senha = new String(passField.getPassword());
            if (usuarios.containsKey(username) && usuarios.get(username).validarSenha(senha)) {
                frame.dispose();
                criarJanelaPrincipal(username, usuarios.get(username).isAdmin());
            } else {
                JOptionPane.showMessageDialog(frame, "Usuário ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            frame.dispose();
            criarJanelaCadastro();
        });

        frame.setVisible(true);
    }

    private void criarJanelaCadastro() {
        JFrame frame = new JFrame("Cadastro");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Usuário:");
        JTextField userField = new JTextField(12);
        JLabel passLabel = new JLabel("Senha:");
        JPasswordField passField = new JPasswordField(12);
        JCheckBox adminCheckBox = new JCheckBox("Administrador");
        JButton registerButton = new JButton("Cadastrar");
        JButton backButton = new JButton("Voltar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(userLabel, gbc);

        gbc.gridx = 1;
        frame.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(passLabel, gbc);

        gbc.gridx = 1;
        frame.add(passField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(adminCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(registerButton, gbc);

        gbc.gridx = 1;
        frame.add(backButton, gbc);

        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String senha = new String(passField.getPassword());
            boolean isAdmin = adminCheckBox.isSelected();

            if (username.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario novoUsuario = new Usuario(username, senha, isAdmin);
            usuarios.put(username, novoUsuario);
            saveUsuarioToCSV(novoUsuario);
            JOptionPane.showMessageDialog(frame, "Cadastro realizado com sucesso!");
            frame.dispose();
            criarJanelaLogin();
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            criarJanelaLogin();
        });

        frame.setVisible(true);
    }

    private void criarJanelaPrincipal(String username, boolean isAdmin) {
        if (mainFrame != null) {
            mainFrame.dispose();
        }

        mainFrame = new JFrame("Sistema de Controle");
        mainFrame.setSize(600, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel painelUsuario = criarPainelUsuario(username);
        tabbedPane.addTab("Usuário", painelUsuario);

        if (isAdmin) {
            JPanel painelAdmin = criarPainelAdmin();
            tabbedPane.addTab("Administração", painelAdmin);
        }

        mainFrame.add(tabbedPane);
        mainFrame.setVisible(true);
    }

    private String getCurrentDayPortuguese() {
        DayOfWeek diaSemana = LocalDate.now().getDayOfWeek();
        switch (diaSemana) {
            case MONDAY:    return "Segunda";
            case TUESDAY:   return "Terça";
            case WEDNESDAY: return "Quarta";
            case THURSDAY:  return "Quinta";
            case FRIDAY:    return "Sexta";
            case SATURDAY:  return "Sábado";
            case SUNDAY:    return "Domingo";
            default:        return "";
        }
    }

    private JPanel criarPainelUsuario(String username) {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(7, 1));

        String[] dias = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"};
        JComboBox<String> diaBox = new JComboBox<>(dias);
        JButton confirmarDia = new JButton("Confirmar Dia");
        JLabel coletaLabel = new JLabel("Nenhuma coleta confirmada.");
        JButton botaoRetirarLixo = new JButton("Retirei o Lixo");
        JButton gerarComprovante = new JButton("Gerar Comprovante");
        JButton verAgenda = new JButton("Ver Agenda");

        botaoRetirarLixo.setEnabled(false);
        gerarComprovante.setEnabled(false);

        painel.add(new JLabel("Escolha o dia da coleta:"));
        painel.add(diaBox);
        painel.add(confirmarDia);
        painel.add(coletaLabel);
        painel.add(botaoRetirarLixo);
        painel.add(gerarComprovante);
        painel.add(verAgenda);

        final String[] confirmedDay = {null};

        confirmarDia.addActionListener(e -> {
            String dia = (String) diaBox.getSelectedItem();
            confirmedDay[0] = dia;
            coletaLabel.setText("Coleta agendada para: " + dia + " | Usuário: " + username);
            agenda.computeIfAbsent(dia, d -> new ArrayList<>()).add(username);
            if (dia != null) {
                botaoRetirarLixo.setEnabled(true);
            }
        });

        botaoRetirarLixo.addActionListener(e -> {
            JOptionPane.showMessageDialog(painel, "Coleta confirmada para " + confirmedDay[0]);
            botaoRetirarLixo.setEnabled(false);
            gerarComprovante.setEnabled(true);
        });

        gerarComprovante.addActionListener(e -> {
            String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String comprovante = "Coleta realizada: " + confirmedDay[0] + " - " + dataHora;
            registros.add(comprovante);
            JOptionPane.showMessageDialog(painel, "Comprovante gerado com sucesso!");
        });

        verAgenda.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Agenda de Coletas:\n");
            for (Map.Entry<String, java.util.List<String>> entry : agenda.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            JOptionPane.showMessageDialog(painel, sb.toString(), "Agenda de Coletas", JOptionPane.INFORMATION_MESSAGE);
        });

        return painel;
    }

    private JPanel criarPainelAdmin() {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(3, 1));

        JButton verRegistros = new JButton("Ver Registros");
        JButton confirmarRetirada = new JButton("Confirmar Retirada");

        painel.add(verRegistros);
        painel.add(confirmarRetirada);

        verRegistros.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Registros de Comprovantes:\n");
            if (registros.isEmpty()) {
                sb.append("Nenhum registro encontrado.");
            } else {
                for (String registro : registros) {
                    sb.append(registro).append("\n");
                }
            }
            JOptionPane.showMessageDialog(painel, sb.toString(), "Registros", JOptionPane.INFORMATION_MESSAGE);
        });

        confirmarRetirada.addActionListener(e -> {
            JOptionPane.showMessageDialog(painel, "Retirada confirmada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });

        return painel;
    }

    public static void main(String[] args) {
        new Sistema();
    }
}
