public class Chess {
	private Piece[][] tabuleiroGlobal = new Piece[8][8];
	private String turnoAtual;

	private int reiPretoY;
	private int reiPretoX;

	private int reiBrancoY;
	private int reiBrancoX;
	
	private boolean moveuReiBranco;
	private boolean moveuTorreEsqBranco;
	private boolean moveuTorreDirBranco;
	
	private boolean moveuReiPreto;
	private boolean moveuTorreEsqPreto;
	private boolean moveuTorreDirPreto;
	
	private String enPassant;
	

	private static final String notacaoAlgebricaGlobal = "abcdefgh";

	private boolean vitoriaBranco;
	private boolean vitoriaPreto;
	
	private static final String[][] tabuleiroPadraoString = { 
			{ "To", "Ca", "Bi", "Re", "Ra", "Bi", "Ca", "To" },
			{ "Pe", "Pe", "Pe", "Pe", "Pe", "Pe", "Pe", "Pe" }, 
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " },
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " }, 
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " },
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " }, 
			{ "Pe", "Pe", "Pe", "Pe", "Pe", "Pe", "Pe", "Pe" },
			{ "To", "Ca", "Bi", "Re", "Ra", "Bi", "Ca", "To" }, 
			};
	
	private static final String[][] tabuleiroTesteRoque = { 
			{ "To", "", "  ", "Re", "", "", "", "To" },
			{ "Pe", "Pe", "Pe", "Pe", "Pe", "Pe", "Pe", "Pe" }, 
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " },
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " }, 
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " },
			{ "  ", "  ", "  ", "Bi", "  ", "  ", "  ", "  " }, 
			{ "  ", "  ", "  ", "  ", "Pe", "Pe", "Pe", "Pe" },
			{ "To", "  ", "  ", "Re", "  ", "  ", "  ", "To" }, 
			};
	
	private static final String[][] tabuleiroTesteEnPassant = { 
			{ "To", "", "  ", "Re", "", "", "", "To" },
			{ "Pe", "Pe", "Pe", "Pe", "Pe", "Pe", "Pe", "Pe" }, 
			{ "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  " },
			{ "  ", "", "  ", "  ", "  ", "  ", "  ", "Pe" }, 
			{ "  ", "Pe", "  ", "  ", "  ", "  ", "  ", "  " },
			{ "  ", "  ", "  ", "", "  ", "  ", "  ", "  " }, 
			{ "Pe", "  ", "Pe", "  ", "", "", "", "" },
			{ "", "  ", "  ", "Re", "  ", "  ", "  ", "" }, 
			};
	
	public Chess(int escolhaTabuleiro) {
		this.tabuleiroGlobal = new Piece[8][8];
		this.turnoAtual = "branco";
		
		this.vitoriaBranco = false;
		this.vitoriaPreto = false;
		
		this.enPassant = "";
		
		this.moveuReiBranco = false;
		this.moveuTorreEsqBranco = false;
		this.moveuTorreDirBranco = false;
		
		this.moveuReiPreto = false;
		this.moveuTorreEsqPreto = false;
		this.moveuTorreDirPreto = false;
		
		if(escolhaTabuleiro == 1) { 
			tabuleiroGlobal = tabuleiroStringParaPiece(tabuleiroPadraoString, 4);
		}
		if(escolhaTabuleiro == 2) {
			tabuleiroGlobal = tabuleiroStringParaPiece(tabuleiroTesteRoque, 5);
		}
		if(escolhaTabuleiro == 3) {
			tabuleiroGlobal = tabuleiroStringParaPiece(tabuleiroTesteEnPassant, 5);
		}
	}
	
	public String getVitoria() {
		if(vitoriaBranco) return "branco";
		if(vitoriaPreto) return "preto";
		return "";
	}

	public void enPassantUpkeep() {
		if(this.enPassant.equals(this.turnoAtual)) {
			this.enPassant = "";
		}
	}
	
