package iscteiul.ista.battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe genérica Ship")
class ShipTest {

    Ship ship;

    @BeforeEach
    void setup() {
        // Criamos uma BARCA (tamanho 1) para testar comportamento simples
        ship = Ship.buildShip("barca", Compass.NORTH, new Position(3, 'C'));

        // Preenchimento manual das posições, já que as subclasses o fazem no construtor
        ship.getPositions().add(new Position(3, 'C'));
    }

    // -----------------------------------------------------------
    @Nested
    @DisplayName("Testes da construção do navio")
    class ConstructionTests {

        @Test
        @DisplayName("Categoria deve ser correta")
        void testCategory() {
            // O category real vem como "Barca" (B maiúsculo)
            assertEquals("Barca", ship.getCategory());
        }

        @Test
        @DisplayName("Bearing deve ser o mesmo passado no construtor")
        void testBearing() {
            assertEquals(Compass.NORTH, ship.getBearing());
        }

        @Test
        @DisplayName("Posição inicial deve ser correta")
        void testInitialPosition() {
            assertEquals(new Position(3, 'C'), ship.getPosition());
        }

        @Test
        @DisplayName("toString deve conter category, bearing e position")
        void testToString() {
            String txt = ship.toString();

            assertTrue(txt.toLowerCase().contains("barca"));
            assertTrue(txt.contains(String.valueOf(ship.getBearing())));
            assertTrue(txt.contains("Linha = " + ship.getPosition().getRow()));
            assertTrue(txt.contains("Coluna = " + ship.getPosition().getColumn()));
        }



    }

    // -----------------------------------------------------------
    @Nested
    @DisplayName("Testes sobre as posições do navio")
    class PositionTests {

        @Test
        @DisplayName("getPositions deve devolver lista não vazia")
        void testPositionsListNotEmpty() {
            List<IPosition> positions = ship.getPositions();
            assertFalse(positions.isEmpty());
        }

        @Test
        @DisplayName("occupies deve retornar true para posição correta")
        void testOccupiesCorrectPos() {
            assertTrue(ship.occupies(new Position(3, 'C')));
        }

        @Test
        @DisplayName("occupies deve retornar false para posição errada")
        void testOccupiesWrongPos() {
            assertFalse(ship.occupies(new Position(4, 'D')));
        }

        @Test
        @DisplayName("Top/Bottom/Left/Right Most para navio de 1 posição")
        void testExtremePositionsForSinglePositionShip() {
            assertEquals(3, ship.getTopMostPos());
            assertEquals(3, ship.getBottomMostPos());
            assertEquals('C', ship.getLeftMostPos());
            assertEquals('C', ship.getRightMostPos());
        }
    }

    // -----------------------------------------------------------
    @Nested
    @DisplayName("Testes do comportamento de tiro e flutuação")
    class ShootingTests {

        @Test
        @DisplayName("Navio de 1 posição começa flutuante")
        void testStillFloatingInitially() {
            assertTrue(ship.stillFloating());
        }

        @Test
        @DisplayName("Depois de acertar, navio deixa de flutuar")
        void testShipSinks() {
            ship.shoot(new Position(3, 'C'));
            assertFalse(ship.stillFloating());
        }

        @Test
        @DisplayName("Tiro em posição vazia não altera estado")
        void testShootWrongPosition() {
            ship.shoot(new Position(5, 'D'));
            assertTrue(ship.stillFloating());
        }
    }

    // -----------------------------------------------------------
    @Nested
    @DisplayName("Testes sobre proximidade (tooCloseTo)")
    class DistanceTests {

        @Test
        @DisplayName("Navios demasiado próximos devem ser detectados")
        void testTooCloseTrue() {
            Ship other =
                    Ship.buildShip("barca", Compass.NORTH, new Position(3, 'D'));
            other.getPositions().add(new Position(3, 'D'));

            assertTrue(ship.tooCloseTo(other));
        }

        @Test
        @DisplayName("Navios com distância segura NÃO devem ser detectados")
        void testTooCloseFalse() {
            Ship other =
                    Ship.buildShip("barca", Compass.NORTH, new Position(6, 'F'));
            other.getPositions().add(new Position(6, 'F'));

            assertFalse(ship.tooCloseTo(other));
        }
    }
}
