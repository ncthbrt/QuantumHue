package co.za.cuthbert.three.ui;

import co.za.cuthbert.three.Iteration3Main;
import co.za.cuthbert.three.ui.actions.ButtonAction;
import co.za.cuthbert.three.ui.actions.HideDialogAction;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Copyright Nick Cuthbert, 2014
 */
public class ConfirmDialog extends Dialog {
    private final Sprite icon;
    private static Sprite okIcon;
    private static Sprite cancelIcon;


    private static final int buttonSpacing = 10;
    private ButtonGroup group;

    public ConfirmDialog(InputMultiplexer multiplexer, Sprite icon, ButtonAction confirmAction, ButtonAction cancelAction) {
        if (okIcon == null) {
            okIcon = Iteration3Main.textureAtlas().createSprite("icon_ok");
            cancelIcon = Iteration3Main.textureAtlas().createSprite("icon_cancel");
        }

        initialise(multiplexer, icon.getRegionWidth(), icon.getRegionHeight());
        offset(-okIcon.getRegionWidth() - buttonSpacing, 0);

        group = new ButtonGroup(buttonAnchorPoint(), ButtonGroup.Direction.DOWN, ButtonGroup.Anchor.TOP_LEFT);

        Array<ButtonAction> confirmActions = new Array<ButtonAction>();
        confirmActions.add(confirmAction);
        confirmActions.add(new HideDialogAction(this));
        group.addButton("Ok", okIcon, confirmActions);

        Array<ButtonAction> cancelActions = new Array<ButtonAction>();
        cancelActions.add(cancelAction);
        cancelActions.add(new HideDialogAction(this));
        group.addButton("Cancel", cancelIcon, cancelActions);
        this.icon = icon;
    }

    @Override
    public void show() {
        super.show();
        multiplexer.addProcessor(group);
    }

    private Vector2 buttonAnchorPoint() {
        Vector2 diagCoord = dialogPosition();
        return new Vector2(diagCoord.x + dialogPatch.getTotalWidth() - buttonSpacing, diagCoord.y + dialogPatch.getTotalHeight());
    }

    private Vector2 iconAnchorPoint() {
        Vector2 diagCoord = contentPosition();
        Vector2 widthHeight = contentWidthHeight();
        return new Vector2(diagCoord.x + (widthHeight.x - icon.getRegionWidth()) / 2f, diagCoord.y + ((widthHeight.y - icon.getRegionHeight()) / 2f));
    }


    @Override
    public void renderAction(SpriteBatch batch, float delta) {
        Vector2 buttonAnchorPoint = buttonAnchorPoint();
        Vector2 iconPosition = iconAnchorPoint();
        group.anchorPoint(buttonAnchorPoint.x, buttonAnchorPoint.y);
        group.render(batch);
        icon.setPosition(iconPosition.x, iconPosition.y);
        icon.draw(batch);
    }

}
