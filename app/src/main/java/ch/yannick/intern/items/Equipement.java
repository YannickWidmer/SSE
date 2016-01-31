package ch.yannick.intern.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.yannick.intern.action_talent.Action;
import ch.yannick.intern.action_talent.Talent;
import ch.yannick.intern.personnage.HitZone;
import ch.yannick.intern.personnage.Limb;
import ch.yannick.intern.state.MentalState;
import ch.yannick.intern.usables.Usable;
import ch.yannick.intern.usables.Weapon;

/**
 * Created by Yannick on 19.08.2015.
 * this class represents what the Hero has equipped, these items have effects on him also the weight has some effects.
 */
public class Equipement {
    private Map<Limb,Weapon> mWeapons;
    private ArrayList<Armor> mArmor;

    public Equipement(){
        mArmor = new ArrayList<>();
        mWeapons = new HashMap<>();
    }

    public void setTalents(Map<Talent,Integer> talents, MentalState mentalState){
        for(Usable usable: mWeapons.values())
            usable.setTalents(talents,mentalState);
    }

    public int getProtection(HitZone part){
        int res = 0;
        for(Armor a:mArmor){
            if(a.getPart()==part)
                res+= a.getProtection();
        }
        return res;
    }

    public void setArmor(ArrayList<Armor> armor){
        mArmor = armor;
    }

    public ArrayList<Armor> getAllArmor() {
        return mArmor;
    }

    public void put(Armor armor) {
        this.mArmor.add(armor);
    }

    public void removeArmor(Armor armor) {
        for(Iterator<Armor> it = mArmor.iterator();it.hasNext();){
            if(it.next().getId().equals(armor.getId())) {
                it.remove();
                break;
            }
        }
    }

    public void removeWeapon(Limb limb){
        mWeapons.remove(limb);
    }

    public void setWeapon(Weapon w, Limb limb){
        if(limb == Limb.LEFTHAND || limb == Limb.RIGHTHAND || limb == Limb.BOTHHANDS)
            mWeapons.remove(Limb.BOTHHANDS);
        if((w.getTyp()).isTwohanded()) {
            limb = Limb.BOTHHANDS;
            mWeapons.remove(Limb.LEFTHAND);
            mWeapons.remove(Limb.RIGHTHAND);
        }
        mWeapons.put(limb,w);
    }

    public boolean hasWeapon(Limb which){
        return mWeapons.containsKey(which);
    }

    public Weapon getWeapon(Limb which) {
        if(mWeapons.containsKey(which))
            return mWeapons.get(which);
        return null;
    }

    public List<Action> getActions(Limb which){
        Set<Action> set = new HashSet<>();
        // Add the resolved actions
        if(which == Limb.ALL)
            for(Weapon w: mWeapons.values())
                set.addAll(w.getActions());
        if(mWeapons.containsKey(which))
            set.addAll(mWeapons.get(which).getActions());

        // Remove both hands action if has two weapons
        if(wearsTwoWeapons()){
            Iterator<Action> it = set.iterator();
            while(it.hasNext()){
                if(it.next().is("TWOHANDED")) // TODO test if it works
                    it.remove();
            }
        }
        return new ArrayList<>(set);
    }

    private boolean wearsTwoWeapons(){
        return mWeapons.containsKey(Limb.LEFTHAND) && mWeapons.containsKey(Limb.RIGHTHAND);
        // When two swords are combined they become one twohanded weapon
    }

    public int getWeight() {
        int weight = 0;
        for(Armor armor:mArmor)
            weight += armor.mWeight;
        for(Usable usable: mWeapons.values())
            if(usable instanceof  Weapon)
               weight += ((Weapon)usable).getWeight();
        return weight;
    }

    public void combine(Limb limb) {
        if(mWeapons.containsKey(Limb.LEFTHAND)&& mWeapons.containsKey(Limb.RIGHTHAND)){
            Limb other = Limb.LEFTHAND;
            if(limb == Limb.LEFTHAND)
                other = Limb.RIGHTHAND;
            mWeapons.put(Limb.BOTHHANDS,mWeapons.get(limb).combine(mWeapons.get(other)));
            mWeapons.remove(Limb.LEFTHAND);
            mWeapons.remove(Limb.RIGHTHAND);
        }
    }
}
