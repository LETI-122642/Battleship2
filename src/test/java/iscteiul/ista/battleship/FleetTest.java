package iscteiul.ista.battleship;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import static org.junit.jupiter.api.condition.OS.*;
import static org.junit.jupiter.api.condition.JRE.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Comprehensive test class for Fleet implementation using JUnit 5 features.
 * Demonstrates advanced testing strategies and annotation usage.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("🚢 Fleet Management System Comprehensive Tests")
class FleetTest {

    private Fleet fleet;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private static int testCounter = 0;

    // ===== LIFECYCLE METHODS =====

    @BeforeAll
    @DisplayName("🚀 Global Test Suite Setup")
    static void globalSetUp() {
        System.out.println("=== Starting Fleet Test Suite ===");
        testCounter = 0;
    }

    @AfterAll
    @DisplayName("🏁 Global Test Suite Cleanup")
    static void globalTearDown() {
        System.out.println("=== Fleet Test Suite Completed ===");
        System.out.println("Total tests executed: " + testCounter);
    }

    @BeforeEach
    @DisplayName("🔄 Individual Test Setup")
    void setUp() {
        fleet = new Fleet();
        System.setOut(new PrintStream(outputStream));
        testCounter++;
        System.out.println("Initializing test #" + testCounter);
    }

    @AfterEach
    @DisplayName("🧹 Individual Test Cleanup")
    void tearDown() {
        System.setOut(originalOut);
        outputStream.reset();
    }

    // ===== NESTED TEST CLASSES =====

    @Nested
    @DisplayName("📥 Ship Addition Operations")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ShipAdditionTests {

        @Test
        @DisplayName("✅ Add Valid Ship to Empty Fleet")
        void addShip_ValidShip_ShouldAddSuccessfully() {
            // Arrange
            IShip ship = createMockShip("Santa Maria", "Caravela", 3, 0, 0, Compass.EAST, true, false);

            // Act
            boolean result = fleet.addShip(ship);

            // Assert
            assertAll(
                    () -> assertTrue(result, "Ship addition should succeed"),
                    () -> assertEquals(1, fleet.getShips().size(), "Fleet should contain one ship"),
                    () -> assertEquals(ship, fleet.getShips().get(0), "Added ship should match original")
            );
        }

        @Test
        @DisplayName("❌ Prevent Addition of Ship Outside Board Boundaries")
        void addShip_OutsideBoard_ShouldNotAdd() {
            // Arrange
            IShip ship = createMockShip("Ghost Ship", "Galeao", 5, -1, 0, Compass.EAST, true, false);

            // Act
            boolean result = fleet.addShip(ship);

            // Assert
            assertAll(
                    () -> assertFalse(result, "Ship outside board should be rejected"),
                    () -> assertTrue(fleet.getShips().isEmpty(), "Fleet should remain empty")
            );
        }

        @Test
        @DisplayName("🚫 Prevent Addition Due to Collision Risk")
        void addShip_CollisionRisk_ShouldNotAdd() {
            // Arrange
            IShip ship1 = createMockShip("Ship Alpha", "Galeao", 5, 0, 0, Compass.EAST, true, false);
            IShip ship2 = createMockShip("Ship Beta", "Fragata", 4, 0, 0, Compass.EAST, true, true);

            // Act
            fleet.addShip(ship1);
            boolean result = fleet.addShip(ship2);

            // Assert
            assertAll(
                    () -> assertFalse(result, "Colliding ship should be rejected"),
                    () -> assertEquals(1, fleet.getShips().size(), "Only one ship should be present")
            );
        }

        @ParameterizedTest(name = "Position ({0},{1}) with bearing {2} should {3}")
        @DisplayName("📊 Parameterized Ship Placement Validation")
        @CsvSource({
                "0, 0, east, true, 'Valid starting position'",
                "5, 3, south, true, 'Valid middle position'",
                "9, 9, east, false, 'Invalid edge position'",
                "10, 0, east, false, 'Outside horizontal boundary'",
                "0, 10, south, false, 'Outside vertical boundary'"
        })
        void addShip_ParameterizedPositions(int startX, int startY, String bearing,
                                            boolean expectedSuccess, String description) {
            // Arrange
            Compass compass = Compass.charToCompass(bearing.charAt(0));
            IShip ship = createMockShip("Test Ship", "Barca", 2, startX, startY, compass, true, false);

            // Act
            boolean result = fleet.addShip(ship);

            // Assert
            assertEquals(expectedSuccess, result,
                    String.format("Failed: %s - Expected %s but was %s",
                            description, expectedSuccess, result));
        }

        @Test
        @DisplayName("📏 Fleet Capacity Limit Enforcement")
        void addShip_FleetAtMaxCapacity_ShouldNotAdd() {
            // Arrange - Fill fleet to capacity
            for (int i = 0; i < 10; i++) {
                IShip ship = createMockShip("Ship" + i, "Barca", 2, i * 3, 0, Compass.EAST, true, false);
                assertTrue(fleet.addShip(ship), "Should add ship #" + i);
            }

            // Act - Try to exceed capacity
            IShip extraShip = createMockShip("Extra Ship", "Barca", 2, 30, 0, Compass.EAST, true, false);
            boolean result = fleet.addShip(extraShip);

            // Assert
            assertFalse(result, "Should reject ship when fleet is at capacity");
            assertEquals(10, fleet.getShips().size(), "Fleet should remain at capacity");
        }
    }