	public int[] notacaoAlgebricaParaCoordenada(String notacao) {
		notacao = notacao.stripIndent().strip();
		if (notacao.length() != 2) return null;
		if (!Character.isLetter(notacao.charAt(0)) || !Character.isDigit(notacao.charAt(1)))
			return null;

		int[] coordenadas = new int[2];

		int pecaY = (Character.getNumericValue(notacao.charAt(1)) - 8) * -1;
		int pecaX = notacaoAlgebricaGlobal.indexOf(notacao.charAt(0));
		coordenadas[0] = pecaY;
		coordenadas[1] = pecaX;

		return coordenadas;
	}
	
	public String getEnPassant() {
		return this.enPassant;
	}

	
	public void checarCheckMate() {
		if(turnoAtual.equals("branco") && !validarReiCheckMate(reiBrancoY, reiBrancoX)) {
			System.out.println("Vitoria preto");
			vitoriaPreto = true;
			return;
		}
		if(turnoAtual.equals("preto") && !validarReiCheckMate(reiPretoY, reiPretoX)) {
			System.out.println("Vitoria branco");
			vitoriaBranco = true;
			return;
		}
	}
	
	public Piece[][] getTabuleiroGlobal(){
		return this.tabuleiroGlobal;
	}
	
	private boolean validarReiCheckMate(int reiY, int reiX) {
		for (int i = 0; i < tabuleiroGlobal.length; i++) {
			for (int j = 0; j < tabuleiroGlobal[0].length; j++) {
				if (tabuleiroGlobal[i][j] == null)
					continue;
				if (tabuleiroGlobal[i][j].getCor().equals(tabuleiroGlobal[reiY][reiX].getCor()))
					continue;

				String nome = tabuleiroGlobal[i][j].getNome();

				if (nome.equals("Ca") || nome.equals("Pe") || nome.equals("Re")) {
					if (!validarPeao(tabuleiroGlobal, i, j, reiY, reiX)
							&& !validarCavalo(tabuleiroGlobal, i, j, reiY, reiX)
							&& !validarRei(tabuleiroGlobal, i, j, reiY, reiX)) {
						continue;
					}
					
					for (int k = 0; k < tabuleiroGlobal.length; k++) {
						for (int l = 0; l < tabuleiroGlobal[0].length; l++) {
							if (validarMovimentoPeca(k, l, i, j, reiY, reiX)) {
								return true;
							}
						}
					}

					for (int k = reiY - 1; k < reiY + 1; k++) {
						for (int l = reiX - 1; l < reiX + 1; l++) {
							if (k == reiY && l == reiX)
								continue;
							if (validarMovimentoPeca(reiY, reiX, k, l, k, l)) {
								return true;
							}
						}
					}
					System.out.println("Cheque cavalo ou peao ou rei?");
					return false;
				}

				if (nome.equals("To") || nome.equals("Ra") || nome.equals("Bi")) {
					int[][] caminho = null;
					if (nome.equals("Ra"))
						caminho = validarRainha(tabuleiroGlobal, i, j, reiY, reiX);
					if (nome.equals("Bi"))
						caminho = validarBispo(tabuleiroGlobal, i, j, reiY, reiX);
					if (nome.equals("To"))
						caminho = validarTorre(tabuleiroGlobal, i, j, reiY, reiX);
					if (caminho != null) {
						for (int k = 0; k < caminho.length; k++) {
							int destinoYAtual = caminho[k][0];
							int destinoXAtual = caminho[k][1];
							for (int l = 0; l < tabuleiroGlobal.length; l++) {
								for (int m = 0; m < tabuleiroGlobal[0].length; m++) {
									if (tabuleiroGlobal[l][m] != null && !tabuleiroGlobal[l][m].getNome().equals("Re") && validarMovimentoPeca(l, m, destinoXAtual, destinoYAtual, reiY, reiX)) {
										return true;
									}
								}
							}
						}

						for (int k = 0; k < tabuleiroGlobal.length; k++) {
							for (int l = 0; l < tabuleiroGlobal[0].length; l++) {
								if (tabuleiroGlobal[k][l] != null && validarMovimentoPeca(k, l, i, j, reiY, reiX)) {
									return true;
								}

							}
						}
						
						return false;
					}
				}
			}
		}		


		return true;
	}
	
