package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes parametrizados da classe Frigate (tamanho 4)")
class FrigateTest {

    @ParameterizedTest(name = "Frigate com direção {0} deve gerar 4 posições válidas e distintas")
    @ValueSource(strings = {"NORTH", "SOUTH", "EAST", "WEST"})
    void testFrigateForAllDirections(String dir) {
        Position pos = new Position(3, 3);
        Frigate f = new Frigate(Compass.valueOf(dir), pos);

        // Verificações genéricas
        assertEquals(4, f.getSize(), "Tamanho incorreto");
        assertEquals(4, f.getPositions().size(), "Número de posições incorreto");

        // Todas as posições devem ser distintas
        long distinctCount = f.getPositions().stream()
                .map(p -> p.getRow() + "," + p.getColumn())
                .distinct()
                .count();
        assertEquals(4, distinctCount, "As posições do Frigate devem ser únicas");

        // Primeira posição deve corresponder à posição inicial
        IPosition first = f.getPositions().get(0);
        assertAll(
                () -> assertEquals(pos.getRow(), first.getRow(), "Linha inicial incorreta"),
                () -> assertEquals(pos.getColumn(), first.getColumn(), "Coluna inicial incorreta")
        );
    }

    @ParameterizedTest(name = "Frigate criada em ({0}, {1}) deve conter a posição inicial")
    @CsvSource({
            "1,1",
            "2,3",
            "5,4"
    })
    void testFrigateContainsInitialPosition(int row, int col) {
        Position pos = new Position(row, col);
        Frigate f = new Frigate(Compass.NORTH, pos);

        boolean containsStart = f.getPositions().stream()
                .anyMatch(p -> p.getRow() == row && p.getColumn() == col);

        assertTrue(containsStart, "A posição inicial deve fazer parte do Frigate");
    }

    @ParameterizedTest(name = "Frigate deve aceitar direção {0} sem exceções")
    @ValueSource(strings = {"NORTH", "SOUTH", "EAST", "WEST"})
    void testValidDirectionsNoException(String dir) {
        assertDoesNotThrow(() -> new Frigate(Compass.valueOf(dir), new Position(4,4)));
    }
}
