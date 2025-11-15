package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Compass")
class CompassTest {

    @Nested
    @DisplayName("Testes das direções e do toString")
    class BasicTests {

        @Test
        @DisplayName("getDirection deve devolver o char correto de cada direção")
        void testGetDirection() {
            assertEquals('n', Compass.NORTH.getDirection());
            assertEquals('s', Compass.SOUTH.getDirection());
            assertEquals('e', Compass.EAST.getDirection());
            assertEquals('o', Compass.WEST.getDirection());
            assertEquals('u', Compass.UNKNOWN.getDirection());
        }

        @Test
        @DisplayName("toString deve devolver a String correta de cada direção")
        void testToString() {
            assertEquals("n", Compass.NORTH.toString());
            assertEquals("s", Compass.SOUTH.toString());
            assertEquals("e", Compass.EAST.toString());
            assertEquals("o", Compass.WEST.toString());
            assertEquals("u", Compass.UNKNOWN.toString());
        }
    }

    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Testes do método charToCompass")
    class CharToCompassTests {

        @Test
        @DisplayName("charToCompass mapeia corretamente caracteres válidos")
        void testValidMapping() {
            assertEquals(Compass.NORTH, Compass.charToCompass('n'));
            assertEquals(Compass.SOUTH, Compass.charToCompass('s'));
            assertEquals(Compass.EAST, Compass.charToCompass('e'));
            assertEquals(Compass.WEST, Compass.charToCompass('o'));
        }

        @Test
        @DisplayName("charToCompass devolve UNKNOWN para caracteres inválidos")
        void testUnknownMapping() {
            assertEquals(Compass.UNKNOWN, Compass.charToCompass('x'));
            assertEquals(Compass.UNKNOWN, Compass.charToCompass('?'));
            assertEquals(Compass.UNKNOWN, Compass.charToCompass(' '));
        }
    }
}
