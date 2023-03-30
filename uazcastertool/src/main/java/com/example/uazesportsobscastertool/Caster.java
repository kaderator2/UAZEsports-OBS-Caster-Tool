package com.example.uazesportsobscastertool;

import javafx.beans.property.SimpleStringProperty;

public class Caster {
    private SimpleStringProperty lastNameTB;
    private SimpleStringProperty firstNameTB;
    private SimpleStringProperty discordTB;

    public Caster(String firstNameTB, String lastNameTB, String discordTB) {
        this.setLastNameTB(lastNameTB);
        this.setFirstNameTB(firstNameTB);
        this.setDiscordTB(discordTB);
    }

    public String getLastNameTB() {
        return lastNameTB.get();
    }

    public void setLastNameTB(String lastNameTB) {
        this.lastNameTB = new SimpleStringProperty(lastNameTB);
    }

    public String getFirstNameTB() {
        return firstNameTB.get();
    }

    public void setFirstNameTB(String firstNameTB) {
        this.firstNameTB = new SimpleStringProperty(firstNameTB);
    }

    public String getDiscordTB() {
        return discordTB.get();
    }

    public void setDiscordTB(String discordTB) {
        this.discordTB = new SimpleStringProperty(discordTB);
    }

    public String toString() {
        return firstNameTB.get();
    }

    public String toDataString() {
        return "'" + firstNameTB.get() + "','" + lastNameTB.get() + "','" + discordTB.get() + "'";
    }
}
