package co.za.cuthbert.three.level_editor.actions;

import co.za.cuthbert.three.Level;
import co.za.cuthbert.three.LevelChangeListener;
import co.za.cuthbert.three.level_editor.Button;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class PlayPauseAction extends ButtonAction implements LevelChangeListener {

    private final Sprite playIcon;
    private final Sprite pauseIcon;
    private final Button button;
    public PlayPauseAction(Sprite playIcon, Sprite pauseIcon, Button button){
        this.playIcon=playIcon;
        this.pauseIcon=pauseIcon;
        this.button=button;

    }
    @Override
    public void actionPerformed(String command) {
        if(level!=null){
            if(level.stepping()){
                level.pause();
                button.icon(playIcon);
            }else{
                level.resume();
                button.icon(pauseIcon);
            }
        }
    }

    private Level level;
    @Override
    public void level(Level level) {
        this.level=level;
    }
}
