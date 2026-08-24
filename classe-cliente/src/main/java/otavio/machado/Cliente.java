package otavio.machado;

public class Cliente {
    private int cpf;
    private String nome;
    private int valor;

    public Cliente(int cpf, String nome, int valor){
        this.cpf = cpf;
        this.nome = nome;
        this.valor = valor;
    }

    public int getCpf(){ return cpf; }

    public String getNome() { return nome; }

    public int getValor() { return valor;} 

    

}
