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

       // essa venderProduto está vendendo produto já vendido, por isso coloquei o novo código.
       
  /*     public void venderProduto(int id) {
    Connection conn = null;
    PreparedStatement prep = null;
    
    // Comando SQL para atualizar o status do produto com base no ID recebido
    String sql = "UPDATE produtos SET status = ? WHERE id = ?";
    
    try {
        conn = new conectaDAO().connectDB();
        prep = conn.prepareStatement(sql);
        
        // Substitui as interrogações pelos valores correspondentes
        prep.setString(1, "Vendido");
        prep.setInt(2, id);
        
        // Executa a atualização no banco de dados
        prep.executeUpdate(); 
        
        JOptionPane.showMessageDialog(null, "Produto vendido com sucesso!");
        
    } catch (java.sql.SQLException erro) {
        JOptionPane.showMessageDialog(null, "Erro ao vender produto no DAO: " + erro.getMessage());
    } finally {
        // Garante o fechamento das conexões para não travar o banco
        try { if (prep != null) prep.close(); } catch (java.sql.SQLException e) { }
        try { if (conn != null) conn.close(); } catch (java.sql.SQLException e) { }
    }
} */
       public void venderProduto(int id) {
    Connection conn = null;
    PreparedStatement prep = null;
    java.sql.ResultSet rs = null; // Linha nova para ler o status do banco
    
    try {
        conn = new conectaDAO().connectDB();
        
        // 1. PASSO NOVO: Busca o status atual do produto no banco de dados
        String sqlCheck = "SELECT status FROM produtos WHERE id = ?";
        prep = conn.prepareStatement(sqlCheck);
        prep.setInt(1, id);
        rs = prep.executeQuery();
        
        if (rs.next()) {
            String statusAtual = rs.getString("status");
            
            // 2. PASSO NOVO: Se o status já for "Vendido", mostra o aviso e para tudo
            if (statusAtual != null && statusAtual.equalsIgnoreCase("Vendido")) {
                JOptionPane.showMessageDialog(null, "Este produto já foi vendido!");
                return; // O 'return' faz o código parar aqui e não deixa executar o UPDATE abaixo
            }
        } else {
            JOptionPane.showMessageDialog(null, "Produto não encontrado!");
            return;
        }
        
        // Fecha o comando de checagem para poder abrir o comando de atualização
        prep.close();
        
        // 3. SEU CÓDIGO ORIGINAL: Atualiza para "Vendido" se o produto estava liberado
        String sql = "UPDATE produtos SET status = ? WHERE id = ?";
        prep = conn.prepareStatement(sql);
        
        prep.setString(1, "Vendido");
        prep.setInt(2, id);
        
        prep.executeUpdate(); 
        JOptionPane.showMessageDialog(null, "Produto vendido com sucesso!");
        
    } catch (java.sql.SQLException erro) {
        JOptionPane.showMessageDialog(null, "Erro ao vender produto no DAO: " + erro.getMessage());
    } finally {
        // Garante o fechamento de tudo o que foi aberto
        try { if (rs != null) rs.close(); } catch (java.sql.SQLException e) { }
        try { if (prep != null) prep.close(); } catch (java.sql.SQLException e) { }
        try { if (conn != null) conn.close(); } catch (java.sql.SQLException e) { }
    }
}


       public ArrayList<ProdutosDTO> listarProdutosVendidos() {
    ArrayList<ProdutosDTO> listagemVendidos = new ArrayList<>();
    
    Connection conn = null;
    PreparedStatement prep = null;
    ResultSet resultset = null;
    
    // Comando SQL com o filtro WHERE para buscar apenas os vendidos
    String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";
    
    try {
        conn = new conectaDAO().connectDB();
        prep = conn.prepareStatement(sql);
        resultset = prep.executeQuery();
        
        while (resultset.next()) {
            ProdutosDTO produto = new ProdutosDTO();
            
            produto.setId(resultset.getInt("id")); 
            produto.setNome(resultset.getString("nome"));
            produto.setValor(resultset.getInt("valor"));
            produto.setStatus(resultset.getString("status"));
            
            listagemVendidos.add(produto);
        }
        
    } catch (java.sql.SQLException erro) {
        JOptionPane.showMessageDialog(null, "Erro ao listar produtos vendidos no DAO: " + erro.getMessage());
    } finally {
        // Fecha as conexões com segurança
        try { if (resultset != null) resultset.close(); } catch (java.sql.SQLException e) { }
        try { if (prep != null) prep.close(); } catch (java.sql.SQLException e) { }
        try { if (conn != null) conn.close(); } catch (java.sql.SQLException e) { }
    }
    
    return listagemVendidos;
}

    
    
        
}