    @Nested
    @DisplayName("🔍 Ship Retrieval and Query Operations")
    class ShipRetrievalTests {

        @Test
        @DisplayName("📭 Retrieve Ships from Empty Fleet")
        void getShips_EmptyFleet_ShouldReturnEmptyList() {
            // Act
            List<IShip> ships = fleet.getShips();

            // Assert
            assertAll(
                    () -> assertTrue(ships.isEmpty(), "List should be empty"),
                    () -> assertEquals(0, ships.size(), "List size should be zero"),
                    () -> assertNotNull(ships, "List should not be null")
            );
        }

        @Test
        @DisplayName("📋 Retrieve All Ships from Populated Fleet")
        void getShips_WithShips_ShouldReturnAllShips() {
            // Arrange
            IShip ship1 = createMockShip("Ship Uno", "Galeao", 5, 0, 0, Compass.EAST, true, false);
            IShip ship2 = createMockShip("Ship Dos", "Fragata", 4, 5, 0, Compass.EAST, true, false);
            fleet.addShip(ship1);
            fleet.addShip(ship2);

            // Act
            List<IShip> ships = fleet.getShips();

            // Assert
            assertAll(
                    () -> assertEquals(2, ships.size(), "Should return exactly two ships"),
                    () -> assertTrue(ships.contains(ship1), "Should contain first ship"),
                    () -> assertTrue(ships.contains(ship2), "Should contain second ship"),
                    () -> assertIterableEquals(List.of(ship1, ship2), ships, "Should maintain insertion order")
            );
        }

        @ParameterizedTest
        @DisplayName("🎯 Filter Ships by Various Categories")
        @ValueSource(strings = {"Galeao", "Fragata", "Nau", "Caravela", "Barca"})
        void getShipsLike_VariousCategories(String category) {
            // Arrange
            IShip ship1 = createMockShip("Ship1", "Galeao", 5, 0, 0, Compass.EAST, true, false);
            IShip ship2 = createMockShip("Ship2", "Fragata", 4, 5, 0, Compass.EAST, true, false);
            IShip ship3 = createMockShip("Ship3", "Galeao", 5, 0, 5, Compass.EAST, true, false);
            fleet.addShip(ship1);
            fleet.addShip(ship2);
            fleet.addShip(ship3);

            // Act
            List<IShip> filteredShips = fleet.getShipsLike(category);

            // Assert
            switch (category) {
                case "Galeao":
                    assertEquals(2, filteredShips.size(), "Should find two Galleons");
                    break;
                case "Fragata":
                    assertEquals(1, filteredShips.size(), "Should find one Frigate");
                    break;
                default:
                    assertTrue(filteredShips.isEmpty(),
                            "Should return empty list for category: " + category);
            }

            // Verify all returned ships match the requested category
            assertTrue(filteredShips.stream()
                            .allMatch(ship -> ship.getCategory().equals(category)),
                    "All returned ships should match category: " + category);
        }