	private boolean validarMovimentoPeca(int origemY, int origemX, int destinoY, int destinoX, int reiY, int reiX) {
		if (tabuleiroGlobal[origemY][origemX] == null)
			return false;
		if (origemY >= tabuleiroGlobal.length || origemY < 0)
			return false;
		if (origemX >= tabuleiroGlobal[0].length || origemX < 0)
			return false;

		if (destinoY >= tabuleiroGlobal.length || destinoY < 0)
			return false;
		if (destinoX >= tabuleiroGlobal[0].length || destinoX < 0)
			return false;
		Piece peca = tabuleiroGlobal[origemY][origemX];
		if (!peca.getCor().equals(turnoAtual))
			return false;

		if (peca.getNome().equals("Bi")) {
			if (validarBispo(tabuleiroGlobal, origemY, origemX, destinoY, destinoX) == null) {
				return false;
			}
		} else if (peca.getNome().equals("Pe")) {
			if (!validarPeao(tabuleiroGlobal, origemY, origemX, destinoY, destinoX)){
				return false;
			}
		} else if (peca.getNome().equals("Ca")) {
			if (!validarCavalo(tabuleiroGlobal, origemY, origemX, destinoY, destinoX))
				return false;
		} else if (peca.getNome().equals("To")) {
			if (validarTorre(tabuleiroGlobal, origemY, origemX, destinoY, destinoX) == null) {
				return false;
			} 
			
			if(this.turnoAtual.equals("branco")) {
				if(origemX <= this.tabuleiroGlobal.length / 2) this.moveuTorreEsqBranco = true;
				else this.moveuTorreDirBranco = true;
			}
			else {
				if(origemX <= this.tabuleiroGlobal.length / 2) this.moveuTorreEsqPreto = true;
				else this.moveuTorreDirPreto = true;
			}
		} else if (peca.getNome().equals("Re")) {
			if (!validarRei(tabuleiroGlobal, origemY, origemX, destinoY, destinoX)) return false;
			reiY = destinoY;
			reiX = destinoX;
		} else if (peca.getNome().equals("Ra")) {
			if (validarRainha(tabuleiroGlobal, origemY, origemX, destinoY, destinoX) == null) {
				return false;
			}
		} else return false;

		Piece[][] tabuleiroTemporarioJogada = clonarTabuleiro();
		tabuleiroTemporarioJogada[destinoY][destinoX] = tabuleiroGlobal[origemY][origemX];
		tabuleiroTemporarioJogada[origemY][origemX] = null;
		
		if (!validarReiDuranteJogada(tabuleiroTemporarioJogada, reiY, reiX, this.turnoAtual))
			return false;

		return true;
	}

	private boolean validarReiDuranteJogada(Piece[][] tabuleiro, int reiY, int reiX, String cor) {
		for (int i = 0; i < tabuleiro.length; i++) {
			for (int j = 0; j < tabuleiro[0].length; j++) {
				if (tabuleiro[i][j] == null)
					continue;
				
				if (tabuleiro[i][j].getCor().equals(cor))
					continue;

				switch (tabuleiro[i][j].getNome()) {
				case "Ca":
					if (validarCavalo(tabuleiro, i, j, reiY, reiX)) {
						return false;
					}

					break;
				case "Bi":
					if (validarBispo(tabuleiro, i, j, reiY, reiX) != null) {
						return false;
					}

					break;
				case "To":
					if (validarTorre(tabuleiro, i, j, reiY, reiX) != null) {
						return false;
					}
					break;
				case "Pe": //???
					if (validarPeao(tabuleiro, i, j, reiY, reiX)) {
						return false;
					}
					break;
				case "Ra":
					if (validarRainha(tabuleiro, i, j, reiY, reiX) != null) {
						return false;
					}
					break;
				}
			}
		}

		return true;
	}

