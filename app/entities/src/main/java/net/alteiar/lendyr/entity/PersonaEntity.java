package net.alteiar.lendyr.entity;

import com.badlogic.gdx.math.Rectangle;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.model.items.Weapon;
import net.alteiar.lendyr.model.items.WeaponType;
import net.alteiar.lendyr.model.map.DynamicBlockingObject;
import net.alteiar.lendyr.model.persona.*;
import net.alteiar.lendyr.persistence.ItemRepository;

import java.util.UUID;

@Log4j2
public class PersonaEntity {
  private static final Weapon BARE_HAND = Weapon.builder().attackType(WeaponType.MELEE)
      .ability(Ability.ACCURACY).focus(AbilityFocus.BRAWLING).cost(0)
      .damageDice(1).damageFixed(-3)
      .damageAbility(Ability.PERCEPTION)
      .normalRange(1).longRange(1).penetrating(false).build();

  private final ItemRepository itemRepository;
  private final Persona persona;

  @Setter
  @Getter
  private Position targetPosition;
  @Setter
  @Getter
  private Position nextPosition;

  @Builder
  public PersonaEntity(@NonNull ItemRepository itemRepository, @NonNull Persona persona) {
    this.itemRepository = itemRepository;
    this.persona = persona;
  }

  public UUID getId() {
    return persona.getId();
  }

  public Size getSize() {
    return persona.getSize();
  }

  public int getLayer() {
    return persona.getPosition().getLayer();
  }

  public String getName() {
    return persona.getName();
  }

  public boolean isDefeated() {
    return getCurrentHealthPoint() <= 0;
  }

  public Position getPosition() {
    return persona.getPosition();
  }

  public void setPosition(Position position) {
    persona.getPosition().setPosition(position);
  }

  public void setPosition(float x, float y, int layer) {
    persona.getPosition().setPosition(x, y, layer);
  }

  public float getMoveDistance() {
    return persona.getSpeed();
  }

  public float getChargeDistance() {
    return getMoveDistance() / 2f;
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

  public DynamicBlockingObject getAttackLongBoundingBox() {
    return getBoundingBox(getAttack().getLongRange());
  }

  public DynamicBlockingObject getAttackNormalBoundingBox() {
    return getBoundingBox(getAttack().getNormalRange());
  }

  public DynamicBlockingObject getDefenceBoundingBox() {
    return getBoundingBox(0);
  }

  public DynamicBlockingObject getBoundingBoxAt(Position nextPosition) {
    float sourceX = nextPosition.getX();
    float sourceY = nextPosition.getY();
    float sourceWidth = persona.getSize().getWidth();
    float sourceHeight = persona.getSize().getHeight();

    return new DynamicBlockingObject(new Rectangle(sourceX, sourceY, sourceWidth, sourceHeight), nextPosition.getLayer());
  }

  private DynamicBlockingObject getBoundingBox(float extension) {
    float sourceX = persona.getPosition().getX() - extension;
    float sourceY = persona.getPosition().getY() - extension;
    float sourceWidth = persona.getSize().getWidth() + extension * 2;
    float sourceHeight = persona.getSize().getHeight() + extension * 2;

    return new DynamicBlockingObject(new Rectangle(sourceX, sourceY, sourceWidth, sourceHeight), persona.getPosition().getLayer());
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

  public PersonaEquipped getEquipped() {
    return persona.getEquipped();
  }

  public Inventory getInventory() {
    return persona.getInventory();
  }
  
  public Persona toModel() {
    return this.persona;
  }

  @Override
  public String toString() {
    return "PersonaEntity{name=%s}".formatted(persona.getName());
  }
}
