package byow.Core;

import byow.TileEngine.TETile;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class tests {
    @Test
    public void test31() {
        Engine e = new Engine();
        TETile[][] b = e.interactWithInputString("n8391172972297503990swswswwawadas");
        e.interactWithInputString("n8391172972297503990swswswwawa:q");
        TETile[][] z = e.interactWithInputString("ldas");
        assertThat(b).isEqualTo(z);
    }
}
