package chess;

public class Piece {
	private String nome;
	private String cor;
	public static final String COR_BRANCA = "\u001B[0;37m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[030m";
	public static final String FUNDO_BRANCO = "\u001B[47m";
	
	public Piece(String nome, String cor){
		this.nome = nome;
		this.cor = cor;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public String getCor() {
		return this.cor;	
	}
	
	String getNomeFormatado(){
		if(this.getCor().equals("branco")){
			return COR_BRANCA + this.nome + ANSI_RESET;
		} if(this.getCor().equals("preto")) {
			return FUNDO_BRANCO + ANSI_RED + this.nome + ANSI_RESET;
		} else return "";
	}
}