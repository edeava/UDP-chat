package udp_chat;

import java.util.Random;

public class NamePool {

	private static String[] names = {"Stefan", "Bojan", "Nemanja", "Marko", "Goran", "Lazar", "Viktor"};
	
	public static String getName() {
		Random rand = new Random();
		int index = rand.nextInt(7);
		return names[index];
	}
}
