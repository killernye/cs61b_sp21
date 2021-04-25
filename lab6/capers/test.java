package capers;

import org.junit.Test;

import java.io.File;

public class test {

    @Test
    public void createDogs() {
        Dog a = new Dog("dogA", "hasiki", 6);
        Dog b = new Dog("dogB", "hasiki", 3);
        Dog c = new Dog("dogC", "hasiki", 4);
        b.setBrother(a);
        a.setBrother(c);

        a.saveDog();
        b.saveDog();
    }

    public static void main(String[] args) {
        File inFile = Utils.join(Dog.DOG_FOLDER, "dogA");
        Dog d = Utils.readObject(inFile, Dog.class);

        System.out.println(d.toString());
        System.out.println(d.getBrother());
    }
}
