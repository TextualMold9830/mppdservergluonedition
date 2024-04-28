/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.items.armor;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.EquipableItem;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.armor.glyphs.*;
import com.watabou.pixeldungeon.sprites.HeroSprite;
import com.watabou.pixeldungeon.sprites.ItemSpriteGlowing;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.watabou.pixeldungeon.network.SendData.sendUpdateItemFull;

public class Armor extends EquipableItem {

	private static final int HITS_TO_KNOW	= 10;

	private static final String TXT_EQUIP_CURSED	= "your %s constricts around you painfully";

	private static final String TXT_IDENTIFY	= "you are now familiar enough with your %s to identify it. It is %s.";

	private static final String TXT_TO_STRING	= "%s :%d";
	private static final String TXT_BROKEN		= "broken %s :%d";

	private static final String TXT_INCOMPATIBLE =
		"Interaction of different types of magic has erased the glyph on this armor!";

	public int tier;
	public int STR;

	private int hitsToKnow = HITS_TO_KNOW;

	public Glyph glyph;

	public Armor( int tier ) {

		this.tier = tier;

		STR = typicalSTR();
	}

	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String GLYPH			= "glyph";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, hitsToKnow );
		bundle.put( GLYPH, glyph );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			hitsToKnow = HITS_TO_KNOW;
		}
		inscribe( (Glyph)bundle.get( GLYPH ) );
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
		return actions;
	}

	@Override
	public boolean doEquip( Hero hero ) {

		detach( hero.belongings.backpack );

		if (hero.belongings.getArmor() == null || hero.belongings.getArmor().doUnequip( hero, true, false )) {

			hero.belongings.setArmor(this);

			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( TXT_EQUIP_CURSED, toString() );
			}

			((HeroSprite) hero.getSprite()).updateArmor();

			hero.spendAndNext( time2equip( hero ) );
			return true;

		} else {

			collect( hero.belongings.backpack );
			return false;

		}
	}

	@Override
	protected float time2equip( Hero hero ) {
		return 2 / hero.speed();
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

			hero.belongings.setArmor(null);
			((HeroSprite) hero.getSprite()).updateArmor();

			return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.getArmor() == this;
	}

	public int DR() {
		return tier * (2 + effectiveLevel() + (glyph == null ? 0 : 1));
	}

	@Override
	public Item upgrade() {
		return upgrade( false );
	}

	public Item upgrade( boolean inscribe ) {

		if (glyph != null) {
			if (!inscribe && Random.Int( level() ) > 0) {
				GLog.w( TXT_INCOMPATIBLE );
				inscribe( null );
			}
		} else {
			if (inscribe) {
				inscribe();
			}
		};

		STR--;

		return super.upgrade();
	}

	public Item safeUpgrade() {
		return upgrade( glyph != null );
	}

	@Override
	public Item degrade() {
		STR++;
		return super.degrade();
	}

	@Override
	public int maxDurability( int lvl ) {
		return 6 * (lvl < 16 ? 16 - lvl : 1);
	}

	public int proc( Char attacker, Char defender, int damage ) {

		if (glyph != null) {
			damage = glyph.proc( this, attacker, defender, damage );
		}

		if (!levelKnown) {
			if (--hitsToKnow <= 0) {
				levelKnown = true;
				GLog.w( TXT_IDENTIFY, name(), toString() );
				Badges.validateItemLevelAquired( this );
			}
		}
		if (defender instanceof Hero) {
			use((Hero) defender);
		}
		return damage;
	}

	@Override
	public String toString() {
		return levelKnown ? Utils.format( isBroken() ? TXT_BROKEN : TXT_TO_STRING, super.toString(), STR ) : super.toString();
	}

	@Override
	public String name() {
		return glyph == null ? super.name() : glyph.name( super.name() );
	}

	@Override
	public String info(Hero hero) {
		String name = name();
		StringBuilder info = new StringBuilder( desc() );

		if (levelKnown) {
			info.append(
				"\n\nThis " + name + " provides damage absorption up to " +
				"" + Math.max( DR(), 0 ) + " points per attack. " );

			if (STR > hero.STR()) {

				if (isEquipped( hero )) {
					info.append(
						"\n\nBecause of your inadequate strength your " +
						"movement speed and defense skill is decreased. " );
				} else {
					info.append(
						"\n\nBecause of your inadequate strength wearing this armor " +
						"will decrease your movement speed and defense skill. " );
				}

			}
		} else {
			info.append(
				"\n\nTypical " + name + " provides damage absorption up to " + typicalDR() + " points per attack " +
				" and requires " + typicalSTR() + " points of strength. " );
			if (typicalSTR() > hero.STR()) {
				info.append( "Probably this armor is too heavy for you. " );
			}
		}

		if (glyph != null) {
			info.append( "It is enchanted." );
		}

		if (isEquipped(hero )) {
			info.append( "\n\nYou are wearing the " + name +
				(cursed ? ", and because it is cursed, you are powerless to remove it." : ".") );
		} else {
			if (cursedKnown && cursed) {
				info.append( "\n\nYou can feel a malevolent magic lurking within the " + name + "." );
			}
		}

		return info.toString();
	}

	@Override
	public Item random() {
		if (Random.Float() < 0.4) {
			int n = 1;
			if (Random.Int( 3 ) == 0) {
				n++;
				if (Random.Int( 3 ) == 0) {
					n++;
				}
			}
			if (Random.Int( 2 ) == 0) {
				upgrade( n );
			} else {
				degrade( n );
				cursed = true;
			}
		}

		if (Random.Int( 10 ) == 0) {
			inscribe();
		}

		return this;
	}

	public int typicalSTR() {
		return 7 + tier * 2;
	}

	public int typicalDR() {
		return tier * 2;
	}

	@Override
	public int price() {
		int price = 10 * (1 << (tier - 1));
		if (glyph != null) {
			price *= 1.5;
		}
		return considerState( price );
	}

	public Armor inscribe(Glyph glyph ) {
		this.glyph = glyph;
		if (glyph == null) {
			setGlowing(null);
		} else {
			setGlowing(glyph.glowing());
		}
		sendUpdateItemFull(this);
		return this;
	}

	public Armor inscribe() {

		Class<? extends Glyph> oldGlyphClass = glyph != null ? glyph.getClass() : null;
		Glyph gl = Glyph.random();
		while (gl.getClass() == oldGlyphClass) {
			gl = Glyph.random();
		}

		return inscribe( gl );
	}

	public boolean isInscribed() {
		return glyph != null;
	}

	public static abstract class Glyph implements Bundlable {

		private static final Class<?>[] glyphs = new Class<?>[]{
			Bounce.class, Affection.class, AntiEntropy.class, Multiplicity.class,
			Potential.class, Metabolism.class, Stench.class, Viscosity.class,
			Displacement.class, Entanglement.class, AutoRepair.class };

		private static final float[] chances= new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		public abstract int proc( Armor armor, Char attacker, Char defender, int damage );

		public String name( String armorName ) {
			return armorName;
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}

		public ItemSpriteGlowing glowing() {
			return ItemSpriteGlowing.WHITE;
		}

		public boolean checkOwner( Char owner ) {
			if (!owner.isAlive() && owner instanceof Hero) {

				((Hero)owner).killerGlyph = this;
				Badges.validateDeathFromGlyph();
				return true;

			} else {
				return false;
			}
		}

		@SuppressWarnings("unchecked")
		public static Glyph random() {
			try {
				return ((Class<Glyph>)glyphs[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				return null;
			}
		}

	}
}
