package ch.yannick.display.activityArsenal;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;

import ch.yannick.context.MyBaseActivity;
import ch.yannick.context.R;
import ch.yannick.context.RootApplication;
import ch.yannick.display.views.DiceDisplayer;
import ch.yannick.display.views.ValueControler;
import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.ActionData;
import ch.yannick.intern.dice.Dice;
import ch.yannick.intern.usables.Weapon;

/**
 * Created by Yannick on 14.03.2015.
 */
public class Dialog_Schaden extends MyBaseActivity {

    private static String LOG = "Dialog Schaden";
    private ToggleButton direct;
    private ValueControler schaden, penetration;
    private int mTextPadding;
    private DiceDisplayer diceDisplayer;
    private Weapon w;
    private Action action;
    @Override
    public void react(String res, int Flag) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_schaden);

        mTextPadding = getResources().getDimensionPixelOffset(R.dimen.view_padding);
        try{
            w = ((RootApplication) getApplication()).mCurrentUsable;
        }catch(Exception e){ e.printStackTrace();}


        action = Action.valueOf(getIntent().getStringExtra("action"));
        ActionData actionData = w.getData(action);

        schaden = (ValueControler) findViewById(R.id.schaden);
        diceDisplayer = (DiceDisplayer) findViewById(R.id.degats_dice);

        diceDisplayer.setDices(actionData.resultDice);

        String[] resultString = actionData.resultString.split("\\s*:\\s*");
        penetration.setValue(0);
        if(resultString.length>0 && resultString[0].equals("direct")) {
            direct.setChecked(true);
        }else{
            direct.setChecked(false);
            if(resultString.length>1)
                penetration.setValue(Integer.valueOf(resultString[1]));
        }


        diceDisplayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(LOG,"diceDisplayer.onTouch");
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    diceDisplayer.removeLastDie();
                    return true;
                }
                return false;
            }
        });


        int textSize = getResources().getDimensionPixelOffset(R.dimen.big_text);

        Log.d(LOG, "text Size "+ textSize);

        LinearLayout dicesLayout = (LinearLayout) findViewById(R.id.dices);
        for(final Dice d: Dice.values()){
            DiceDisplayer diceImage = new DiceDisplayer(this);

            diceImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    diceDisplayer.addDice(d);
                    diceDisplayer.requestLayout();
                    diceDisplayer.getParent().requestLayout();
                    diceDisplayer.getParent().getParent().requestLayout();
                }
            });
            diceImage.setTextSize(textSize);
            diceImage.setDice(d);
            diceImage.setPadding(mTextPadding,0,mTextPadding,0);
            dicesLayout.addView(diceImage);
        }
        schaden.invalidate();
        penetration.invalidate();
        diceDisplayer.invalidate();
        dicesLayout.invalidate();
        dicesLayout.requestLayout();
    }

    public void confirmed(View v){
        ActionData actionData = w.getData(action);
        if(direct.isChecked())
            actionData.resultString = "direct";
        else
            actionData.resultString = "penetration: "+penetration.getValue();
        actionData.resultDice = (ArrayList<Dice>) diceDisplayer.getDices();

        super.finish();
    }

    public void cancel(View v){
        super.finish();
    }
}