	private Piece[][] tabuleiroStringParaPiece(String[][] tabuleiro, int separacao) {
		Piece[][] tabuleiroPiece = new Piece[tabuleiro.length][tabuleiro[0].length];
		for (int i = 0; i < tabuleiro.length; i++) {
			for (int j = 0; j < tabuleiro[0].length; j++) {
				if (tabuleiro[i][j].equals("Re")) {
					String cor = "";
					if(i <= separacao) cor = "preto";
					else cor = "branco";
					tabuleiroPiece[i][j] = new Piece(tabuleiro[i][j].strip(), cor);
					if (tabuleiroPiece[i][j].getCor().equals("preto")) {
						this.reiPretoY = i;
						this.reiPretoX = j;
					} else {
						this.reiBrancoY = i;
						this.reiBrancoX = j;
					}

					continue;
				}
				if (!tabuleiro[i][j].strip().equals("")) {
					String cor = "";
					if(i <= separacao) cor = "preto";
					else cor = "branco";
					tabuleiroPiece[i][j] = new Piece(tabuleiro[i][j].strip(), cor);
					continue;
				}
			}
		}
		return tabuleiroPiece;
	}

	private Piece[][] clonarTabuleiro() {
		Piece[][] tabuleiroLocal = new Piece[tabuleiroGlobal.length][tabuleiroGlobal[0].length];

		for (int i = 0; i < tabuleiroGlobal.length; i++) {
			for (int j = 0; j < tabuleiroGlobal[0].length; j++) {
				tabuleiroLocal[i][j] = tabuleiroGlobal[i][j];
			}
		}

		return tabuleiroLocal;
	}

	private boolean validarRei(Piece[][] tabuleiro, int origemY, int origemX, int destinoY, int destinoX) {
		if(tabuleiro[origemY][origemX].getCor().equals("branco") && !this.moveuReiBranco) {
			if (origemX + 2 == destinoX && origemY == destinoY &&  !this.moveuTorreDirBranco) {
				int[] caminho = new int[2];
				int indice = 0;
				for(int i = origemX + 1; i < 8; i++) {
					if(tabuleiro[origemY][i] != null && !tabuleiro[origemY][i].getNome().equals("To")) {
						return false;
					}
					if(indice < 2) {
						caminho[indice] = i;
						indice++;
					}
				}
				for(int i = 0; i < caminho.length; i++) {
					if(!validarReiDuranteJogada(tabuleiro, origemY, caminho[i], "branco")) {
						return false;
					}
				}
				return true;
			}
			if (origemX - 2 == destinoX && origemY == destinoY && !this.moveuTorreEsqBranco) {
				int[] caminho = new int[2];
				int indice = 0;
				for(int i = origemX - 1; i >= 0; i--) {
					if(tabuleiro[origemY][i] != null && !tabuleiro[origemY][i].getNome().equals("To")) {
						return false;
					}
					if(indice < 2) {
						caminho[indice] = i;
						indice++;
					}
				}
				for(int i = 0; i < caminho.length; i++) {
					if(!validarReiDuranteJogada(tabuleiro, origemY, caminho[i], "branco")) {
						return false;
					}
				}
				return true;
			}
		}
		if(tabuleiro[origemY][origemX].getCor().equals("preto") && !this.moveuReiPreto) {
			if (origemX + 2 == destinoX && origemY == destinoY &&  !this.moveuTorreDirPreto) {
				int[] caminho = new int[2];
				int indice = 0;
				for(int i = origemX + 1; i < 8; i++) {
					if(tabuleiro[origemY][i] != null && !tabuleiro[origemY][i].getNome().equals("To")) {
						return false;
					}
					if(indice < 2) {
						caminho[indice] = i;
						indice++;
					}
				}
				for(int i = 0; i < caminho.length; i++) {
					if(!validarReiDuranteJogada(tabuleiro, origemY, caminho[i], "preto")) {
						return false;
					}
				}
				return true;
			}
			if (origemX - 2 == destinoX && origemY == destinoY && !this.moveuTorreEsqPreto) {
				int[] caminho = new int[2];
				int indice = 0;
				for(int i = origemX - 1; i >= 0; i--) {
					if(tabuleiro[origemY][i] != null && !tabuleiro[origemY][i].getNome().equals("To")) {
						return false;
					}
					if(indice < 2) {
						caminho[indice] = i;
						indice++;
					}
				}
				for(int i = 0; i < caminho.length; i++) {
					if(!validarReiDuranteJogada(tabuleiro, origemY, caminho[i], "branco")) {
						return false;
					}
				}
				return true;
			}
		}
		
		if (origemY + 1 != destinoY && origemY - 1 != destinoY && origemY != destinoY)
			return false;

		if (origemX + 1 != destinoX && origemX - 1 != destinoX && origemX != destinoX)
			return false;

		if (tabuleiro[destinoY][destinoX] != null
				&& tabuleiro[destinoY][destinoX].getCor().equals(tabuleiro[origemY][origemX].getCor()))
			return false;

		return true;
	}

