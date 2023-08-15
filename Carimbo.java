package PedroCarimbo;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Carimbo extends AdvancedRobot {
    double moveDirection = 1;  // Inicializa a direção de movimento como positiva

    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);

        while (true) {
            setTurnRadarRight(Double.POSITIVE_INFINITY);

            // Ao bater na parede, muda a direção de movimento e gira 135 graus para reorientação
            if (isWallHit()) {
                moveDirection *= -1;
                setTurnRight(135);
                setAhead(100 * moveDirection);
            } else {
                // Move-se em um círculo suave enquanto mantém a mira no inimigo
                setTurnRightRadians(Math.PI / 2);
                setAhead(100 * moveDirection);
            }

            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent event) {
        // Calcula o ângulo absoluto para mirar diretamente no inimigo
        double absoluteBearing = getHeadingRadians() + event.getBearingRadians();
        // Calcula o ângulo para girar a arma e mirar no inimigo
        double gunTurnAngle = Utils.normalRelativeAngle(absoluteBearing - getGunHeadingRadians());

        // Calcula a distância até o inimigo
        double enemyDistance = event.getDistance();

        // Calcula a potência do tiro com base na distância
        double bulletPower = Math.min(3.0, getEnergy() / 10);

        // Gira a arma para mirar no inimigo
        setTurnGunRightRadians(gunTurnAngle);

        // Se a arma estiver apontada na direção correta, atira
        if (Math.abs(gunTurnAngle) < Math.toRadians(10)) {
            setFire(bulletPower);
        }

        // Evita colisões com o inimigo
        if (enemyDistance < 100) {
            setBack(10);
        }
    }

    // Verifica se o robô está próximo de uma parede
    private boolean isWallHit() {
        return (getX() <= 50 || getX() >= getBattleFieldWidth() - 50 ||
                getY() <= 50 || getY() >= getBattleFieldHeight() - 50);
    }

}
