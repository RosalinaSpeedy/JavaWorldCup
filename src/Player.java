public class Player extends Person {
	private String position;
	private double fitness;
	private double passingAccuracy;
	private double shotAccuracy;
	private double shotFrequency;
	private double defensiveness;
	private double aggression;
	private double positioning;
	private double dribbling;
	private double chanceCreation;
	private double offsideAdherence;

	// constructor
	public Player(String firstName, String surname, String team, String position, double fitness,
			double passingAccuracy, double shotAccuracy, double shotFrequency, double defensiveness, double aggression,
			double positioning, double dribbling, double chanceCreation, double offsideAdherence) {
		super(firstName, surname, team);
		this.position = position;
		this.fitness = fitness;
		this.passingAccuracy = passingAccuracy;
		this.shotAccuracy = shotAccuracy;
		this.shotFrequency = shotFrequency;
		this.defensiveness = defensiveness;
		this.aggression = aggression;
		this.positioning = positioning;
		this.dribbling = dribbling;
		this.chanceCreation = chanceCreation;
		this.offsideAdherence = offsideAdherence;
	}

	// getters and setters:

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getPassingAccuracy() {
		return passingAccuracy;
	}

	public void setPassingAccuracy(double passingAccuracy) {
		this.passingAccuracy = passingAccuracy;
	}

	public double getShotAccuracy() {
		return shotAccuracy;
	}

	public void setShotAccuracy(double shotAccuracy) {
		this.shotAccuracy = shotAccuracy;
	}

	public double getDefensiveness() {
		return defensiveness;
	}

	public void setDefensiveness(double defensiveness) {
		this.defensiveness = defensiveness;
	}

	public double getShotFrequency() {
		return shotFrequency;
	}

	public void setShotFrequency(double shotFrequency) {
		this.shotFrequency = shotFrequency;
	}

	public double getAggression() {
		return aggression;
	}

	public void setAggression(double aggression) {
		this.aggression = aggression;
	}

	public double getPositioning() {
		return positioning;
	}

	public void setPositioning(double positioning) {
		this.positioning = positioning;
	}

	public double getDribbling() {
		return dribbling;
	}

	public void setDribbling(double dribbling) {
		this.dribbling = dribbling;
	}

	public double getOffsideAdherence() {
		return offsideAdherence;
	}

	public void setOffsideAdherence(double offsideAdherence) {
		this.offsideAdherence = offsideAdherence;
	}

	public double getChanceCreation() {
		return chanceCreation;
	}

	public void setChanceCreation(double chanceCreation) {
		this.chanceCreation = chanceCreation;
	}

	// return average method
	public double getAverage() {
		return (this.fitness + this.passingAccuracy + this.shotAccuracy + this.shotFrequency + this.defensiveness
				+ this.aggression + this.positioning + this.dribbling + this.chanceCreation + this.offsideAdherence)
				/ 10;
	}
}
