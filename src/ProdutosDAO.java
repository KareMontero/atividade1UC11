import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto (ProdutosDTO produto){
    // 1. Abre a conexão com o banco uc11atividades
    conn = new conectaDAO().connectDB();
    
    // 2. Prepara o comando SQL de inserção
    String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
    
    try {
        // 3. Vincula as interrogações (?) com os dados que vieram da tela (DTO)
        prep = conn.prepareStatement(sql);
        prep.setString(1, produto.getNome());
        prep.setInt(2, produto.getValor());
        prep.setString(3, produto.getStatus());
        
        // 4. Executa e grava definitivamente no banco de dados MySQL
        prep.execute();
        prep.close();
        
    } catch (java.sql.SQLException erro) {
        // Mostra uma mensagem de erro caso o banco de dados rejeite o cadastro
        JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto no DAO: " + erro.getMessage());
    }
}

       public ArrayList<ProdutosDTO> listarProdutos(){
        // Limpa a lista para garantir que os dados não venham duplicados na tela
        listagem.clear();
        
        // Abre a conexão com o banco
        conn = new conectaDAO().connectDB();
        
        // Comando SQL para buscar todos os produtos
        String sql = "SELECT * FROM produtos";
        
        try {
            prep = conn.prepareStatement(sql);
            resultset = prep.executeQuery(); // Executa a busca e guarda o resultado
            
            // Passa por cada linha que o banco de dados retornou
            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                
                // Pega os dados das colunas do MySQL e salva no objeto DTO
                produto.setId(resultset.getInt("id")); 
                produto.setNome(resultset.getString("nome"));
                produto.setValor(resultset.getInt("valor"));
                produto.setStatus(resultset.getString("status"));
                
                // Adiciona o produto preenchido na lista
                listagem.add(produto);
            }
            
            prep.close();
            resultset.close();
            
        } catch (java.sql.SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos no DAO: " + erro.getMessage());
        }
        
        return listagem;
    }

    
    
        
}

