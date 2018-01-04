package IRCClasses;

import BiNetcatClasses.ReaderPrinter;

public class Parser {
	
	public static void check(ReaderPrinter printer, User user) {
		User.addUser(user);
		printer.tellAfterParse(user.getNick(), user.getUser());
		user.setEnable(true);
	}

}
