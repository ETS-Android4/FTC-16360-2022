package org.firstinspires.ftc.teamcode.lib.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Spinner {
    HardwareMap hardwareMap;
    enum State {
        SPINNING,
        IDLE
    }

    public State state;

    private DcMotorEx motor;

    public Spinner(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        //initialize Spinner Motor
        motor = hardwareMap.get(DcMotorEx.class, "spinner");
        motor.setDirection(DcMotor.Direction.FORWARD);

        //set inital state
        state = State.IDLE;
    }

    public void setSpinning() {
        state = State.SPINNING;
    }

    public void setIdle() {
        state = State.IDLE;
    }

    public void toggleState() {
        if (state == State.IDLE) {
            state = State.SPINNING;
        } else {
            state = State.IDLE;
        }
    }

    public void update() {
        switch (state) {
            case IDLE:
                motor.setPower(0);
                break;
            case SPINNING:
                motor.setPower(0.7);
                break;
        }
    }

}
/*
Mode de déplacement supérieur à 60 % dans la plupart des villes françaises pour les trajets quotidiens, la marche reste la grande oubliée des politiques publiques. Pourtant les collectivités ont les clés pour avancer sur la question. Tour d'horizon.
public est aujourd'hui organisé autour de la voiture. Pour Françoise Rossignol, présidente du Club des villes et territoires cyclables, « c'est seulement ce qui reste qui est dévolu au vélo et aux piétons. L'idée est de renverser la hiérarchie en faveur du vélo et de la marche ». Le besoin de partager le trottoir, entre files d'attente et cheminement, pendant la crise sanitaire, a rappelé que le plus partagé des modes de déplacement restait le parent pauvre de la mobilité.
Mode de déplacement supérieur à 60 % dans la plupart des villes françaises pour les trajets quotidiens, la marche reste la grande oubliée des politiques publiques, déplore le nouveau collectif Place aux piétons, composé des associations 60 millions de piétons, Rue de l'avenir et la Fédération française de la randonnée pédestre. Une situation paradoxale. Dans son guide sur les aménagements provisoires pour les piétons, le Cerema rappelle que la part modale de la marche représente 70 % pour les déplacements de moins de 1 km, moins de 25 % pour les déplacements de 1 à 2 km et moins de 10 % pour les déplacements compris entre 2 et 3 km, pour devenir marginale au-delà. En Île-de-France, 40 % des déplacements quotidiens se font intégralement à pied et le développement du télétravail a accru la pratique hors du cœur d'agglomération, selon les enquêtes mobilité Covid menées par Île-de-France Mobilités.
L'espace public, clé de voûte de la marchabilité
L'enjeu est de reconquérir cette part perdue de l'espace public. Selon l'anthropologue urbaine et géographe Sonia Lavadhino, créatrice du bureau d'expertise bfluid, « la vraie ville marchable permet de marcher de trente minutes à une heure et de faire du vélo tranquillement une demi-heure ». Et la réponse est multidimensionnelle, les infrastructures piétonnes ne suffisent pas. « Comment faire une ville marchable qui ne soit pas composée de zones piétonnes réservées à un seul mode ? Entre ces deux paradigmes consistant, pour l'un, à segmenter, pour l'autre, à proposer une ville généralisée dans laquelle les gens se baladent dans des espaces partagés, j'opte plutôt pour le second », poursuit Sonia Lavadinho. Selon cette logique, il n'est pas uniquement question d'avancer, mais de se rencontrer, de pouvoir traverser une route en poursuivant une discussion, ou encore d'adapter les espaces urbains aux enfants, afin qu'ils puissent marcher ou circuler à vélo, de façon autonome. Cette tranquillité ne peut faire l'impasse d'une vitesse de circulation ralentie, mais la maîtrise de la vitesse ne suffit pas.
C'est bel et bien l'espace public qui est la clé de voûte de la marchabilité, comme le souligne une note récemment publiée par l'Institut Paris Région, qui met en avant une série de critères. Ainsi, la compacité d'une zone urbaine, formée d'îlots urbains d'une longueur optimale comprise entre 60 et 120 mètres, favorise la marche. La mise en relation piétonne des principales destinations de la ville est également incitative. C'est le cas à Strasbourg (Bas-Rhin), première ville à se doter d'un plan piéton, où le pont du Maire-Kuss, proche de la gare, est aux deux tiers ouvert aux marcheurs. L'incitation à la marche dépend fortement de la densité des habitations et de la mixité de l'occupation des sols. Ce qui se vérifie à Sceaux (Hauts-de-Seine), une des premières villes à avoir sacralisé un quartier piéton, et qui se veut désormais modèle de ville du quart d'heure. « Aujourd'hui, l'objectif est de créer des continuités piétonnes entre la gare RER de Robinson et le centre-ville », explique Patrice Pattee, adjoint à la mairie de Sceaux.
Mettre en place une stratégie
La coprésence de services et d'équipements permet à la fois de répondre aux besoins du marcheur, tels que s'arrêter, boire et manger, et de lui offrir des points d'intérêt et de repère dans l'espace. Enfin, il importe de travailler les interfaces entre les bâtiments et la rue, d'introduire de la végétation sur les trottoirs, comme ces « frontages », des bordures végétalisées des maisons et immeubles qui apaisent certains quartiers de Montréal ou Barcelone. « Les aspects perceptuels, tels que la sécurité et l'esthétique, sont des facteurs importants pour encourager ou contraindre la marche », soulignent Teodora Nikolova, Frédérique Prédali et Gaëtane Carette, de l'Institut Paris Région.
Identifier qui marche ou qui pourrait marcher, cerner les secteurs favorables à la marche, imaginer une « ville de la pantoufle », selon l'expression de l'architecte Philippe Madec. « L'enjeu est de travailler sur l'espace public, sur les intersections, sur les discontinuités des déplacements piétons, et pas seulement en termes d'infrastructures et de réduction de vitesse. Il faut habiter la rue », confirme Louis Boulanger, du cabinet Inddigo, qui accompagne le plan piéton de la métropole de Nancy (Meurthe-et-Moselle). Par où commencer ? « Il faut travailler sur une action forte et visible pour asseoir une politique sur un territoire. Par exemple, une magistrale piétonne avec des points de repères, de respiration, pour changer de carte mentale », estime M. Boulanger.

 */
