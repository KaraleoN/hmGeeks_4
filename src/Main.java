import java.util.Random;

public class Main {
    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {290, 270, 250, 280};
    public static int[] heroesDamage = {25, 15, 10, 0};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic"};
    public static int roundNumber = 0;
    public static boolean medicAlive = true;

    public static void main(String[] args) {
        showStatistics();
        while (!isGameOver()) {
            round();
        }
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int heroHealth : heroesHealth) {
            if (heroHealth > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead || !medicAlive) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;
    }

    public static void round() {
        roundNumber++;
        chooseBossdefence();
        bossAttacks();
        heroesAttack();
        medicHeal();
        showStatistics();
    }

    public static void chooseBossdefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length);
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void heroesAttack() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0 && !heroesAttackType[i].equals("Medic")) {
                int damage = heroesDamage[i];
                if (bossDefence.equals(heroesAttackType[i])) {
                    Random random = new Random();
                    int coeff = random.nextInt(9 + 2);
                    damage *= coeff;
                    System.out.println("Critical damage: " + heroesAttackType[i] + " " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth -= damage;
                }
            }
        }
    }

    public static void bossAttacks() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0 && !heroesAttackType[i].equals("Medic")) {
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] -= bossDamage;
                }
            } else if (heroesAttackType[i].equals("Medic")) {
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0; // Медик умер
                    medicAlive = false;
                } else {
                    heroesHealth[i] -= bossDamage;
                }
            }
        }
    }

    public static void medicHeal() {
        if (!medicAlive) {
            return; // Медик мертв, не лечит
        }

        int minHealth = Integer.MAX_VALUE;
        int minHealthIndex = -1;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (!heroesAttackType[i].equals("Medic") && heroesHealth[i] < 100 && heroesHealth[i] > 0) {
                if (heroesHealth[i] < minHealth) {
                    minHealth = heroesHealth[i];
                    minHealthIndex = i;
                }
            }
        }
        if (minHealthIndex != -1) {
            System.out.println("Medic healed: " + heroesAttackType[minHealthIndex]);
            heroesHealth[minHealthIndex] += 50; // Предположим, что медик лечит на 50 единиц
        }
    }

    public static void showStatistics() {
        System.out.println("ROUND " + roundNumber + " ---------------");
        System.out.println("Boss health: " + bossHealth
                + " damage: " + bossDamage + " defence: " + (bossDefence == null ? "None" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] +
                    " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
    }
}