	public boolean moverPiece(int origemY, int origemX, int destinoY, int destinoX) {
		if(tabuleiroGlobal[origemY][origemX] == null) return false;
		int reiY = -1;
		int reiX = -1;
		if(this.turnoAtual.equals("branco")) {
			reiY = reiBrancoY;
			reiX = reiBrancoX;
		} else {
			reiY = reiPretoY;
			reiX = reiPretoX;
		}
		if(reiY == -1 || reiX == -1) return false;
		
		if (!validarMovimentoPeca(origemY, origemX, destinoY, destinoX, reiY, reiX))
			return false;

		if (tabuleiroGlobal[origemY][origemX].getNome().equals("Re")) {
			if (turnoAtual.equals("preto")) {
				reiPretoX = destinoX;
				reiPretoY = destinoY;
			} else {
				reiBrancoX = destinoX;
				reiBrancoY = destinoY;
			}

		}
		
		if(tabuleiroGlobal[origemY][origemX].getNome().equals("Pe")){
			if(origemX != destinoX) {
				if(origemX + 1 < 8 && tabuleiroGlobal[origemY][origemX + 1] != null) {
					if(tabuleiroGlobal[origemY][origemX + 1].getCor().equals(enPassant) && tabuleiroGlobal[origemY][origemX + 1].getNome().equals("Pe")) {
						System.out.println("EN passant direita teste");
						tabuleiroGlobal[origemY][origemX + 1] = null;
					}
				}

				if(origemX - 1 >= 0 && tabuleiroGlobal[origemY][origemX - 1] != null) {
					if(tabuleiroGlobal[origemY][origemX - 1].getCor().equals(enPassant) && tabuleiroGlobal[origemY][origemX - 1].getNome().equals("Pe")) {
						System.out.println("EN passant esquerda teste");
						tabuleiroGlobal[origemY][origemX - 1] = null;
					}
				}
			}
		}
		
		if(tabuleiroGlobal[origemY][origemX].getNome().equals("Re")) {
			if(origemX + 2 == destinoX) {
				int torreX = 0;
				for(int i = origemX + 1; i < 8; i++) {
					if(tabuleiroGlobal[origemY][i] != null && !tabuleiroGlobal[origemY][i].getNome().equals("To")) {
						System.out.println(tabuleiroGlobal[origemY][i].getNome());
						return false;
					}
					torreX = i;
				}
				
				tabuleiroGlobal[origemY][origemX + 1] = tabuleiroGlobal[origemY][torreX];
				tabuleiroGlobal[origemY][torreX] = null;
				
				if(this.turnoAtual.equals("branco")) {
					this.moveuTorreDirBranco = true;
				} else {
					this.moveuTorreDirPreto = true;
				}
			}
			
			if(origemX - 2 == destinoX) {
				int torreX = 0;
				for(int i = origemX - 1; i >= 0; i--) {
					if(tabuleiroGlobal[origemY][i] != null && !tabuleiroGlobal[origemY][i].getNome().equals("To")) return false;
					torreX = i;
				}
				
				tabuleiroGlobal[origemY][origemX - 1] = tabuleiroGlobal[origemY][torreX];
				tabuleiroGlobal[origemY][torreX] = null;
				
				if(this.turnoAtual.equals("branco")) {
					this.moveuTorreEsqBranco = true;
				} else {
					this.moveuTorreEsqPreto = true;
				}
			}
			
			if(this.turnoAtual.equals("branco")) {
				this.moveuReiBranco = true;
			} else {
				this.moveuReiPreto = true;
			}
		}
		
		tabuleiroGlobal[destinoY][destinoX] = tabuleiroGlobal[origemY][origemX];
		tabuleiroGlobal[origemY][origemX] = null;


		if (turnoAtual.equals("preto"))
			turnoAtual = "branco";
		else
			turnoAtual = "preto";
		
		return true;
	}

