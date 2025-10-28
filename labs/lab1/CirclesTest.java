package labs.lab1;
import java.util.*;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends RuntimeException {
    ObjectCanNotBeMovedException(String message){
        super(message);
    }

}
class MovableObjectNotFittableException extends RuntimeException {
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();
    int getCurrentYPosition();
}

class MovablePoint implements Movable {
    private int x;
    private int y;
    private final int xSpeed;
    private final int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() {
        int newY = this.y + ySpeed;
        if (newY > MovablesCollection.y_MAX) {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", this.x, newY));
        }
        this.y = newY;
    }

    @Override
    public void moveLeft() {
        int newX = this.x - xSpeed;
        if (newX < MovablesCollection.x_MIN) {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", newX, this.y));
        }
        this.x = newX;
    }

    @Override
    public void moveRight() {
        int newX = this.x + xSpeed;
        if (newX > MovablesCollection.x_MAX) {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", newX, this.y));
        }
        this.x = newX;
    }

    @Override
    public void moveDown() {
        int newY = this.y - ySpeed;
        if (newY < MovablesCollection.y_MIN) {
            throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds", this.x, newY));
        }
        this.y = newY;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%s,%s)", x,y);
    }
}

class MovableCircle implements Movable {
    private final int radius;
    private final MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException{
        center.moveUp();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        center.moveLeft();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException{
        center.moveRight();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException{
        center.moveDown();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public String toString() {
        return String.format(
                "Movable circle with center coordinates (%s,%s) and radius %s",
                center.getCurrentXPosition(),center.getCurrentYPosition(), radius);
    }

    public int getRadius() {
        return radius;
    }

    public MovablePoint getCenter() {
        return center;
    }
}

class MovablesCollection {
    Movable [] movable;
    static int x_MAX;
    static final int x_MIN = 0;
    static int y_MAX;
    static final int y_MIN = 0;

    MovablesCollection(int x, int y){
        movable = new Movable[]{};
        x_MAX = x;
        y_MAX = y;
    }

    private boolean checkCircleFit(MovableCircle circle){
        int currX = circle.getCurrentXPosition();
        int currY = circle.getCurrentYPosition();
        int radius = circle.getRadius();
        return !(
                currX + radius > x_MAX ||
                currX - radius < x_MIN ||
                currY + radius > y_MAX ||
                currY - radius < y_MIN
        );
    }

    private boolean checkPoint(MovablePoint point) {
        int currX = point.getCurrentXPosition();
        int currY = point.getCurrentYPosition();
        return !(
                currX < x_MIN || currX > x_MAX ||
                        currY < y_MIN || currY > y_MAX
        );
    }


    void addMovableObject(Movable m) throws MovableObjectNotFittableException{
        boolean canFit = false;
        String message = "";
        if (m instanceof MovablePoint){
            canFit = checkPoint((MovablePoint) m);
            if (!canFit) {
                message = String.format(
                        "Movable point with coordinates (%s,%s) can not be fitted into the collection",
                        m.getCurrentXPosition(), m.getCurrentYPosition());
            }
        } else if (m instanceof MovableCircle) {
            MovableCircle circle = (MovableCircle) m;
            canFit = checkCircleFit(circle);
            if (!canFit) {
                message = String.format(
                        "Movable circle with center (%s,%s) and radius %s can not be fitted into the collection",
                        circle.getCurrentXPosition(), circle.getCurrentYPosition(), circle.getRadius());
            }
        }
        if (!canFit) throw new MovableObjectNotFittableException(message);
        addMovableHelper(m);
    }

    private void addMovableHelper(Movable m){
        Movable []newMovables = new Movable[movable.length + 1];
        for (int i = 0; i < movable.length; i++) {
            newMovables[i] = movable[i];
        }
        newMovables[movable.length] = m;
        movable = newMovables;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Collection of movable objects with size " + movable.length + ":\n");
        for (Movable m : movable) {
            sb.append(m.toString()).append('\n');
        }
        return sb.toString();
    }

    public static void setxMax(int x_MAX) {
        MovablesCollection.x_MAX = x_MAX;
    }

    public static void setyMax(int y_MAX) {
        MovablesCollection.y_MAX = y_MAX;
    }

    void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction)
            throws ObjectCanNotBeMovedException {
        Arrays.stream(movable)
                .filter(m -> {
                    if (type == TYPE.POINT) {
                        return m instanceof MovablePoint;
                    } else if (type == TYPE.CIRCLE){
                        return m instanceof MovableCircle;
                    }
                    return false;
                })
                .forEach(m -> {
                    try{
                        switch (direction) {
                            case UP:
                                m.moveUp(); break;
                            case DOWN:
                                m.moveDown(); break;
                            case LEFT:
                                m.moveLeft(); break;
                            case RIGHT:
                                m.moveRight(); break;
                        }
                    } catch (ObjectCanNotBeMovedException e){
                        System.out.println(e.getMessage());
                    }
                });
    }

}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());


    }


}
