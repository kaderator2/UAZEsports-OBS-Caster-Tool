package application;

public class Caster {
	private String lName;
	private String fName;
	private String discordName;

	public Caster(String lName, String fName, String discordName) {
		this.setlName(lName);
		this.setfName(fName);
		this.setDiscordName(discordName);
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getDiscordName() {
		return discordName;
	}

	public void setDiscordName(String discordName) {
		this.discordName = discordName;
	}

	public String toString() {
		return fName + ", " + lName + ", " + discordName;
	}
}
