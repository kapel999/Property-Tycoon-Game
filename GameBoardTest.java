/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

public class GameBoardTest {

	private GameBoard board;
	private Player[] players;

	@Before
	public void setUp() {
		players = new Player[6];
		Texture texture = new Texture(Gdx.files.internal("tokens/token1.png"));
		for(int i=0; i<players.length; i++) {
			players[i] = new Players("player" + i, texture);
		}
		board = new GameBoard(players);
	}

}