        @Test
        @DisplayName("🛟 Retrieve Only Floating Ships from Mixed Fleet")
        void getFloatingShips_MixedFloatingAndSunk_ShouldReturnOnlyFloating() {
            // Arrange
            IShip floatingShip1 = createMockShip("Unsinkable", "Galeao", 5, 0, 0, Compass.EAST, true, false);
            IShip sunkShip = createMockShip("Titanic", "Fragata", 4, 5, 0, Compass.EAST, false, false);
            IShip floatingShip2 = createMockShip("Survivor", "Nau", 3, 0, 5, Compass.EAST, true, false);

            fleet.addShip(floatingShip1);
            fleet.addShip(sunkShip);
            fleet.addShip(floatingShip2);

            // Act
            List<IShip> floatingShips = fleet.getFloatingShips();

            // Assert
            assertAll(
                    () -> assertEquals(2, floatingShips.size(), "Should return two floating ships"),
                    () -> assertTrue(floatingShips.contains(floatingShip1), "Should contain first floating ship"),
                    () -> assertTrue(floatingShips.contains(floatingShip2), "Should contain second floating ship"),
                    () -> assertFalse(floatingShips.contains(sunkShip), "Should not contain sunk ship"),
                    () -> assertTrue(floatingShips.stream().allMatch(IShip::stillFloating),
                            "All returned ships should be floating")
            );
        }

        @Test
        @DisplayName("⚓️ Retrieve Empty List When All Ships Are Sunk")
        void getFloatingShips_AllSunk_ShouldReturnEmptyList() {
            // Arrange
            IShip sunkShip1 = createMockShip("Wreck1", "Galeao", 5, 0, 0, Compass.EAST, false, false);
            IShip sunkShip2 = createMockShip("Wreck2", "Fragata", 4, 5, 0, Compass.EAST, false, false);

            fleet.addShip(sunkShip1);
            fleet.addShip(sunkShip2);

            // Act
            List<IShip> floatingShips = fleet.getFloatingShips();

            // Assert
            assertTrue(floatingShips.isEmpty(), "Should return empty list when all ships are sunk");
        }
    }

    @Nested
    @DisplayName("📍 Position-based Operations")
    class PositionBasedTests {

        @Test
        @DisplayName("🎯 Find Ship at Occupied Position")
        void shipAt_PositionOccupied_ShouldReturnShip() {
            // Arrange
            MockPosition position = new MockPosition(2, 0);
            IShip ship = createMockShip("Positioned Ship", "Galeao", 5, 0, 0, Compass.EAST, true, false);
            fleet.addShip(ship);

            // Act
            IShip result = fleet.shipAt(position);

            // Assert
            assertAll(
                    () -> assertNotNull(result, "Should find ship at occupied position"),
                    () -> assertEquals(ship, result, "Returned ship should match expected"),
                    () -> assertEquals("Positioned Ship", result.toString().split(" ")[0],
                            "Ship name should match")
            );
        }

        @Test
        @DisplayName("❌ Return Null for Unoccupied Position")
        void shipAt_PositionNotOccupied_ShouldReturnNull() {
            // Arrange
            MockPosition position = new MockPosition(10, 10);
            IShip ship = createMockShip("Distant Ship", "Galeao", 5, 0, 0, Compass.EAST, true, false);
            fleet.addShip(ship);

            // Act
            IShip result = fleet.shipAt(position);

            // Assert
            assertNull(result, "Should return null for unoccupied position");
        }

        @Test
        @DisplayName("🌊 Handle Empty Fleet in Position Query")
        void shipAt_EmptyFleet_ShouldReturnNull() {
            // Arrange
            MockPosition position = new MockPosition(0, 0);

            // Act
            IShip result = fleet.shipAt(position);

            // Assert
            assertNull(result, "Should return null for empty fleet");
        }

