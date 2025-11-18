package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    // ---------- Reflection init ----------
    private void initCounters(Game game) throws Exception {
        for (String field : new String[]{"countHits", "countSinks"}) {
            Field f = Game.class.getDeclaredField(field);
            f.setAccessible(true);
            f.set(game, 0);
        }
    }

    // ---------- Invalid shots ----------
    @ParameterizedTest
    @CsvSource({
            "-1,5", "5,-1", "20,2", "2,20"
    })
    void invalidShotsIncrementCounter(int r, int c) throws Exception {
        Fleet fleet = new Fleet();
        Game game = new Game(fleet);
        initCounters(game);

        game.fire(new Position(r, c));

        assertEquals(1, game.getInvalidShots());
        assertEquals(0, game.getShots().size());
    }

    // ---------- Valid shots (no hit) ----------
    @ParameterizedTest
    @CsvSource({
            "0,0", "1,2", "5,5", "9,9"
    })
    void validShotsAreRecorded(int r, int c) throws Exception {
        Fleet fleet = new Fleet();
        Game game = new Game(fleet);
        initCounters(game);

        Position p = new Position(r, c);
        game.fire(p);

        assertEquals(1, game.getShots().size());
        assertEquals(0, game.getHits());
    }

    // ---------- Repeated shots ----------
    @ParameterizedTest
    @CsvSource({
            "3,3", "4,4"
    })
    void repeatedShotsIncrementCounter(int r, int c) throws Exception {
        Fleet fleet = new Fleet();
        Game game = new Game(fleet);
        initCounters(game);

        Position p = new Position(r, c);

        game.fire(p);
        game.fire(p);

        assertEquals(1, game.getRepeatedShots());
    }

    // ---------- Hit but not sink ----------
    @ParameterizedTest
    @CsvSource({
            "2,2"
    })
    void singleHitDoesNotSink(int row, int col) throws Exception {
        Fleet fleet = new Fleet();
        Caravel cv = new Caravel(Compass.NORTH, new Position(row, col)); // 2 positions
        fleet.addShip(cv);

        Game game = new Game(fleet);
        initCounters(game);

        // Hit only first segment
        game.fire(new Position(row, col));

        assertEquals(1, game.getHits());
        assertEquals(0, game.getSunkShips());
    }

    // ---------- Sink ----------
    @ParameterizedTest
    @CsvSource({
            "3,3"
    })
    void hittingAllSegmentsSinksShip(int row, int col) throws Exception {
        Fleet fleet = new Fleet();
        Caravel cv = new Caravel(Compass.NORTH, new Position(row, col));
        fleet.addShip(cv);

        Game game = new Game(fleet);
        initCounters(game);

        game.fire(new Position(row, col));
        IShip sunk = game.fire(new Position(row+1, col));

        assertNotNull(sunk);
        assertEquals(1, game.getSunkShips());
    }

    // ---------- remaining ships ----------
    @Test
    void remainingShipsUpdatesCorrectly() throws Exception {
        Fleet fleet = new Fleet();
        Barge b1 = new Barge(Compass.NORTH, new Position(1,1));
        Barge b2 = new Barge(Compass.NORTH, new Position(3,1));
        fleet.addShip(b1);
        fleet.addShip(b2);

        Game game = new Game(fleet);
        initCounters(game);

        assertEquals(2, game.getRemainingShips());

        game.fire(new Position(1,1));
        assertEquals(1, game.getRemainingShips());
    }

    // ---------- printValidShots ----------
    @Test
    void printValidShotsDoesNotThrow() throws Exception {
        Fleet fleet = new Fleet();
        Game game = new Game(fleet);
        initCounters(game);

        game.fire(new Position(4,4));

        assertDoesNotThrow(game::printValidShots);
    }

    // ---------- printFleet ----------
    @Test
    void printFleetDoesNotThrow() throws Exception {
        Fleet fleet = new Fleet();
        fleet.addShip(new Barge(Compass.NORTH, new Position(2,2)));

        Game game = new Game(fleet);
        initCounters(game);

        assertDoesNotThrow(game::printFleet);
    }

    // ---------- printBoard (indirect) ----------
    @Test
    void printBoardCoversInternalLoops() throws Exception {
        Fleet fleet = new Fleet();
        Barge b = new Barge(Compass.NORTH, new Position(3,3));
        fleet.addShip(b);

        Game game = new Game(fleet);
        initCounters(game);

        List<IPosition> pos = b.getPositions();

        assertDoesNotThrow(() -> game.printBoard(pos, '#'));
    }
}
