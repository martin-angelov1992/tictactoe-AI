package coarseProject;

public class Player {
	private Symbol symbol;
	private String name;
	private final byte ID;
	private boolean bot;

	public enum Symbol {
		X("X"), O("O");

		private String file;

		private Symbol(String file) {
			this.file = file;
		}

		public String getFIle() {
			return file;
		}
	}

	private Player(String name, byte ID, boolean bot, Symbol symbol) {
		this.name = name;
		this.ID = ID;
		this.bot = bot;
		this.symbol = symbol;
	}

	public byte getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public boolean isBot() {
		return bot;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	// Factory Method
	public static Player newBot(String name, byte ID) {
		return new Player(name, ID, true, Symbol.O);
	}

	// Factory Method
	public static Player newPlayer(String name, byte ID) {
		return new Player(name, ID, false, Symbol.X);
	}
}