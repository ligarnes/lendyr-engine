package net.alteiar.lendyr.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.model.items.Weapon;
import net.alteiar.lendyr.model.items.WeaponType;
import net.alteiar.lendyr.model.persona.*;
import net.alteiar.lendyr.persistence.ItemRepository;

import java.util.UUID;

@Log4j2
@Builder
public class PersonaEntity {
  private static Weapon BARE_HAND = Weapon.builder().attackType(WeaponType.MELEE)
      .ability(Ability.ACCURACY).focus(AbilityFocus.BRAWLING).cost(0)
      .damageDice(1).damageFixed(-3)
      .damageAbility(Ability.PERCEPTION)
      .normalRange(1).longRange(1).penetrating(false).build();

  @NonNull
  private final ItemRepository itemRepository;
  @NonNull
  private final Persona persona;

  public UUID getId() {
    return persona.getId();
  }

  public Size getSize() {
    return persona.getSize();
  }

  public String getName() {
    return persona.getName();
  }

  public boolean isDefeated() {
    return getCurrentHealthPoint() <= 0;
  }

  public Vector2 getPosition() {
    return persona.getPosition();
  }

  public void setPosition(Vector2 position) {
    persona.setPosition(position);
  }

  public float getMoveDistance() {
    double halfSpeed = Math.ceil(persona.getSpeed() / 2d);
    return (float) halfSpeed;
  }

  public int getSpeed() {
    return persona.getSpeed();
  }

  public int getCurrentHealthPoint() {
    return persona.getCurrentHealthPoint();
  }

  public Attack getAttack() {
    PersonaItem item = persona.getEquipped().getEquippedWeapon();
    Weapon weapon = itemRepository.findWeaponById(item.getItemId()).orElse(BARE_HAND);
    return Attack.builder()
        .attackType(weaponToAttackType(weapon.getAttackType()))
        .attack(weapon.getAbility())
        .attackFocus(weapon.getFocus())
        .longRange(weapon.getLongRange())
        .normalRange(weapon.getNormalRange())
        .penetrating(weapon.isPenetrating())
        .diceCount(weapon.getDamageDice())
        .fixedDamage(weapon.getDamageFixed())
        .damageBonus(weapon.getDamageAbility())
        .build();
  }

  private AttackType weaponToAttackType(WeaponType weaponType) {
    return switch (weaponType) {
      case MELEE -> AttackType.MELEE;
      case RANGED -> AttackType.RANGE;
      case MAGIC -> AttackType.MAGIC;
    };
  }

  public Rectangle getAttackLongBoundingBox() {
    return getBoundingBox(getAttack().getLongRange());
  }

  public Rectangle getAttackNormalBoundingBox() {
    return getBoundingBox(getAttack().getNormalRange());
  }

  public Rectangle getDefenceBoundingBox() {
    return getBoundingBox(0);
  }

  public Rectangle getBoundingBoxAt(Vector2 nextPosition) {
    float sourceX = nextPosition.x;
    float sourceY = nextPosition.y;
    float sourceWidth = persona.getSize().getWidth();
    float sourceHeight = persona.getSize().getHeight();

    return new Rectangle(sourceX, sourceY, sourceWidth, sourceHeight);
  }

  private Rectangle getBoundingBox(float extension) {
    float sourceX = persona.getPosition().x - extension;
    float sourceY = persona.getPosition().y - extension;
    float sourceWidth = persona.getSize().getWidth() + extension * 2;
    float sourceHeight = persona.getSize().getHeight() + extension * 2;

    return new Rectangle(sourceX, sourceY, sourceWidth, sourceHeight);
  }

  public int getDefense() {
    return persona.getDefense();
  }

  /**
   * Assign damage to the character and return the unmitigated damage taken.
   *
   * @param damage the damage
   * @return the mitigated damaged
   */
  public int takeDamage(int damage) {
    int totalAr = Math.max(0, persona.getArmorRating());

    int mitigatedDamage = damage - totalAr;
    if (mitigatedDamage <= 0) {
      // All damage mitigated
      log.info("Damage fully mitigated");
      return 0;
    }
    reduceHealthPoint(mitigatedDamage);
    return mitigatedDamage;
  }

  public int takePenetratingDamage(int damage) {
    reduceHealthPoint(damage);
    return damage;
  }

  private void reduceHealthPoint(int damage) {
    int currentHp = persona.getCurrentHealthPoint() - damage;
    persona.setCurrentHealthPoint(currentHp);
  }

  public AbilityStat getAbility(Ability ability) {
    return switch (ability) {
      case ACCURACY -> persona.getAbilities().getAccuracy();
      case COMMUNICATION -> persona.getAbilities().getCommunication();
      case CONSTITUTION -> persona.getAbilities().getConstitution();
      case DEXTERITY -> persona.getAbilities().getDexterity();
      case FIGHTING -> persona.getAbilities().getFighting();
      case INTELLIGENCE -> persona.getAbilities().getIntelligence();
      case PERCEPTION -> persona.getAbilities().getPerception();
      case STRENGTH -> persona.getAbilities().getStrength();
      case WILLPOWER -> persona.getAbilities().getWillpower();
    };
  }

  public Persona toModel() {
    return this.persona;
  }

  @Override
  public String toString() {
    return "PersonaEntity{name=%s}".formatted(persona.getName());
  }
}
