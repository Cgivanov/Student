public class Assignment {
    private int number;
    private int maxPoints;
    private int earnedPoints;

    public Assignment(int number, int maxPoints, int earnedPoints) {
        this.number = number;
        this.maxPoints = maxPoints;
        this.earnedPoints = earnedPoints;
    }

    public int getNumber() { // number of ass
        return number;
    }

    public int getMaxPoints() { // number of max points can earn
        return maxPoints;

    }

    public int getEarnedPoints() { // what the student receive from it
        return earnedPoints;

    }
}
