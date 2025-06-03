package com.gruppo07iz.geometrika.forme;

import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

/**
 * La classe {@code Specchiamento} fornisce metodi per applicare e rilevare
 * trasformazioni di specchiatura (orizzontale e verticale) su oggetti {@link Shape}.
 */
public class Specchiamento {

    /**
     * Verifica se la forma è specchiata orizzontalmente (rispetto all'asse X).
     *
     * @param forma la forma da controllare
     * @return {@code true} se è specchiata orizzontalmente, altrimenti {@code false}
     */
    public boolean isSpecchiatoOrizzontalmente(Shape forma) {
        double scalaY = 1;
        for (Transform t : forma.getTransforms()) {
            if (t instanceof Scale) {
                scalaY *= ((Scale) t).getY();
            }
        }
        return scalaY < 0;
    }

    /**
     * Verifica se la forma è specchiata verticalmente (rispetto all'asse Y).
     *
     * @param forma la forma da controllare
     * @return {@code true} se è specchiata verticalmente, altrimenti {@code false}
     */
    public boolean isSpecchiatoVerticalmente(Shape forma) {
        double scalaX = 1;
        for (Transform t : forma.getTransforms()) {
            if (t instanceof Scale) {
                scalaX *= ((Scale) t).getX();
            }
        }
        return scalaX < 0;
    }

    /**
     * Applica o rimuove una specchiatura orizzontale alla forma.
     * La trasformazione avviene rispetto a un punto di riferimento specificato.
     *
     * @param forma               la forma da trasformare
     * @param coordinataCentroX   coordinata X del centro di specchiatura
     * @param coordinataCentroY   coordinata Y del centro di specchiatura
     */
    public void setSpecchiaturaOrizzontale(Shape forma, double coordinataCentroX, double coordinataCentroY) {
        if (isSpecchiatoOrizzontalmente(forma)) {
            // Rimuovi la specchiatura orizzontale (Y negativa)
            rimuoviSpecchiatura(forma, 1, -1);
        } else {
            // Applica specchiatura orizzontale
            forma.getTransforms().add(new Scale(1, -1, coordinataCentroX, coordinataCentroY));
        }
    }

    /**
     * Applica o rimuove una specchiatura verticale alla forma.
     * La trasformazione avviene rispetto a un punto di riferimento specificato.
     *
     * @param forma               la forma da trasformare
     * @param coordinataCentroX   coordinata X del centro di specchiatura
     * @param coordinataCentroY   coordinata Y del centro di specchiatura
     */
    public void setSpecchiaturaVerticale(Shape forma, double coordinataCentroX, double coordinataCentroY) {
        if (isSpecchiatoVerticalmente(forma)) {
            // Rimuovi la specchiatura verticale (X negativa)
            rimuoviSpecchiatura(forma, -1, 1);
        } else {
            // Applica specchiatura verticale
            forma.getTransforms().add(new Scale(-1, 1, coordinataCentroX, coordinataCentroY));
        }
    }

    /**
     * Rimuove la trasformazione di specchiatura che corrisponde ai segni specificati.
     * Questo metodo rimuove l'ultima scala con i segni indicati.
     *
     * @param forma  la forma da modificare
     * @param scalaX segno della scala X da rimuovere (1 o -1)
     * @param scalaY segno della scala Y da rimuovere (1 o -1)
     */
    public void rimuoviSpecchiatura(Shape forma, double scalaX, double scalaY) {
        for (int i = forma.getTransforms().size() - 1; i >= 0; i--) {
            Transform t = forma.getTransforms().get(i);
            if (t instanceof Scale) {
                Scale s = (Scale) t;
                if (Math.signum(s.getX()) == scalaX && Math.signum(s.getY()) == scalaY) {
                    forma.getTransforms().remove(i);
                    break;
                }
            }
        }
    }
}