	private boolean validarCavalo(Piece[][] tabuleiro, int origemY, int origemX, int destinoY, int destinoX) {
		boolean valid = false;

		for (int i = 1; i < 3; i++) {
			boolean resultX = (origemX + i == destinoX) || (origemX - i == destinoX);
			boolean resultY = (origemY + (3 - i) == destinoY) || (origemY - (3 - i) == destinoY);
			if (resultX && resultY) {
				valid = true;
			}
		}

		if (!valid)
			return false;

		if (tabuleiro[destinoY][destinoX] != null) {
			if (tabuleiro[destinoY][destinoX].getCor().equals(tabuleiro[origemY][origemX].getCor())) {
				return false;
			}

			return true;
		}

		return true;
	}

	private boolean validarPeao(Piece[][] tabuleiro, int origemY, int origemX, int destinoY, int destinoX) {
		int dirY = 0;
		if (tabuleiro[origemY][origemX].getCor().equals("branco")) {
			if (origemY == 6 && destinoY == (origemY - 2))
				dirY = -2;
			else
				dirY = -1;
		} else {
			if (origemY == 1 && destinoY == (origemY + 2))
				dirY = +2;
			else
				dirY = +1;
		}
		
		
		if (origemY + dirY != destinoY) {
			return false;
		}

		if (tabuleiro[destinoY][destinoX] == null) {
			
			if(dirY == -2 || dirY == +2) {
				if(this.turnoAtual.equals(tabuleiro[origemY][origemX].getCor())) {
					if(this.turnoAtual.equals("branco")) {
						this.enPassant = "branco";
					} else this.enPassant = "preto";
				} 
			}
			
			if (origemX == destinoX)
				return true;

			int diffX = 0;
			if (origemX < destinoX) diffX = 1;
			if (origemX > destinoX) diffX = -1;

			if (tabuleiro[origemY][origemX + diffX].getCor().equals(enPassant) && tabuleiro[origemY][origemX + diffX].getNome().equals("Pe") 
					&& !tabuleiro[origemY][origemX + diffX].getCor().equals(tabuleiro[origemY][origemX].getCor())) {
				
				return true;
			}
				
			return false;
		}

		if (tabuleiro[destinoY][destinoX].getCor().equals(tabuleiro[origemY][origemX].getCor())) {
			return false;
		}

		if (origemX + 1 != destinoX && origemX - 1 != destinoX) {
			return false;
		}

		return true;
	}

