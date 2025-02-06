
//Visão Geral

//Este sistema permite o gerenciamento de usuários, agendamento de coletas e geração de comprovantes. O sistema possui funcionalidades para administradores e usuários comuns.
// A interface gráfica é feita com a biblioteca Swing do Java, permitindo uma interação simples e intuitiva.
//Estrutura do Projeto

//O projeto está dividido em três classes principais:

   // Usuario.java: Contém a definição da classe Usuario, que representa os usuários do sistema, incluindo seus dados (nome de usuário, senha e status de administrador).
    //Sistema.java: Contém a lógica principal do sistema, como o gerenciamento de usuários, login, cadastro, agendamento de coletas e geração de comprovantes.
    //Main.java: Ponto de entrada do sistema, onde o programa é inicializado.

//Funcionalidades

   // Cadastro e Login: O sistema permite que os usuários se cadastrem e façam login usando um nome de usuário e senha. O cadastro salva os dados no arquivo usuarios.csv.
    
    //Agendamento de Coletas: Os usuários podem agendar coletas para diferentes dias da semana e confirmar a retirada do lixo.
   
   // Comprovantes: Após confirmar a retirada do lixo, o sistema gera um comprovante com a data e hora da coleta.
    
    //Agenda de Coletas: A agenda é organizada por dia da semana, e os administradores podem visualizar e gerenciar as coletas agendadas.
    
    //Administração: Administradores têm acesso a uma interface para ver registros de comprovantes e confirmar retiradas.

//  ALGUMAS CONSIDERAÇÕES:
    //Persistência via CSV:
       //Os dados de login (usuário, senha e se é administrador) são salvos no arquivo usuarios.csv. Assim, 
       //novos usuários são persistidos e poderão ser carregados em execuções futuras.

//Dependências Externas:
//Essa abordagem não requer bibliotecas externas como o Apache POI(teste ela porém não teve um desempelho legal), sendo uma solução simples para pequenos sistemas.


public class Main {
    public static void main(String[] args) {
        new Sistema();
    }
}
