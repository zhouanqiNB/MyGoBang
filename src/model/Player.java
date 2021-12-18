package model;

/**
 * 玩家类
 */
public class Player {
    protected String name;
    protected String address = "本地玩家";
    protected int grade = 100;
    protected String color = Spot.notChess;
    protected static int playerIndex = 1;
    // 储存对战玩家，我方，对方
    public static Player myPlayer, otherPlayer;

    public Player() {
        this.name = "玩家" + playerIndex;
        playerIndex++;
    }

    /**
     * 初始化两玩家
     */
    public static void init() {
        myPlayer = new Player();
        otherPlayer = new Player();
    }

    /**
     * 开始游戏
     */
    public void start(String color) {
        this.color = color;
        System.out.println(name + " 下的是 " + getColorString());
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getColorString() {
        return Spot.getColorString(color);
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int addGrade(int add) {
        grade += add;
        return grade;
    }
}