	private int[][] validarTorre(Piece[][] tabuleiro, int origemY, int origemX, int destinoY, int destinoX) {
		if (!(origemX == destinoX ^ origemY == destinoY)) {
			return null;
		}

		int[][] casasAndadas = new int[8][2];
		int posicaoCasasAndadas = 0;

		int diffY = 0;
		int diffX = 0;
		if (destinoY > origemY)
			diffY = 1;
		else if (destinoY < origemY)
			diffY = -1;
		if (destinoX > origemX)
			diffX = 1;
		else if (destinoX < origemX)
			diffX = -1;
		int pecaX = origemX + diffX;
		int pecaY = origemY + diffY;

		casasAndadas[posicaoCasasAndadas][0] = pecaY;
		casasAndadas[posicaoCasasAndadas][1] = pecaX;

		while (pecaX != destinoX || pecaY != destinoY) {
			if (tabuleiro[pecaY][pecaX] != null) {
				return null;
			}
			pecaX += diffX;
			pecaY += diffY;

			posicaoCasasAndadas++;
			casasAndadas[posicaoCasasAndadas][0] = pecaY;
			casasAndadas[posicaoCasasAndadas][1] = pecaX;
		}

		if (tabuleiro[destinoY][destinoX] != null) {
			if (tabuleiro[destinoY][destinoX].getCor().equals(tabuleiro[origemY][origemX].getCor()))
				return null;
			return casasAndadas;
		}

		return casasAndadas;

	}

	private int[][] validarRainha(Piece[][] tabuleiro, int origemY, int origemX, int destinoY, int destinoX) {
		int[][] casasAndadas = new int[8][2];
		int posicaoCasasAndadas = 0;

		if ((origemX == destinoX) ^ (origemY == destinoY)) {
			if ((origemX + origemY) % 2 != (destinoX + destinoY) % 2) {
				return null;
			}

			int diffY = 0;
			int diffX = 0;
			if (origemY < destinoY)
				diffY = 1;
			else
				diffY = -1;
			if (origemX < destinoX)
				diffX = 1;
			else
				diffX = -1;
			int pecaY = origemY + diffY;
			int pecaX = origemX + diffX;
			casasAndadas[posicaoCasasAndadas][0] = pecaY;
			casasAndadas[posicaoCasasAndadas][1] = pecaX;
			while (pecaX != destinoX || pecaY != destinoY) {
				if ((pecaX == destinoX) ^ (pecaY == destinoY)) {
					return null;
				}
				if (tabuleiro[pecaY][pecaX] != null) {
					return null;
				}
				pecaY += diffY;
				pecaX += diffX;
				posicaoCasasAndadas++;
				casasAndadas[posicaoCasasAndadas][0] = pecaY;
				casasAndadas[posicaoCasasAndadas][1] = pecaX;
			}

			if (tabuleiro[destinoY][destinoX] == null) {
				return casasAndadas;
			}

			if (tabuleiro[destinoY][destinoX].getCor().equals(tabuleiro[origemY][origemX].getCor())) {
				return null;
			}

			return casasAndadas;
		}
		
		if(origemX == destinoX || origemY == destinoY) {
			int diffY = 0;
			int diffX = 0;
			if (destinoY > origemY) diffY = 1;
			else if (destinoY < origemY) diffY = -1;
			if (destinoX > origemX) diffX = 1;
			else if (destinoX < origemX) diffX = -1;

			int pecaX = origemX + diffX;
			int pecaY = origemY + diffY;
			
			casasAndadas[posicaoCasasAndadas][0] = pecaY;
			casasAndadas[posicaoCasasAndadas][1] = pecaX;
			while (pecaX != destinoX || pecaY != destinoY) {
				if (tabuleiro[pecaY][pecaX] != null) {
					return null;
				}
				pecaX += diffX;
				pecaY += diffY;
				posicaoCasasAndadas++;
				casasAndadas[posicaoCasasAndadas][0] = pecaY;
				casasAndadas[posicaoCasasAndadas][1] = pecaX;
			}

			if (tabuleiro[destinoY][destinoX] != null) {
				if (tabuleiro[destinoY][destinoX].getCor().equals(tabuleiro[origemY][origemX].getCor()))
					return null;
				return casasAndadas;
			}

			return casasAndadas;
		}
		
		return null;
	}