        @ParameterizedTest(name = "Position ({0},{1}) - should find ship: {2}")
        @DisplayName("🗺️ Comprehensive Position Validation")
        @MethodSource("providePositionTestCases")
        void shipAt_VariousPositions(int posX, int posY, boolean shouldFindShip, String description) {
            // Arrange - Create a horizontal ship from (1,1) to (3,1)
            IShip ship = createMockShip("Test Ship", "Nau", 3, 1, 1, Compass.EAST, true, false);
            fleet.addShip(ship);
            MockPosition position = new MockPosition(posX, posY);

            // Act
            IShip result = fleet.shipAt(position);

            // Assert
            if (shouldFindShip) {
                assertNotNull(result, "Should find ship at " + description);
                assertEquals(ship, result, "Returned ship should match expected");
            } else {
                assertNull(result, "Should not find ship at " + description);
            }
        }

        private static Stream<Arguments> providePositionTestCases() {
            return Stream.of(
                    Arguments.of(1, 1, true, "ship start position"),
                    Arguments.of(2, 1, true, "ship middle position"),
                    Arguments.of(3, 1, true, "ship end position"),
                    Arguments.of(0, 1, false, "position before ship"),
                    Arguments.of(4, 1, false, "position after ship"),
                    Arguments.of(2, 0, false, "different row above"),
                    Arguments.of(2, 2, false, "different row below"),
                    Arguments.of(1, 2, false, "diagonal position")
            );
        }
    }

    @Nested
    @DisplayName("📊 Output and Display Operations")
    class OutputTests {

        @Test
        @DisplayName("📋 Print Comprehensive Fleet Status")
        void printStatus_ShouldPrintAllStatusInformation() {
            // Arrange
            IShip ship1 = createMockShip("Santa Maria", "Caravela", 3, 0, 0, Compass.EAST, true, false);
            IShip ship2 = createMockShip("Sao Gabriel", "Nau", 4, 3, 0, Compass.EAST, false, false);
            fleet.addShip(ship1);
            fleet.addShip(ship2);

            // Act
            fleet.printStatus();

            // Assert
            String output = outputStream.toString();
            assertAll(
                    () -> assertTrue(output.contains("Santa Maria"), "Should print first ship name"),
                    () -> assertTrue(output.contains("Sao Gabriel"), "Should print second ship name"),
                    () -> assertFalse(output.isEmpty(), "Output should not be empty")
            );
        }

        @Test
        @DisplayName("🚢 Print Ships Filtered by Specific Category")
        void printShipsByCategory_ValidCategory_ShouldPrintShips() {
            // Arrange
            IShip ship1 = createMockShip("Galleon 1", "Galeao", 5, 0, 0, Compass.EAST, true, false);
            IShip ship2 = createMockShip("Galleon 2", "Galeao", 5, 5, 0, Compass.EAST, true, false);
            IShip otherShip = createMockShip("Frigate", "Fragata", 4, 0, 5, Compass.EAST, true, false);

            fleet.addShip(ship1);
            fleet.addShip(ship2);
            fleet.addShip(otherShip);

            // Act
            fleet.printShipsByCategory("Galeao");

            // Assert
            String output = outputStream.toString();
            assertAll(
                    () -> assertTrue(output.contains("Galleon 1"), "Should print first galleon"),
                    () -> assertTrue(output.contains("Galleon 2"), "Should print second galleon"),
                    () -> assertFalse(output.contains("Frigate"), "Should not print ships from other categories")
            );
        }

        @Test
        @DisplayName("🛟 Print Only Floating Ships")
        void printFloatingShips_ShouldPrintOnlyFloatingShips() {
            // Arrange
            IShip floatingShip = createMockShip("Survivor", "Galeao", 5, 0, 0, Compass.EAST, true, false);
            IShip sunkShip = createMockShip("Sunken", "Fragata", 4, 5, 0, Compass.EAST, false, false);
            fleet.addShip(floatingShip);
            fleet.addShip(sunkShip);

            // Act
            fleet.printFloatingShips();

            // Assert
            String output = outputStream.toString();
            assertAll(
                    () -> assertTrue(output.contains("Survivor"), "Should print floating ship"),
                    () -> assertFalse(output.contains("Sunken"), "Should not print sunk ship")
            );
        }

