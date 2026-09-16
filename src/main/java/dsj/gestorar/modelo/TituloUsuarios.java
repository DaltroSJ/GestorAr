package dsj.gestorar.modelo;

import dsj.gestorar.seguranca.acesso.Permissoes;
import dsj.gestorar.seguranca.acesso.PoliticasPermissao;

public enum TituloUsuarios {

    ADMINISTRADOR(99, "Usuários com poder máximo, tem acesso total ao sistema", Permissoes.ADMIN),
    OPERACIONAL(1, "Usuarios que podem administrar as funções de (PAs, Agentes, Gestores e seu próprio usuário), as demais áreas são como observador", Permissoes.OPERADOR),
    TECNICO(2, "Todas as funções do sistema exceto(Usuários)", Permissoes.TECNICO),
    OBSERVADOR(0, "Pode tirar relatórios e acessar dados do sistema, porém não pode alterar nada", Permissoes.OBSERVADOR);

    private final int codigo;
    private final String descricao;
    private final PoliticasPermissao politica;

    TituloUsuarios(int codigo, String descricao, PoliticasPermissao politica) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.politica = politica;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public PoliticasPermissao getPolitica() {
        return politica;
    }
    
    public static TituloUsuarios fromCodigo(int codigo) {
    	
        for (TituloUsuarios titulo : values()) {
        	
            if (titulo.codigo == codigo) {
            	
                return titulo;
                
            }
            
        }
        
        return OBSERVADOR;
    }
    
}
