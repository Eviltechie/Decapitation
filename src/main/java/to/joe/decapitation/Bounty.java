package to.joe.decapitation;

import java.sql.Timestamp;

public class Bounty {

    private int ID;
    private String issuer;
    private String hunted;
    public void setID(int iD) {
        ID = iD;
    }

    private double reward;
    private Timestamp created;
    private String hunter;
    private Timestamp turnedIn;
    private Timestamp redeemed;

    public Bounty(int ID, String issuer, String hunted, double reward, Timestamp created, String hunter, Timestamp turnedIn, Timestamp redeemed) {
        this.ID = ID;
        this.issuer = issuer;
        this.hunted = hunted;
        this.reward = reward;
        this.created = created;
        this.hunter = hunter;
        this.turnedIn = turnedIn;
        this.redeemed = redeemed;
    }

    public Bounty(String issuer, String hunted, double reward) {
        this.issuer = issuer;
        this.hunted = hunted;
        this.reward = reward;
    }

    public double getReward() {
        return reward;
    }

    public String getHunted() {
        return hunted;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setHunted(String hunted) {
        this.hunted = hunted;
    }

    public void setHunter(String hunter) {
        this.hunter = hunter;
    }

    public void setTurnedIn(Timestamp turnedIn) {
        this.turnedIn = turnedIn;
    }

    public void setRedeemed(Timestamp redeemed) {
        this.redeemed = redeemed;
    }

    public int getID() {
        return ID;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getHunter() {
        return hunter;
    }

    public Timestamp getTurnedIn() {
        return turnedIn;
    }

    public Timestamp getRedeemed() {
        return redeemed;
    }
    
    
}