	private int[][] validarBispo(Piece[][] tabuleiro, int origemY, int origemX, int destinoY, int destinoX) {
		int[][] casasAndadas = new int[8][2];
		int posicaoCasasAndadas = 0;
		if ((origemX + origemY) % 2 != (destinoX + destinoY) % 2) {
			return null;
		}

		if (origemY == destinoY || origemX == destinoX) {
			return null;
		}

		int diffY;
		int diffX;
		if (origemY < destinoY)
			diffY = 1;
		else
			diffY = -1;
		if (origemX < destinoX)
			diffX = 1;
		else
			diffX = -1;
		int pecaY = origemY + diffY;
		int pecaX = origemX + diffX;
		casasAndadas[posicaoCasasAndadas][0] = pecaY;
		casasAndadas[posicaoCasasAndadas][1] = pecaX;
		while (pecaX != destinoX || pecaY != destinoY) {
			if ((pecaX == destinoX) ^ (pecaY == destinoY)) {
				return null;
			}
			if (tabuleiro[pecaY][pecaX] != null) {
				return null;
			}
			pecaY += diffY;
			pecaX += diffX;
			posicaoCasasAndadas++;
			casasAndadas[posicaoCasasAndadas][0] = pecaY;
			casasAndadas[posicaoCasasAndadas][1] = pecaX;
		}

		if (tabuleiro[destinoY][destinoX] == null) {
			return casasAndadas;
		}

		if (tabuleiro[destinoY][destinoX].getCor().equals(tabuleiro[origemY][origemX].getCor())) {
			System.out.println("AAAAA");
			return null;
		}

		return casasAndadas;
	}

	public String getTurnoAtual() {
		return this.turnoAtual;
	}
	
	public String getPecaCor(int Y, int X ) {
		if(this.tabuleiroGlobal[Y][X] == null) return "";
		return tabuleiroGlobal[Y][X].getCor();
	}
	
	public String getPecaNome(int Y, int X) {
		if(this.tabuleiroGlobal[Y][X] == null) return "";
		String nome = tabuleiroGlobal[Y][X].getNome();
		
		if(nome.equals("To")) return "Torre";
		if(nome.equals("Ra")) return "Rainha";
		if(nome.equals("Pe")) return "Peão";
		if(nome.equals("Re")) return "Rei";
		if(nome.equals("Bi")) return "Bispo";
		if(nome.equals("Ca")) return "Cavalo";
		
		return "";
	}
	
	public String getTabuleiroFormatado(Piece[][] tabuleiro) {
		String tabuleiroFormatado = "";
		tabuleiroFormatado += "    ";
		for (int i = 0; i < tabuleiro[0].length; i++) {
			tabuleiroFormatado += ("  " + notacaoAlgebricaGlobal.charAt(i) + "   ");
		}

		tabuleiroFormatado +=  "\n    ";
		for (int i = 0; i < tabuleiro[0].length; i++) {
			tabuleiroFormatado += "______";
		}
		tabuleiroFormatado += "\n";

		for (int i = 0; i < tabuleiro.length; i++) {
			tabuleiroFormatado += (8 - i + " | ");
			for (int j = 0; j < tabuleiro[0].length; j++) {
				tabuleiroFormatado += "[ ";
				if (tabuleiro[i][j] != null)
					tabuleiroFormatado += tabuleiro[i][j].getNomeFormatado();
				else
					tabuleiroFormatado += "  ";
				tabuleiroFormatado += " ]";
			}
			tabuleiroFormatado += "\n";
		}
		tabuleiroFormatado += "\n";
		return tabuleiroFormatado;
	}
}
