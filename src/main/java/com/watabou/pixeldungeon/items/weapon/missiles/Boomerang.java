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
package com.watabou.pixeldungeon.items.weapon.missiles;

import com.nikita22007.multiplayer.server.sprites.MissileSprite;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

public class Boomerang extends MissileWeapon {

	{
		name = "boomerang";
		image(ItemSpriteSheet.BOOMERANG);

		STR = 10;

		stackable = false;
	}

	@Override
	public int min() {
		return isBroken() ? 1 : 1 + level();
	}

	@Override
	public int max() {
		return isBroken() ? 4 : 4 + 2 * level();
	}

	@Override
	public boolean isUpgradable() {
		return true;
	}

	@Override
	public Item upgrade() {
		return upgrade( false );
	}

	@Override
	public Item upgrade( boolean enchant ) {
		super.upgrade( enchant );
		return this;
	}

	@Override
	public int maxDurability( int lvl ) {
		return 8 * (lvl < 16 ? 16 - lvl : 1);
	}

	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		super.proc( attacker, defender, damage );
		if (attacker instanceof Hero && ((Hero)attacker).rangedWeapon == this) {
			circleBack( defender.pos, (Hero)attacker );
		}
	}

	@Override
	protected void miss( int cell ) {
		circleBack( cell, curUser );
	}

	private void circleBack( int from, Hero owner ) {

		MissileSprite.
			reset( from, curUser.pos, curItem, null );

		if (throwEquiped) {
			owner.belongings.setWeapon(this);
			owner.spend( -TIME_TO_EQUIP );
		} else
		if (!collect( curUser.belongings.backpack )) {
			Dungeon.level.drop( this, owner.pos );
		}
	}

	private boolean throwEquiped;

	@Override
	public void cast( Hero user, int dst ) {
		throwEquiped = isEquipped( user );
		super.cast( user, dst );
	}

	@Override
	public String desc() {
		return
			"Thrown to the enemy this flat curved wooden missile will return to the hands of its thrower.";
	}
}
