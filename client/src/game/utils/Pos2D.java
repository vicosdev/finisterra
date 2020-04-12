package game.utils;

import com.artemis.E;
import component.position.WorldPos;
import component.position.WorldPosOffsets;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import shared.model.map.Tile;

public class Pos2D {

    public float x;
    public float y;

    public Pos2D(float pX, float pY) {
        this.x = pX;
        this.y = pY;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Pos2D get(@NotNull WorldPos pos, WorldPosOffsets offsets) {
        float offsetX = offsets != null ? offsets.x : 0;
        float offsetY = offsets != null ? offsets.y : 0;
        return new Pos2D(pos.x + offsetX, pos.y + offsetY);
    }

    public static @NotNull Pos2D get(@NotNull E player) {
        return get(player.getWorldPos(), player.getWorldPosOffsets());
    }

    public Pos2D toScreen() {
        return new Pos2D(x * Tile.TILE_PIXEL_WIDTH,
                y * Tile.TILE_PIXEL_HEIGHT + Tile.TILE_PIXEL_HEIGHT);
    }

    @Override
    public String toString() {
        return "X: " + x + " - Y: " + y;
    }
}