        @Test
        @DisplayName("📜 Print All Ships in Fleet")
        void printAllShips_ShouldPrintAllShips() {
            // Arrange
            IShip ship1 = createMockShip("Ship Alpha", "Galeao", 5, 0, 0, Compass.EAST, true, false);
            IShip ship2 = createMockShip("Ship Beta", "Fragata", 4, 5, 0, Compass.EAST, false, false);
            fleet.addShip(ship1);
            fleet.addShip(ship2);

            // Act
            fleet.printAllShips();

            // Assert
            String output = outputStream.toString();
            assertAll(
                    () -> assertTrue(output.contains("Ship Alpha"), "Should print first ship"),
                    () -> assertTrue(output.contains("Ship Beta"), "Should print second ship")
            );
        }

        @Test
        @DisplayName("🖨️ Static PrintShips Method Should Print Given List")
        void staticPrintShips_ShouldPrintAllShipsInList() {
            // Arrange
            List<IShip> ships = new ArrayList<>();
            ships.add(createMockShip("Static Ship 1", "Galeao", 5, 0, 0, Compass.EAST, true, false));
            ships.add(createMockShip("Static Ship 2", "Fragata", 4, 5, 0, Compass.EAST, true, false));

            // Act
            Fleet.printShips(ships);

            // Assert
            String output = outputStream.toString();
            assertAll(
                    () -> assertTrue(output.contains("Static Ship 1"), "Should print first static ship"),
                    () -> assertTrue(output.contains("Static Ship 2"), "Should print second static ship")
            );
        }
    }

    // ===== SPECIALIZED TESTS =====

    @Test
    @Disabled("🚧 Under Construction - Advanced Formation Logic")
    @DisplayName("🔮 Future Feature: Ship Formation Validation")
    void testShipFormations_NotYetImplemented() {
        // This test will validate complex ship formations
        // Currently disabled as the feature is in development
        fail("Ship formation validation not yet implemented");
    }

    @Test
    @DisplayName("💻 Windows-Specific Fleet Operations")
    void windowsOnlyTest() {
        assumeTrue(WINDOWS.isCurrentOs(), "Test designed for Windows environment");

        // Windows-specific fleet operations would go here
        IShip ship = createMockShip("Windows Ship", "Barca", 2, 0, 0, Compass.EAST, true, false);
        boolean result = fleet.addShip(ship);

        assertTrue(result, "Windows-specific fleet operation should succeed");
    }

    @Test
    @DisplayName("☕️ Java 11+ Compatibility Check")
    void java11PlusTest() {
        assumeTrue(JAVA_17.isCurrentVersion() || JAVA_21.isCurrentVersion(),
                "Test requires Java 11 or higher");

        // Test modern Java features compatibility
        IShip ship = createMockShip("Modern Ship", "Nau", 3, 0, 0, Compass.EAST, true, false);
        var ships = List.of(ship); // Using 'var' from Java 10+

        fleet.addShip(ship);
        var result = fleet.getShips();

        assertFalse(result.isEmpty(), "Should work with modern Java features");
    }

    @Test
    @DisplayName("⚡️ Performance: Rapid Sequential Ship Additions")
    void performance_RapidShipAdditions() {
        // Test performance with multiple rapid additions
        int numberOfShips = 5;

        for (int i = 0; i < numberOfShips; i++) {
            IShip ship = createMockShip("FastShip" + i, "Barca", 2, i * 2, 0, Compass.EAST, true, false);
            assertTrue(fleet.addShip(ship), "Should rapidly add ship #" + i);
        }

        assertEquals(numberOfShips, fleet.getShips().size(),
                "All rapid additions should be successful");
    }

    // ===== HELPER METHODS =====

    private IShip createMockShip(String name, String category, int size, int startX, int startY,
                                 Compass bearing, boolean floating, boolean tooClose) {
        return new MockShip(name, category, size, startX, startY, bearing, floating, tooClose);
    }

