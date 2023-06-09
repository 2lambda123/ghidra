/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package help.screenshot;

import java.awt.Color;

import javax.swing.JFrame;

import generic.theme.GThemeDefaults.Colors.Palette;

/*package*/ abstract class AbstractSearchScreenShots extends GhidraScreenShotGenerator {

	protected static final Color YELLOW_ORANGE = Palette.getColor("darkkhaki");
	protected static final Color BLUE_GREEN = Palette.GREEN;
	protected static final Color NAVY = Palette.getColor("navy");
	protected static final Color DARK_GREEN = Palette.getColor("darkgreen");

	@Override
	protected String getHelpTopicName() {
		return "Search";
	}

	protected void moveTool(final int x, final int y) {
		runSwing(() -> {
			JFrame toolFrame = tool.getToolFrame();
			toolFrame.setLocation(x, y);
		});
	}
}
