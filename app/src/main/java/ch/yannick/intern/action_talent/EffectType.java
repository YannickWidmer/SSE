package ch.yannick.intern.action_talent;

/**
 * Created by Yannick on 18.07.2015.
 * This enum describes the differernt kinds of Talents,
 */
public enum EffectType {
    /*
    * Desfcriptive Talents which are just listed in the app but have no functionality
     */
    DESCRIPTIVE(false),
    /*
    *Talents with this effect change the luck of the corresponding Action
     */
    LUCKMODIFIER(true),
    /*
    * Talents with this effect change the Skill of the corresponding Action
      */
    SKILLMODIFIER(true),
    /*
    * Talents with this effect change the damage dealed of the corresponding Attack Action, not the dices only the constant
    */
    RESULTMODIFIER(true),
    /*
    *Talents with this effect Adds an action to a usable, there might be a Talent of the
    * type LUCKMODIFIER combined with this one, if this is the case it is important that the ALLOWING talent is learned first.
    *
    */
    ALLOWACTION(true),
    /*
    *Talents with this effect change values (Resolver.Value) computed in Resolver
     */
    VALUE(false);

    // this means if it has something to do with actions and hence has to be treated in Weapon.setBoniAndMalus;
    private boolean isAction;
    EffectType(boolean action) {
        isAction = action;
    }

    public boolean isAction(){return isAction;}

}
