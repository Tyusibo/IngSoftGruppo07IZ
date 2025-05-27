package com.gruppo07iz.geometrika.state;

import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

public interface StateInterface {
    public void clickSinistro(MouseEvent event);
    public void clickDestro(ContextMenuEvent event);
    public void trascinamento(MouseEvent event);
    public void mouseRilasciato(MouseEvent event);
    public void movimentoMouse(MouseEvent event);
}