    // ===== MOCK IMPLEMENTATIONS =====

    private static class MockShip implements IShip {
        private String name;
        private String category;
        private int size;
        private int startX, startY;
        private Compass bearing;
        private boolean floating;
        private boolean tooClose;
        private List<IPosition> positions;

        public MockShip(String name, String category, int size, int startX, int startY,
                        Compass bearing, boolean floating, boolean tooClose) {
            this.name = name;
            this.category = category;
            this.size = size;
            this.startX = startX;
            this.startY = startY;
            this.bearing = bearing;
            this.floating = floating;
            this.tooClose = tooClose;
            this.positions = createPositions();
        }

        private List<IPosition> createPositions() {
            List<IPosition> posList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                int x = startX + (bearing == Compass.EAST ? i : 0);
                int y = startY + (bearing == Compass.SOUTH ? i : 0);
                MockPosition pos = new MockPosition(x, y);
                pos.occupy();
                posList.add(pos);
            }
            return posList;
        }

        @Override
        public String getCategory() { return category; }

        @Override
        public Integer getSize() { return size; }

        @Override
        public List<IPosition> getPositions() { return new ArrayList<>(positions); }

        @Override
        public IPosition getPosition() { return positions.get(0); }

        @Override
        public Compass getBearing() { return bearing; }

        @Override
        public boolean stillFloating() { return floating; }

        @Override
        public int getTopMostPos() {
            return bearing == Compass.SOUTH ? startY : startY;
        }

        @Override
        public int getBottomMostPos() {
            return bearing == Compass.SOUTH ? startY + size - 1 : startY;
        }

        @Override
        public int getLeftMostPos() {
            return bearing == Compass.EAST ? startX : startX;
        }

        @Override
        public int getRightMostPos() {
            return bearing == Compass.EAST ? startX + size - 1 : startX;
        }

        @Override
        public boolean occupies(IPosition pos) {
            return positions.stream().anyMatch(p -> p.equals(pos));
        }

        @Override
        public boolean tooCloseTo(IShip other) {
            return tooClose;
        }

        @Override
        public boolean tooCloseTo(IPosition pos) {
            return positions.stream().anyMatch(p -> p.isAdjacentTo(pos));
        }

        @Override
        public void shoot(IPosition pos) {
            positions.stream()
                    .filter(p -> p.equals(pos))
                    .findFirst()
                    .ifPresent(IPosition::shoot);
        }

        @Override
        public String toString() {
            return String.format("%s (%s) [%d] at (%d,%d) facing %s",
                    name, category, size, startX, startY, bearing);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof MockShip)) return false;
            MockShip other = (MockShip) obj;
            return this.name.equals(other.name) &&
                    this.startX == other.startX &&
                    this.startY == other.startY;
        }

        @Override
        public int hashCode() {
            return name.hashCode() + startX * 31 + startY;
        }
    }

    private static class MockPosition implements IPosition {
        private int row, column;
        private boolean occupied = false;
        private boolean hit = false;

        public MockPosition(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public int getRow() { return row; }

        @Override
        public int getColumn() { return column; }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof IPosition)) return false;
            IPosition that = (IPosition) other;
            return this.row == that.getRow() && this.column == that.getColumn();
        }

        @Override
        public boolean isAdjacentTo(IPosition other) {
            int rowDiff = Math.abs(this.row - other.getRow());
            int colDiff = Math.abs(this.column - other.getColumn());
            return (rowDiff <= 1 && colDiff <= 1) && !(rowDiff == 0 && colDiff == 0);
        }

        @Override
        public void occupy() { this.occupied = true; }

        @Override
        public void shoot() { this.hit = true; }

        @Override
        public boolean isOccupied() { return occupied; }

        @Override
        public boolean isHit() { return hit; }

        @Override
        public String toString() {
            return String.format("Pos(%d,%d)%s%s", row, column,
                    occupied ? "[O]" : "", hit ? "[X]" : "");
        }

        @Override
        public int hashCode() {
            return row * 31 + column;
        }
    }
}