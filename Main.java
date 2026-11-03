package chess;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Digite o tabuleiro a ser usado.\n1 - Padrão\n2 - Teste Roque\n3 - Teste en passant");
		int opcao = sc.nextInt();
		if(opcao != 1 && opcao != 2 && opcao != 3) {
			System.out.println("Opção inválida.");
			System.out.println("Digite o tabuleiro a ser usado.\n1 - Padrão\n2 - Teste Roque\n3 - Teste en passant");
		}
		sc.nextLine();
		
		Chess xadrez = new Chess(opcao);
		
		System.out.println(xadrez.getTabuleiroFormatado(xadrez.getTabuleiroGlobal()));
		
		 while(xadrez.getVitoria().equals("")) {
			boolean resultado = false;
			System.out.println("Turno atual: " + xadrez.getTurnoAtual());

			xadrez.enPassantUpkeep();
			
			while (!resultado) {
				System.out.println("Digite as coordenadas da origem da peça que deseja mover:");
				String coordenadasAlgebricasOrigem = sc.nextLine();
				int[] coordenadasYXOrigem = xadrez.notacaoAlgebricaParaCoordenada(coordenadasAlgebricasOrigem);
				
				if(xadrez.getPecaCor(coordenadasYXOrigem[0], coordenadasYXOrigem[1]) != xadrez.getTurnoAtual() 
						&& xadrez.getPecaCor(coordenadasYXOrigem[0], coordenadasYXOrigem[1]) != "") {
					System.out.print("Você selecionou uma peça do oponente. ");
					coordenadasYXOrigem = null;
				}
				if(xadrez.getPecaNome(coordenadasYXOrigem[0], coordenadasYXOrigem[1]).equals("")){
					System.out.print("Você não selecionou nenhuma peça. ");
					coordenadasYXOrigem = null;
				}
				while (coordenadasYXOrigem == null) {
					System.out.println("Coordenadas inválidas.");
					
					System.out.println("Digite as coordenadas da origem da peça que deseja mover:");
					coordenadasAlgebricasOrigem = sc.nextLine();
					coordenadasYXOrigem = xadrez.notacaoAlgebricaParaCoordenada(coordenadasAlgebricasOrigem);
				}

				int pecaYOrigem = coordenadasYXOrigem[0];
				int pecaXOrigem = coordenadasYXOrigem[1];

				System.out.println("Peça escolhida: " + xadrez.getPecaNome(pecaYOrigem, pecaXOrigem));

				System.out.println("Digite as coordenadas do destino da peça que deseja mover:");
				String coordenadasAlgebricasDestino = sc.nextLine();
				int[] coordenadasYXDestino = xadrez.notacaoAlgebricaParaCoordenada(coordenadasAlgebricasDestino);
				
				while (coordenadasYXDestino == null) {
					System.out.println("Coordenadas invalidas");
					
					System.out.println("Digite as coordenadas do destino da peça que deseja mover:");
					coordenadasAlgebricasDestino = sc.nextLine();
					coordenadasYXDestino = xadrez.notacaoAlgebricaParaCoordenada(coordenadasAlgebricasDestino);
				}
				
				int pecaYDestino = coordenadasYXDestino[0];
				int pecaXDestino = coordenadasYXDestino[1];

				System.out.println("movendo peça de " + pecaYOrigem + "," + pecaXOrigem + " para " + pecaYDestino + ","
						+ pecaXDestino);
				
				resultado = xadrez.moverPiece(pecaYOrigem, pecaXOrigem, pecaYDestino, pecaXDestino);
				if (resultado) {
					System.out.println("Movimento legal.");
					System.out.println(xadrez.getTabuleiroFormatado(xadrez.getTabuleiroGlobal()));
				} else {
					System.out.println("Movimento ilegal.");
				}
				
				System.out.println("");

			}
			
			xadrez.checarCheckMate();
			if(xadrez.getVitoria().equals("")) {
				System.out.println("Passando para proximo turno!");
			}
		}
		
		sc.close();
		System.out.println("Encerrando o programa.");
	}
}

