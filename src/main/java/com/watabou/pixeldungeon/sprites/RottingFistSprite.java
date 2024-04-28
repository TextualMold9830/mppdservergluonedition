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
package com.watabou.pixeldungeon.sprites;

import com.nikita22007.multiplayer.noosa.Camera;
import com.watabou.pixeldungeon.Assets;

public class RottingFistSprite extends MobSprite {

	private static final float FALL_SPEED	= 64;

	public RottingFistSprite() {
		super();
		texture( Assets.ROTTING );
		idle = new Animation( 2, true );
		play( idle );
	}

	@Override
	public void attack( int cell ) {
		super.attack( cell );

		/*
		speed.set( 0, -FALL_SPEED );
		acc.set( 0, FALL_SPEED * 4 );
		 */
	}

	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim );
		if (anim == attack) {
			/*
			speed.set( 0 );
			acc.set( 0 );
			 */
			place( ch.pos );

			Camera.shake( 4, 0.2f );
		}
	}
}
