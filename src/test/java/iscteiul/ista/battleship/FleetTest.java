package iscteiul.ista.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

class FleetTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Fleet fleet;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        outputStream.reset();
    }

    @Test
    void printShips() {

        Fleet fleet = new Fleet();

        // Arrange: create ships
        MockShip ship1 = new MockShip("Galeao", 3);
        MockShip ship2 = new MockShip("Fragata", 2);

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        // Act
        Fleet.printShips(fleet.getShips());

        // Assert: verify output contains exact ship.toString()
        String output = outputStream.toString();
        assertAll(
                () -> assertTrue(output.contains(ship1.toString()), "Output should contain ship1"),
                () -> assertTrue(output.contains(ship2.toString()), "Output should contain ship2")
        );
    }

    @Test
    void getShips() {

        Fleet fleet = new Fleet();

        List<IShip> ships = fleet.getShips();

        assertAll(
                () -> assertNotNull(ships, "getShips() should never return null"),
                () -> assertTrue(ships.isEmpty(), "New fleet should have no ships")
        );

        MockShip ship1 = new MockShip("Galeao", 3);
        ship1.setPositions(List.of(new MockPosition(0, 0), new MockPosition(0, 1), new MockPosition(0, 2)));
        ship1.setBoundary(0, 2, 0, 0);

        MockShip ship2 = new MockShip("Fragata", 2);
        ship2.setPositions(List.of(new MockPosition(1, 0), new MockPosition(1, 1)));
        ship2.setBoundary(0, 1, 1, 1);

        assertTrue(fleet.addShip(ship1), "Should add first ship");
        assertTrue(fleet.addShip(ship2), "Should add second ship");

        List<IShip> retrievedShips = fleet.getShips();

        assertAll(
                () -> assertEquals(2, retrievedShips.size(), "Fleet should contain 2 ships"),
                () -> assertSame(ship1, retrievedShips.get(0), "First ship should match ship1"),
                () -> assertSame(ship2, retrievedShips.get(1), "Second ship should match ship2")
        );
    }

    @Test
    void addShip() {

        Fleet fleet = new Fleet();

        MockShip validShip = new MockShip("Galeao", 3);
        validShip.setPositions(List.of(
                new MockPosition(0, 0),
                new MockPosition(0, 1),
                new MockPosition(0, 2)
        ));
        validShip.setBoundary(0, 2, 0, 0); // left, right, top, bottom
        validShip.setFloating(true);
        validShip.setCollisionResult(false); // no collision risk

        boolean result1 = fleet.addShip(validShip);

        assertAll(
                () -> assertTrue(result1, "Valid ship should be added successfully"),
                () -> assertEquals(1, fleet.getShips().size(), "Fleet should contain 1 ship"),
                () -> assertSame(validShip, fleet.getShips().get(0), "Added ship should match original")
        );

        MockShip outOfBoundsShip = new MockShip("Fragata", 2);
        outOfBoundsShip.setPositions(List.of(
                new MockPosition(-1, 0),
                new MockPosition(-1, 1)
        ));
        outOfBoundsShip.setBoundary(-1, 0, -1, 0); // invalid positions
        outOfBoundsShip.setFloating(true);
        outOfBoundsShip.setCollisionResult(false);

        boolean result2 = fleet.addShip(outOfBoundsShip);

        assertAll(
                () -> assertFalse(result2, "Ship outside board should be rejected"),
                () -> assertEquals(1, fleet.getShips().size(), "Fleet size should remain 1")
        );
    }

    @Test
    void getShipsLike() {

        Fleet fleet = new Fleet();

        MockShip ship1 = new MockShip("Galeao", 3);
        ship1.setPositions(List.of(new MockPosition(0, 0)));
        ship1.setBoundary(0, 0, 0, 0);

        MockShip ship2 = new MockShip("Fragata", 2);
        ship2.setPositions(List.of(new MockPosition(1, 0)));
        ship2.setBoundary(1, 1, 0, 0);

        MockShip ship3 = new MockShip("Galeao", 4);
        ship3.setPositions(List.of(new MockPosition(2, 0)));
        ship3.setBoundary(2, 2, 0, 0);

        fleet.addShip(ship1);
        fleet.addShip(ship2);
        fleet.addShip(ship3);

        List<IShip> galleons = fleet.getShipsLike("Galeao");

        assertAll(
                () -> assertEquals(2, galleons.size(), "Should return exactly 2 Galleons"),
                () -> assertSame(ship1, galleons.get(0), "First Galleon should be ship1"),
                () -> assertSame(ship3, galleons.get(1), "Second Galleon should be ship3")
        );

        List<IShip> frigates = fleet.getShipsLike("Fragata");
        assertEquals(1, frigates.size(), "Should return 1 Fragata");
        assertSame(ship2, frigates.get(0), "Returned ship should be ship2");

        List<IShip> barcas = fleet.getShipsLike("Barca");
        assertTrue(barcas.isEmpty(), "Should return empty list for category with no ships");
    }

    @Test
    void getFloatingShips() {

        Fleet fleet = new Fleet();

        MockShip floating1 = new MockShip("Galeao", 3);
        floating1.setPositions(List.of(new MockPosition(0, 0)));
        floating1.setBoundary(0, 0, 0, 0);
        floating1.setFloating(true); // still floating

        MockShip sunk = new MockShip("Fragata", 2);
        sunk.setPositions(List.of(new MockPosition(1, 0)));
        sunk.setBoundary(1, 1, 0, 0);
        sunk.setFloating(false); // sunk

        MockShip floating2 = new MockShip("Nau", 4);
        floating2.setPositions(List.of(new MockPosition(2, 0)));
        floating2.setBoundary(2, 2, 0, 0);
        floating2.setFloating(true); // still floating

        fleet.addShip(floating1);
        fleet.addShip(sunk);
        fleet.addShip(floating2);

        List<IShip> floatingShips = fleet.getFloatingShips();

        assertAll(
                () -> assertEquals(2, floatingShips.size(), "Should return exactly 2 floating ships"),
                () -> assertSame(floating1, floatingShips.get(0), "First floating ship should be floating1"),
                () -> assertSame(floating2, floatingShips.get(1), "Second floating ship should be floating2"),
                () -> assertFalse(floatingShips.contains(sunk), "List should not contain sunk ship")
        );

        fleet = new Fleet(); // empty fleet
        MockShip onlySunk = new MockShip("Galeao", 3);
        onlySunk.setPositions(List.of(new MockPosition(0, 0)));
        onlySunk.setBoundary(0, 0, 0, 0);
        onlySunk.setFloating(false);
        fleet.addShip(onlySunk);

        List<IShip> emptyFloating = fleet.getFloatingShips();
        assertTrue(emptyFloating.isEmpty(), "Should return empty list when all ships are sunk");
    }

    @Test
    void shipAt() {

        Fleet fleet = new Fleet();

        MockShip ship1 = new MockShip("Galeao", 3);
        ship1.setPositions(List.of(
                new MockPosition(0, 0),
                new MockPosition(0, 1),
                new MockPosition(0, 2)
        ));
        ship1.setBoundary(0, 2, 0, 0);
        ship1.setFloating(true);

        MockShip ship2 = new MockShip("Fragata", 2);
        ship2.setPositions(List.of(
                new MockPosition(1, 0),
                new MockPosition(1, 1)
        ));
        ship2.setBoundary(0, 1, 1, 1);
        ship2.setFloating(true);

        fleet.addShip(ship1);
        fleet.addShip(ship2);

        IPosition pos0 = new MockPosition(0, 0);
        IPosition pos1 = new MockPosition(0, 1);
        IPosition pos2 = new MockPosition(0, 2);

        assertAll(
                () -> assertSame(ship1, fleet.shipAt(pos0), "Position (0,0) should return ship1"),
                () -> assertSame(ship1, fleet.shipAt(pos1), "Position (0,1) should return ship1"),
                () -> assertSame(ship1, fleet.shipAt(pos2), "Position (0,2) should return ship1")
        );

        IPosition pos3 = new MockPosition(1, 0);
        IPosition pos4 = new MockPosition(1, 1);

        assertAll(
                () -> assertSame(ship2, fleet.shipAt(pos3), "Position (1,0) should return ship2"),
                () -> assertSame(ship2, fleet.shipAt(pos4), "Position (1,1) should return ship2")
        );

        IPosition emptyPos = new MockPosition(2, 2);
        assertNull(fleet.shipAt(emptyPos), "Position (2,2) should return null");


        Fleet emptyFleet = new Fleet();
        IPosition anyPos = new MockPosition(0, 0);
        assertNull(emptyFleet.shipAt(anyPos), "Empty fleet should return null for any position");
    }

    @Test
    void printStatus() {

        Fleet fleet = new Fleet();

        MockShip g1 = new MockShip("Galeao1", 3);
        g1.setPositions(List.of(new MockPosition(0, 0)));
        g1.setBoundary(0, 0, 0, 0);
        g1.setFloating(true);

        MockShip f1 = new MockShip("Fragata1", 2);
        f1.setPositions(List.of(new MockPosition(1, 0)));
        f1.setBoundary(1, 1, 0, 0);
        f1.setFloating(false);

        MockShip n1 = new MockShip("Nau1", 4);
        n1.setPositions(List.of(new MockPosition(2, 0)));
        n1.setBoundary(2, 2, 0, 0);
        n1.setFloating(true);

        fleet.addShip(g1);
        fleet.addShip(f1);
        fleet.addShip(n1);

        // Act
        fleet.printStatus();

        // Assert: output contains all ship names (duplicates are fine)
        String output = outputStream.toString();
        assertAll(
                () -> assertTrue(output.contains("MockShip{Galeao1}"), "Output should contain Galeao1"),
                () -> assertTrue(output.contains("MockShip{Fragata1}"), "Output should contain Fragata1"),
                () -> assertTrue(output.contains("MockShip{Nau1}"), "Output should contain Nau1")
        );

    }

    @Test
    void printShipsByCategory() {

        Fleet fleet = new Fleet();

        // Arrange
        MockShip ship1 = new MockShip("Galeao", 3);
        MockShip ship2 = new MockShip("Galeao", 2);
        MockShip ship3 = new MockShip("Fragata", 2);

        fleet.addShip(ship1);
        fleet.addShip(ship2);
        fleet.addShip(ship3);

        // Act: print only Galeao ships
        fleet.printShipsByCategory("Galeao");

        String output = outputStream.toString();

        // Assert: use ship.toString() for exact match
        assertAll(
                () -> assertTrue(output.contains(ship1.toString()), "Output should contain ship1"),
                () -> assertTrue(output.contains(ship2.toString()), "Output should contain ship2"),
                () -> assertFalse(output.contains(ship3.toString()), "Output should NOT contain Fragata ship")
        );
    }

    @Test
    void printFloatingShips() {

        Fleet fleet = new Fleet();

        MockShip floating1 = new MockShip("Galeao1", 3);
        floating1.setPositions(List.of(new MockPosition(0, 0)));
        floating1.setBoundary(0, 0, 0, 0);
        floating1.setFloating(true);

        MockShip floating2 = new MockShip("Nau1", 2);
        floating2.setPositions(List.of(new MockPosition(1, 0)));
        floating2.setBoundary(1, 1, 0, 0);
        floating2.setFloating(true);

        MockShip sunk = new MockShip("Fragata1", 2);
        sunk.setPositions(List.of(new MockPosition(2, 0)));
        sunk.setBoundary(2, 2, 0, 0);
        sunk.setFloating(false);

        fleet.addShip(floating1);
        fleet.addShip(floating2);
        fleet.addShip(sunk);

        fleet.printFloatingShips();

        String output = outputStream.toString();
        assertAll(
                () -> assertTrue(output.contains("MockShip{Galeao1}"), "Output should contain floating ship Galeao1"),
                () -> assertTrue(output.contains("MockShip{Nau1}"), "Output should contain floating ship Nau1"),
                () -> assertFalse(output.contains("MockShip{Fragata1}"), "Output should not contain sunk ship Fragata1")
        );

    }

    @Test
    void printAllShips() {

        Fleet fleet = new Fleet();

        MockShip ship1 = new MockShip("Galeao1", 3);
        ship1.setPositions(List.of(new MockPosition(0, 0)));
        ship1.setBoundary(0, 0, 0, 0);
        ship1.setFloating(true);

        MockShip ship2 = new MockShip("Fragata1", 2);
        ship2.setPositions(List.of(new MockPosition(1, 0)));
        ship2.setBoundary(1, 1, 0, 0);
        ship2.setFloating(false);

        MockShip ship3 = new MockShip("Nau1", 4);
        ship3.setPositions(List.of(new MockPosition(2, 0)));
        ship3.setBoundary(2, 2, 0, 0);
        ship3.setFloating(true);

        fleet.addShip(ship1);
        fleet.addShip(ship2);
        fleet.addShip(ship3);

        fleet.printAllShips();

        String output = outputStream.toString();
        assertAll(
                () -> assertTrue(output.contains("MockShip{Galeao1}"), "Output should contain ship1"),
                () -> assertTrue(output.contains("MockShip{Fragata1}"), "Output should contain ship2"),
                () -> assertTrue(output.contains("MockShip{Nau1}"), "Output should contain ship3")
        );

    }

    private class MockShip implements IShip {

        private String category;
        private Integer size;
        private List<IPosition> positions = new ArrayList<>();
        private IPosition basePosition;
        private Compass bearing = Compass.UNKNOWN;

        // Boundary values — fully controllable
        private int leftMost = 0;
        private int rightMost = 0;
        private int topMost = 0;
        private int bottomMost = 0;

        private boolean floating = true;

        // For controlling collision behavior
        private boolean collisionResult = false;

        public MockShip(String category, int size) {
            this.category = category;
            this.size = size;
        }

        // ----------------------------
        // Configuration helpers
        // ----------------------------

        public void setPositions(List<IPosition> posList) {
            this.positions = new ArrayList<>(posList);
        }

        public void setBoundary(int left, int right, int top, int bottom) {
            this.leftMost = left;
            this.rightMost = right;
            this.topMost = top;
            this.bottomMost = bottom;
        }

        public void setFloating(boolean floating) {
            this.floating = floating;
        }

        public void setCollisionResult(boolean result) {
            this.collisionResult = result;
        }

        public void setBearing(Compass bearing) {
            this.bearing = bearing;
        }

        public void setBasePosition(IPosition pos) {
            this.basePosition = pos;
        }

        // ----------------------------
        // Interface implementation
        // ----------------------------

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public Integer getSize() {
            return size;
        }

        @Override
        public List<IPosition> getPositions() {
            return new ArrayList<>(positions);
        }

        @Override
        public IPosition getPosition() {
            return basePosition;
        }

        @Override
        public Compass getBearing() {
            return bearing;
        }

        @Override
        public boolean stillFloating() {
            return floating;
        }

        @Override
        public int getTopMostPos() {
            return topMost;
        }

        @Override
        public int getBottomMostPos() {
            return bottomMost;
        }

        @Override
        public int getLeftMostPos() {
            return leftMost;
        }

        @Override
        public int getRightMostPos() {
            return rightMost;
        }

        @Override
        public boolean occupies(IPosition pos) {
            return positions.contains(pos);
        }

        @Override
        public boolean tooCloseTo(IShip other) {
            return collisionResult;
        }

        @Override
        public boolean tooCloseTo(IPosition pos) {
            // Simplified: adjacency check
            for (IPosition p : positions) {
                if (p.isAdjacentTo(pos) || p.equals(pos)) return true;
            }
            return false;
        }

        @Override
        public void shoot(IPosition pos) {
            // For Fleet tests, no need to simulate damage
            floating = false;
        }

        @Override
        public String toString() {
            return "MockShip{" + category + "}";
        }
    }

    private class MockPosition implements IPosition {

        private int row;
        private int column;
        private boolean occupied = false;
        private boolean hit = false;

        public MockPosition(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public int getRow() {
            return row;
        }

        @Override
        public int getColumn() {
            return column;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof IPosition)) return false;
            IPosition p = (IPosition) other;
            return this.row == p.getRow() && this.column == p.getColumn();
        }

        @Override
        public boolean isAdjacentTo(IPosition other) {
            int dr = Math.abs(this.row - other.getRow());
            int dc = Math.abs(this.column - other.getColumn());
            return dr <= 1 && dc <= 1 && !(dr == 0 && dc == 0);
        }

        @Override
        public void occupy() {
            occupied = true;
        }

        @Override
        public void shoot() {
            hit = true;
        }

        @Override
        public boolean isOccupied() {
            return occupied;
        }

        @Override
        public boolean isHit() {
            return hit;
        }
    }

}