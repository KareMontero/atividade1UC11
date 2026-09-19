# 👉Nome do Projeto

Leilões TDS -  UC11 -  Atividade 1

## 👉Explicação do Projeto

Projeto feito em Java, utilizando Banco de Dados em MySQL e usado para praticar o versionamento local e remoto

## 👉Tecnologias utilizadas

- Java
- MySQL

## Exemplo de código da conexão

```java
public class conectaDAO {
        public Connection connectDB(){
        Connection conn = null;
                try {
        
            conn = DriverManager.getConnection("jdbc:mysql://localhost/uc11?user=root&password=");
               } catch (SQLException erro){
            JOptionPane.showMessageDialog(null, "Erro ConectaDAO" + erro.getMessage());
        }
        return conn;
    }
    }
```
