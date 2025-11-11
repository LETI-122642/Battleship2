package iscteiul.ista.battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes parametrizados da classe Barge (tamanho 1)")
class BargeTest {

    @ParameterizedTest(name = "Barge com direção {0} deve gerar uma posição correta")
    @ValueSource(strings = {"NORTH", "SOUTH", "EAST", "WEST"})
    void testBargeForAllDirections(String dir) {
        Position pos = new Position(2, 3);
        Barge b = new Barge(Compass.valueOf(dir), pos);

        assertEquals(1, b.getSize(), "Tamanho deve ser 1");
        assertEquals(1, b.getPositions().size(), "A Barge deve ocupar exatamente uma posição");

        IPosition p = b.getPositions().get(0);
        assertAll(
                () -> assertEquals(pos.getRow(), p.getRow(), "Linha incorreta"),
                () -> assertEquals(pos.getColumn(), p.getColumn(), "Coluna incorreta")
        );
    }

    @ParameterizedTest(name = "Barge criada em ({0}, {1}) deve manter coordenadas iniciais")
    @org.junit.jupiter.params.provider.CsvSource({
            "1,1",
            "2,5",
            "4,7"
    })
    void testBargePositionCoordinates(int row, int col) {
        Position pos = new Position(row, col);
        Barge b = new Barge(Compass.NORTH, pos);
        IPosition p = b.getPositions().get(0);

        assertAll(
                () -> assertEquals(row, p.getRow()),
                () -> assertEquals(col, p.getColumn())
        );
    }
}
