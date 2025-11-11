package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes parametrizados da classe Galleon (tamanho 5)")
class GalleonTest {

    @ParameterizedTest(name = "Galleon com direção {0} deve gerar 5 posições válidas e distintas")
    @ValueSource(strings = {"NORTH", "SOUTH", "EAST", "WEST"})
    void testGalleonForAllDirections(String dir) {
        Position pos = new Position(2, 2);
        Galleon g = new Galleon(Compass.valueOf(dir), pos);

        // Verificações gerais (válidas para todas as direções)
        assertEquals(5, g.getSize(), "Tamanho incorreto");
        assertEquals(5, g.getPositions().size(), "Número de posições incorreto");

        // Garantir que todas as posições são diferentes (sem duplicados)
        long distinctCount = g.getPositions().stream()
                .map(p -> p.getRow() + "," + p.getColumn())
                .distinct()
                .count();
        assertEquals(5, distinctCount, "As posições não são únicas");

        // A primeira posição deve sempre corresponder à posição inicial
        IPosition first = g.getPositions().get(0);
        assertAll(
                () -> assertEquals(pos.getRow(), first.getRow(), "Linha inicial incorreta"),
                () -> assertEquals(pos.getColumn(), first.getColumn(), "Coluna inicial incorreta")
        );
    }

    @ParameterizedTest(name = "Galleon criada em ({0}, {1}) deve conter a posição inicial")
    @CsvSource({
            "1,1",
            "2,3",
            "4,5"
    })
    void testGalleonContainsInitialPosition(int row, int col) {
        Position pos = new Position(row, col);
        Galleon g = new Galleon(Compass.NORTH, pos);

        boolean containsStart = g.getPositions().stream()
                .anyMatch(p -> p.getRow() == row && p.getColumn() == col);

        assertTrue(containsStart, "A posição inicial não foi incluída nas posições do galeão");
    }

    @ParameterizedTest(name = "Galleon deve aceitar direção {0} sem exceções")
    @ValueSource(strings = {"NORTH", "SOUTH", "EAST", "WEST"})
    void testValidDirectionsNoException(String dir) {
        assertDoesNotThrow(() -> new Galleon(Compass.valueOf(dir), new Position(3,3)));
    }
}
