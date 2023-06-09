// This class was created by Elix on 30.08.22


package codes.Elix.Woolbattle.game.HelpClasses;


public class Perk {

    private String firstPerk;
    private String secondPerk;
    private String passivePerk;
    private boolean particles;
    private Integer firstPerkSlot;
    private Integer secondPerkSlot;
    public Perk(String firstPerk, String secondPerk, String passivePerk, boolean particles, Integer firstPerkSlot, Integer secondPerkSlot) {
        this.firstPerk = firstPerk;
        this.secondPerk = secondPerk;
        this.passivePerk = passivePerk;
        this.particles = particles;
        this.firstPerkSlot = firstPerkSlot;
        this.secondPerkSlot = secondPerkSlot;
    }

    public String getfirstPerk() {
        return this.firstPerk;
    }
    public Integer getfirstPerkSlot() {
        return this.firstPerkSlot;
    }

    public String getsecondPerk() {
        return this.secondPerk;
    }
    public Integer getsecondPerkSlot() {
        return this.secondPerkSlot;
    }

    public String getpassivePerk() {
        return this.passivePerk;
    }
    public int getpassivePerkSlot() {
        return 8;
    }
    public boolean hasParticles() { return this.particles; }

    public void setFirstPerk(String firstPerk) { this.firstPerk = firstPerk; }
    public void setSecondPerk(String secondPerk) { this.secondPerk = secondPerk; }
    public void setPassivePerk(String passivePerk) { this.passivePerk = passivePerk; }
    public void setParticles(boolean particles) { this.particles = particles; }
    public void setFirstPerkSlot(Integer firstPerkSlot) { this.firstPerkSlot = firstPerkSlot; }
    public void setSecondPerkSlot(Integer secondPerkSlot) { this.secondPerkSlot = secondPerkSlot; }


}

