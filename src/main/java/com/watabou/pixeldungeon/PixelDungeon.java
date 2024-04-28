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
package com.watabou.pixeldungeon;

import com.nikita22007.multiplayer.noosa.audio.Sample;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.network.Server;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.scenes.TitleScene;
import com.watabou.utils.Bundle;
import textualmold9830.Preferences;

public class PixelDungeon extends Game {

	public PixelDungeon() {
		super( TitleScene.class );
	}



	protected void onCreate( Bundle savedInstanceState ) {
		updateImmersiveMode();
		Sample.INSTANCE.load(
			Assets.SND_CLICK,
			Assets.SND_BADGE,
			Assets.SND_GOLD,

			Assets.SND_DESCEND,
			Assets.SND_STEP,
			Assets.SND_WATER,
			Assets.SND_OPEN,
			Assets.SND_UNLOCK,
			Assets.SND_ITEM,
			Assets.SND_DEWDROP,
			Assets.SND_HIT,
			Assets.SND_MISS,
			Assets.SND_EAT,
			Assets.SND_READ,
			Assets.SND_LULLABY,
			Assets.SND_DRINK,
			Assets.SND_SHATTER,
			Assets.SND_ZAP,
			Assets.SND_LIGHTNING,
			Assets.SND_LEVELUP,
			Assets.SND_DEATH,
			Assets.SND_CHALLENGE,
			Assets.SND_CURSED,
			Assets.SND_EVOKE,
			Assets.SND_TRAP,
			Assets.SND_TOMB,
			Assets.SND_ALERT,
			Assets.SND_MELD,
			Assets.SND_BOSS,
			Assets.SND_BLAST,
			Assets.SND_PLANT,
			Assets.SND_RAY,
			Assets.SND_BEACON,
			Assets.SND_TELEPORT,
			Assets.SND_CHARMS,
			Assets.SND_MASTERY,
			Assets.SND_PUFF,
			Assets.SND_ROCKS,
			Assets.SND_BURNING,
			Assets.SND_FALLING,
			Assets.SND_GHOST,
			Assets.SND_SECRET,
			Assets.SND_BONES,
			Assets.SND_BEE,
			Assets.SND_DEGRADE,
			Assets.SND_MIMIC );
	}


	@Override
	public void onDestroy(){
		Server.stopServer();
		super.onDestroy();
	}

	public static void switchNoFade( Class<? extends PixelScene> c ) {
		PixelScene.noFade = true;
		switchScene( c );
	}

	/*
	 * ---> Prefernces
	 */

	public static void landscape( boolean value ) {
	}

	public static boolean landscape() {
		return width > height;
	}

	// *** IMMERSIVE MODE ****

	private static boolean immersiveModeChanged = false;

    //@SuppressLint("NewApi")  //now is not new
	public static void immerse( boolean value ) {

	}



	//@SuppressLint("NewApi")  //now is not new
	public static void updateImmersiveMode() {

	}

	public static boolean immersed() {
		return false;
	}

	// *****************************

	public static boolean scaleUp() {
		return true;
	}

	public static void onlineMode( boolean value ) {
		Preferences.onlineMode = value;
	}

	public static boolean onlineMode() {
		return Preferences.onlineMode;
	}

	public static void serverName( String value ) {
	Preferences.serverName = value;
	}

	public static String serverName() {
		return Preferences.serverName;
	}

	public static boolean useCustomRelay() {
		return Preferences.useCustomRelay;
	}
	public static void useCustomRelay(boolean value) {
		Preferences.useCustomRelay = value;
	}

	public static String customRelayAddress(){
		return Preferences.customRelayAddress;
	}
	public static void customRelayAddress(String value) {
		Preferences.customRelayAddress = value;
	}

	public static int customRelayPort(){
		return Preferences.customRelayPort;
	}
	public static void customRelayPort(int value) {
		Preferences.customRelayPort = value;
	}


	public static void challenges( int value ) {
		Preferences.challenges = value;
	}

	public static int challenges() {
		return Preferences.challenges;
	}


	/*
	 * <--- Preferences
	 */

	public static void reportException( Throwable tr ) {
		tr.printStackTrace();
	}




}
