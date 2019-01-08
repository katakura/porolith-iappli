//
// Porolith for i-appli (505i version)
//
// Copyright(C)2004 by Y.Katakura
//
import com.nttdocomo.ui.*;

public final class Porolith extends IApplication {

    //アプリの開始
    public void start() {
        PorolithCanvas c=new PorolithCanvas();
        Display.setCurrent(c);
        c.exe();
    }
}
