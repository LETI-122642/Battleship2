package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Position")
class PositionTest {

    @Nested
    @DisplayName("Testes do construtor e getters")
    class ConstructorTests {

        @Test
        @DisplayName("Construtor deve atribuir corretamente row e column")
        void testConstructor() {
            Position p = new Position(3, 5);
            assertEquals(3, p.getRow());
            assertEquals(5, p.getColumn());
        }

        @Test
        @DisplayName("Flags iniciais: isOccupied e isHit começam false")
        void testInitialFlags() {
            Position p = new Position(4, 7);
            assertFalse(p.isOccupied());
            assertFalse(p.isHit());
        }
    }

    @Nested
    @DisplayName("Testes de equals e hashCode")
    class EqualsTests {

        @Test
        @DisplayName("Posições com mesmo row/column são iguais")
        void testEqualsTrue() {
            Position p1 = new Position(2, 8);
            Position p2 = new Position(2, 8);
            assertEquals(p1, p2);
        }

        @Test
        @DisplayName("Posições com row/column diferentes não são iguais")
        void testEqualsFalse() {
            Position p1 = new Position(1, 2);
            Position p2 = new Position(5, 9);
            assertNotEquals(p1, p2);
        }

        @Test
        @DisplayName("equals deve retornar false para objetos de outro tipo")
        void testEqualsDifferentType() {
            Position p1 = new Position(1, 1);
            String s = "não sou posição";
            assertFalse(p1.equals(s));
        }

        @Test
        @DisplayName("hashCode consistente com equals")
        void testHashCode() {
            Position p1 = new Position(3, 4);
            Position p2 = new Position(3, 4);
            assertEquals(p1.hashCode(), p2.hashCode());
        }
    }

    @Nested
    @DisplayName("Testes de adjacência")
    class AdjacentTests {

        @Test
        @DisplayName("Posições diretamente vizinhas são adjacentes")
        void testAdjacentDirect() {
            Position p1 = new Position(5, 5);
            Position p2 = new Position(5, 6);
            assertTrue(p1.isAdjacentTo(p2));
        }

        @Test
        @DisplayName("Posições diagonais também são adjacentes")
        void testAdjacentDiagonal() {
            Position p1 = new Position(5, 5);
            Position p2 = new Position(6, 6);
            assertTrue(p1.isAdjacentTo(p2));
        }

        @Test
        @DisplayName("Posições distantes não são adjacentes")
        void testNotAdjacent() {
            Position p1 = new Position(2, 2);
            Position p2 = new Position(4, 2);
            assertFalse(p1.isAdjacentTo(p2));
        }
    }

    @Nested
    @DisplayName("Testes de ocupação")
    class OccupyTests {

        @Test
        @DisplayName("occupy deve marcar posição como ocupada")
        void testOccupy() {
            Position p = new Position(1, 1);
            p.occupy();
            assertTrue(p.isOccupied());
        }
    }

    @Nested
    @DisplayName("Testes de tiro")
    class ShootTests {

        @Test
        @DisplayName("shoot deve marcar posição como atingida")
        void testShoot() {
            Position p = new Position(2, 2);
            p.shoot();
            assertTrue(p.isHit());
        }
    }

    @Nested
    @DisplayName("Testes do toString")
    class ToStringTests {

        @Test
        @DisplayName("toString deve conter row e column no formato correto")
        void testToString() {
            Position p = new Position(7, 9);
            String txt = p.toString();

            assertTrue(txt.contains("Linha = 7"));
            assertTrue(txt.contains("Coluna = 9"));
        }
    }
}
