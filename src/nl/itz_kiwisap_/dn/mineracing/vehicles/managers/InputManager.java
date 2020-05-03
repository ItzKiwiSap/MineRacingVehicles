package nl.itz_kiwisap_.dn.mineracing.vehicles.managers;

public class InputManager {
	
	private boolean w;
	private boolean a;
	private boolean s;
	private boolean d;
	private boolean space;
	
	public InputManager(boolean w, boolean a, boolean s, boolean d, boolean space) {
		this.w = w;
		this.a = a;
		this.s = s;
		this.d = d;
		this.space = space;
	}
	
	public boolean isW() { return this.w; }
	public boolean isA() { return this.a; }
	public boolean isS() { return this.s; }
	public boolean isD() { return this.d; }
	public boolean isSpace() { return this.space; }
}
