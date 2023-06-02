package com.example.learn_english;

public class Prepositions {
    private int PrepositionsofTime;
    private int SimplePreposition;
    private int DoublePreposition;
    private int CompoundPreposition;
    private int ParticiplePreposition;
    private int DisguisedPreposition;
    private int DetachedPreposition;
    private int PrepositionsofPlaceandDirection;
    private int PrepositionsofAgentsorThings;
    private int PhrasalPrepositions;

    public Prepositions() {
        // Default constructor required for Firebase serialization
    }

    public Prepositions(int prepositionsofTime, int simplePreposition, int doublePreposition, int compoundPreposition, int participlePreposition, int disguisedPreposition, int detachedPreposition, int prepositionsofPlaceandDirection, int prepositionsofAgentsorThings, int phrasalPrepositions) {
        this.PrepositionsofTime = prepositionsofTime;
        this.SimplePreposition = simplePreposition;
        this.DoublePreposition = doublePreposition;
        this.CompoundPreposition = compoundPreposition;
        this.ParticiplePreposition = participlePreposition;
        this.DisguisedPreposition = disguisedPreposition;
        this.DetachedPreposition = detachedPreposition;
        this.PrepositionsofPlaceandDirection = prepositionsofPlaceandDirection;
        this.PrepositionsofAgentsorThings = prepositionsofAgentsorThings;
        this.PhrasalPrepositions = phrasalPrepositions;
    }

    public int getPrepositionsofTime() {
        return PrepositionsofTime;
    }

    public void setPrepositionsofTime(int prepositionsofTime) {
        PrepositionsofTime = prepositionsofTime;
    }

    public int getSimplePreposition() {
        return SimplePreposition;
    }

    public void setSimplePreposition(int simplePreposition) {
        SimplePreposition = simplePreposition;
    }

    public int getDoublePreposition() {
        return DoublePreposition;
    }

    public void setDoublePreposition(int doublePreposition) {
        DoublePreposition = doublePreposition;
    }

    public int getCompoundPreposition() {
        return CompoundPreposition;
    }

    public void setCompoundPreposition(int compoundPreposition) {
        CompoundPreposition = compoundPreposition;
    }

    public int getParticiplePreposition() {
        return ParticiplePreposition;
    }

    public void setParticiplePreposition(int participlePreposition) {
        ParticiplePreposition = participlePreposition;
    }

    public int getDisguisedPreposition() {
        return DisguisedPreposition;
    }

    public void setDisguisedPreposition(int disguisedPreposition) {
        DisguisedPreposition = disguisedPreposition;
    }

    public int getDetachedPreposition() {
        return DetachedPreposition;
    }

    public void setDetachedPreposition(int detachedPreposition) {
        DetachedPreposition = detachedPreposition;
    }

    public int getPrepositionsofPlaceandDirection() {
        return PrepositionsofPlaceandDirection;
    }

    public void setPrepositionsofPlaceandDirection(int prepositionsofPlaceandDirection) {
        PrepositionsofPlaceandDirection = prepositionsofPlaceandDirection;
    }

    public int getPrepositionsofAgentsorThings() {
        return PrepositionsofAgentsorThings;
    }

    public void setPrepositionsofAgentsorThings(int prepositionsofAgentsorThings) {
        PrepositionsofAgentsorThings = prepositionsofAgentsorThings;
    }

    public int getPhrasalPrepositions() {
        return PhrasalPrepositions;
    }

    public void setPhrasalPrepositions(int phrasalPrepositions) {
        PhrasalPrepositions = phrasalPrepositions;
    }
}
