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
package com.watabou.pixeldungeon.windows;

import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.npcs.Blacksmith;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.network.SendData;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.CharSprite;
import com.watabou.pixeldungeon.ui.ItemSlot;
import com.watabou.pixeldungeon.ui.RedButton;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

public class WndBlacksmith extends Window {

	private static final int BTN_SIZE	= 36;
	private static final float GAP		= 2;
	private static final float BTN_GAP	= 10;
	private static final int WIDTH		= 116;

	private ItemButton btnPressed;

	private ItemButton btnItem1;
	private ItemButton btnItem2;
	private RedButton btnReforge;

	private static final String TXT_PROMPT =
		"Ok, a deal is a deal, dat's what I can do for you: I can reforge " +
		"2 items and turn them into one of a better quality.";
	private static final String TXT_SELECT =
		"Select an item to reforge";
	private static final String TXT_REFORGE =
		"Reforge them";

	public WndBlacksmith(Blacksmith troll, final Hero hero ) {

		super(hero);

		CharSprite trollSprite =  troll.sprite();
		String title = Utils.capitalize(troll.name);
		String text = TXT_PROMPT;
		String reforgeText = TXT_REFORGE;

		JSONObject params = new JSONObject();
		try {
			params.put("title", title);
			params.put("text", text);
			params.put("sprite", trollSprite.spriteName());
			params.put("reforge_text", reforgeText);
		} catch (JSONException ignored) {}
		SendData.sendWindow(hero.networkID, "wnd_blacksmith", getId(), params);

		btnItem1 = new ItemButton() {
			@Override
			protected void onClick() {
				btnPressed = btnItem1;
				GameScene.selectItem( hero, itemSelector, WndBag.Mode.UPGRADEABLE, TXT_SELECT );
			}
		};

		btnItem2 = new ItemButton() {
			@Override
			protected void onClick() {
				btnPressed = btnItem2;
				GameScene.selectItem( hero, itemSelector, WndBag.Mode.UPGRADEABLE, TXT_SELECT );
			}
		};

		btnReforge = new RedButton( TXT_REFORGE );
		btnReforge.enable( false );


	}

	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				btnPressed.item( item );

				if (btnItem1.item != null && btnItem2.item != null) {
					String result = Blacksmith.verify( btnItem1.item, btnItem2.item );
					if (result != null) {
						GameScene.show(new WndMessage(result, getOwnerHero()));
						btnReforge.enable( false );
					} else {
						btnReforge.enable( true );
					}
				}
			}
		}
	};

	public static class ItemButton{

		protected ItemSlot slot;

		public Item item = null;


		protected void onClick() {};

		protected void layout() {
		};

		public void item( Item item ) {
			slot.item( this.item = item );
		}
	}
}
