package aud.aud1;

abstract class Alien {
    private int health; // 0=dead, 100=full strength
    private String name;

    public Alien(int health, String name) {
        this.health = health;
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    abstract int getDamage();
}

class SnakeAlien extends Alien {
    public SnakeAlien(int health, String name) {
        super(health, name);
    }

    @Override
    int getDamage() {
        return 10;
    }
}
class OgreAlien extends Alien {
    public OgreAlien(int health, String name) {
        super(health, name);
    }
    @Override
    int getDamage() {
        return 6;
    }

}
class MarshmallowManAlien extends Alien {
    public MarshmallowManAlien(int health, String name) {
        super(health, name);
    }
    @Override
    int getDamage() {
        return 1;
    }
}




class AlienPack {
    private Alien[] aliens;

    public AlienPack(int numAliens) {
        aliens = new Alien[numAliens];
    }

    public void addAlien(Alien newAlien, int index) {
        aliens[index] = newAlien;
    }

    public Alien[] getAliens() {
        return aliens;
    }

    public int calculateDamage() {
        int damage = 0;
        for (Alien alien : aliens) {
            damage += alien.getDamage();
        }
        return damage;
    }
}

public class AlienExample {
    public static void main(String[] args) {
        AlienPack pack = new AlienPack(3);
        pack.addAlien(new SnakeAlien(100, "Snake"), 0);
        pack.addAlien(new OgreAlien(80, "Ogre"), 1);
        pack.addAlien(new MarshmallowManAlien(50, "Marshmallow"), 2);

        System.out.println("Total damage from pack: " + pack.calculateDamage());

    }
}
